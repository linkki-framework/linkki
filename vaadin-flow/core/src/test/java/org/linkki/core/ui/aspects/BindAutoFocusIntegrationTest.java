package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindAutoFocus;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

class BindAutoFocusIntegrationTest {

    private final TestWithBindAutoFocusPmo pmo = new TestWithBindAutoFocusPmo();
    private final BindingContext bindingContext = new BindingContext();
    private final Map<String, Component> uiElements = UiCreator
            .createUiElements(pmo,
                              bindingContext,
                              c -> new NoLabelComponentWrapper((Component)c))
            .map(VaadinComponentWrapper::getComponent)
            .collect(Collectors.toMap(k -> k.getId().get(), v -> v));

    @Test
    void testAspectBindAutoFocus_NoAnnotationSet() {
        TextField component = (TextField)uiElements.get("fieldWithoutAutoFocus");

        assertThat(component.isAutofocus(), is(false));
    }

    @Test
    void testAspectBindAutoFocus_AnnotationSet() {
        TextField component = (TextField)uiElements.get("fieldWithAutoFocus");

        assertThat(component.isAutofocus(), is(true));
    }

    static class TestWithBindAutoFocusPmo {

        public TestWithBindAutoFocusPmo() {

        }

        @UITextField(position = 10)
        public String getFieldWithoutAutoFocus() {
            return "Field without autofocus";
        }

        @BindAutoFocus
        @UITextField(position = 20)
        public String getFieldWithAutoFocus() {
            return "Field with autofocus";
        }
    }
}
