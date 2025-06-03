package org.linkki.ips.ui.element;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.KaribuUtils.getTextContent;

import org.faktorips.runtime.IMarker;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.framework.ui.dialogs.UIOpenDialogButton;
import org.linkki.ips.test.model.TestIpsObject;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@ExtendWith(KaribuUIExtension.class)
class IpsDialogPmoIntegrationTest {

    @Test
    void testDialogValidation() {
        var ipsObject = new TestIpsObject();
        var sectionPmo = new TestSectionPmo(ipsObject);
        var section = VaadinUiCreator.createComponent(sectionPmo, new BindingContext());
        var dialogButton = _get(section, Button.class);

        dialogButton.click();
        var dialog = _get(OkCancelDialog.class);
        var dialogTextField = _get(dialog, TextField.class);
        KaribuUtils.Fields.setValue(dialogTextField, "");
        var filteredValidationMessages = _find(dialog, LinkkiText.class,
                                               ss -> ss.withClasses("linkki-dialog-message-area"));

        assertThat(filteredValidationMessages)
                .as("The required field message should not be displayed before clicking ok")
                .isEmpty();

        KaribuUtils.Dialogs.clickOkButton();

        assertThat(dialog.getContent().isOpened()).isTrue();
        assertThat(KaribuUtils.Dialogs.getOkButton(dialog).isEnabled()).isFalse();
        assertThat(getTextContent(KaribuUtils.Dialogs.getFirstMessage(dialog)))
                .as("The dialog should show an error message")
                .isEqualTo("The new text cannot be empty.");

        KaribuUtils.Fields.setValue(dialogTextField, "test");
        var validValueValidationMessages = _find(dialog, LinkkiText.class,
                                                 ss -> ss.withClasses("linkki-dialog-message-area"));

        assertThat(validValueValidationMessages).isEmpty();

        KaribuUtils.Dialogs.clickOkButton();

        assertThat(_find(Dialog.class)).isEmpty();
        var label = _get(section, Component.class, ss -> ss.withId("foo"));
        assertThat(getTextContent(label)).isEqualTo("test");
    }

    @UISection
    static class TestSectionPmo {

        @ModelObject
        private final TestIpsObject ipsObject;

        TestSectionPmo(TestIpsObject ipsObject) {
            this.ipsObject = ipsObject;
        }

        @UILabel(position = 10, modelAttribute = TestIpsObject.PROPERTY_FOO)
        public void foo() {
            // model binding
        }

        @UIOpenDialogButton(position = 20, caption = "Edit foo")
        public DialogPmo getDialog() {
            return new TestIpsDialogPmo(ipsObject);
        }
    }

    static class TestIpsDialogPmo extends IpsDialogPmo {

        private final TestIpsObject ipsObject;
        private final TestContentPmo contentPmo;

        public TestIpsDialogPmo(TestIpsObject ipsObject) {
            this.ipsObject = ipsObject;
            this.contentPmo = new TestContentPmo(ipsObject.getFoo());
        }

        @Override
        public String getCaption() {
            return "Dialog Caption";
        }

        @Override
        public Handler getOkHandler() {
            return () -> ipsObject.setFoo(contentPmo.getFoo());
        }

        @Override
        public MessageList getIpsMessages() {
            return StringUtils.isBlank(contentPmo.getFoo())
                    ? MessageList.of(new Message.Builder("The new text cannot be empty.", Severity.ERROR)
                            .markers(new TestMarker()).create())
                    : new MessageList();
        }

        @Override
        public TestContentPmo getContentPmo() {
            return contentPmo;
        }

        @UISection
        static class TestContentPmo {
            private String foo;

            public TestContentPmo(String foo) {
                this.foo = foo;
            }

            @CheckForNull
            @UITextField(position = 10)
            public String getFoo() {
                return this.foo;
            }

            public void setFoo(@CheckForNull String text) {
                this.foo = text;
            }
        }
    }

    private static final class TestMarker implements IMarker {

        @Override
        public boolean isRequiredInformationMissing() {
            return true;
        }

        @Override
        public boolean isTechnicalConstraintViolated() {
            return false;
        }
    }
}