package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

class BindVariantNamesIntegrationTest {

    private final TestWithBindVariantNamesPmo pmo = new TestWithBindVariantNamesPmo();
    private final BindingContext bindingContext = new BindingContext();
    private final Map<String, Component> uiElements = UiCreator
            .createUiElements(pmo,
                              bindingContext,
                              c -> new NoLabelComponentWrapper((Component)c))
            .map(VaadinComponentWrapper::getComponent)
            .collect(Collectors.toMap(k -> k.getId().get(), v -> v));

    @Test
    void testAspectBindVariantNames_AnnotationSet() {
        TextField component = (TextField)uiElements.get("fieldWithVariant");

        assertThat(component.getThemeNames(), contains("greco-roman"));
    }

    @Test
    void testAspectBindVariantNames_NoAnnotationSet() {
        TextField component = (TextField)uiElements.get("fieldWithoutVariant");

        assertThat(component.getThemeNames(), is(empty()));
    }

    static class TestWithBindVariantNamesPmo {

        public TestWithBindVariantNamesPmo() {

        }

        @UITextField(position = 10)
        public String getFieldWithoutVariant() {
            return "Field without variant";
        }

        @BindVariantNames(value="greco-roman")
        @UITextField(position = 20)
        public String getFieldWithVariant() {
            return "Field with variant";
        }
    }
}
