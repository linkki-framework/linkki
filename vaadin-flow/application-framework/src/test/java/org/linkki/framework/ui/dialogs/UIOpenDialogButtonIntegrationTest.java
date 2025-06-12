/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.framework.ui.dialogs;

import static com.github.mvysny.kaributesting.v10.LocatorJ._find;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.KaribuUtils.getTextContent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.test.KaribuUtils.Dialogs;
import org.linkki.core.ui.test.KaribuUtils.Fields;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.util.handler.Handler;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@ExtendWith(KaribuUIExtension.class)
class UIOpenDialogButtonIntegrationTest {

    @Test
    void testDialogCreatorFunction() {

        @UISection
        class TestPmo {

            private final AtomicInteger counter = new AtomicInteger();

            @UILabel(position = 0)
            public Integer getCounter() {
                return counter.get();
            }

            @UIOpenDialogButton(position = 1)
            public Function<Handler, OkCancelDialog> getDialog() {
                return modelChanged -> OkCancelDialog.builder("dialog caption")
                        .okHandler(modelChanged.compose(counter::incrementAndGet))
                        .build();
            }
        }

        var testPmo = new TestPmo();
        var component = VaadinUiCreator.createComponent(testPmo, new BindingContext());
        assertThat(_get(component, LinkkiText.class).getText()).isEqualTo("0");

        _get(component, Button.class).click();

        var dialog = _get(Dialog.class);
        assertThat(KaribuUtils.getComponentTree(dialog))
                .contains("dialog caption");

        Dialogs.clickOkButton();

        assertThat(_get(component, LinkkiText.class).getText()).isEqualTo("1");
    }

    @Test
    void testDialogPmo_ModelChangedAfterDialogOk() {
        var sectionPmo = new TestSectionPmo();
        var section = VaadinUiCreator.createComponent(sectionPmo, new BindingContext());
        var textField = _get(section, TextField.class);
        var dialogButton = _get(section, Button.class);

        assertThat(getTextContent(textField)).isEqualTo("initial");
        assertThat(getTextContent(dialogButton)).isEqualTo("Open Dialog");

        dialogButton.click();
        var dialog = _get(OkCancelDialog.class);
        var dialogTextField = _get(dialog, TextField.class);
        var dialogCaption = _get(dialog, H4.class);

        assertThat(getTextContent(dialogCaption)).isEqualTo("Dialog Caption");

        Fields.setValue(dialogTextField, "changed");
        Dialogs.clickOkButton();

        assertThat(getTextContent(textField))
                .as("The sections textfield should contain the value from the dialogs textfield after the ok button was pressed")
                .isEqualTo("changed");
    }

    @Test
    void testDialogPmo_DialogValidation() {
        var sectionPmo = new TestSectionPmo();
        var section = VaadinUiCreator.createComponent(sectionPmo, new BindingContext());
        var dialogButton = _get(section, Button.class);

        dialogButton.click();
        var dialog = _get(OkCancelDialog.class);
        var dialogTextField = _get(dialog, TextField.class);
        var initialValidationMessages = _find(dialog, LinkkiText.class);

        assertThat(initialValidationMessages).as("Initially, the required field message should not be displayed.")
                .isEmpty();

        Fields.setValue(dialogTextField, "");
        var filteredValidationMessages = _find(dialog, LinkkiText.class);

        assertThat(filteredValidationMessages)
                .as("The required field message should not be displayed before clicking ok")
                .isEmpty();

        Dialogs.clickOkButton();

        assertThat(dialog.getContent().isOpened()).isTrue();
        assertThat(Dialogs.getOkButton(dialog).isEnabled()).isFalse();
        assertThat(getTextContent(Dialogs.getFirstMessage(dialog)))
                .as("The dialog should show an error message")
                .isEqualTo("The new text cannot be empty");

        Fields.setValue(dialogTextField, "test");
        var validValueValidationMessages = _find(dialog, LinkkiText.class);

        assertThat(validValueValidationMessages).isEmpty();
    }

    @UISection
    static class TestSectionPmo {
        private String text = "initial";

        @UITextField(position = 10)
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @UIOpenDialogButton(position = 20, caption = "Open Dialog")
        public DialogPmo getDialog() {
            return new TestDialogPmo(this::setText);
        }
    }

    static class TestDialogPmo implements DialogPmo {

        private final TestContentPmo contentPmo = new TestContentPmo();
        private Consumer<String> okConsumer;

        public TestDialogPmo(Consumer<String> okConsumer) {
            this.okConsumer = okConsumer;
        }

        @Override
        public String getCaption() {
            return "Dialog Caption";
        }

        @Override
        public Handler getOkHandler() {
            return () -> okConsumer.accept(contentPmo.getText());
        }

        @Override
        public MessageList validate() {
            return StringUtils.isBlank(contentPmo.getText())
                    ? new MessageList(Message.builder("The new text cannot be empty", Severity.ERROR)
                            .code("error.blankText")
                            .markers(ValidationMarker.REQUIRED)
                            .create())
                    : new MessageList();
        }

        @Override
        public TestContentPmo getContentPmo() {
            return contentPmo;
        }

        @UISection
        static class TestContentPmo {
            private String text = "Dialog Content";

            @CheckForNull
            @UITextField(position = 10)
            public String getText() {
                return this.text;
            }

            public void setText(@CheckForNull String text) {
                this.text = text;
            }
        }
    }
}
