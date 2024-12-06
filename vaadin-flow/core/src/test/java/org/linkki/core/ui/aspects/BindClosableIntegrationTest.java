package org.linkki.core.ui.aspects;

import static org.linkki.test.matcher.Matchers.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindClosable;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.LinkkiSection;

class BindClosableIntegrationTest {

    @Test
    void testBindClosable_Default() {
        var bindingContext = new BindingContext();
        var pmo = new TestBindClosableDefaultPmo();
        var component = (LinkkiSection)VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(component.isClosable());
        assertThat(component.isOpen());
    }

    @Test
    void testBindClosable_InitialTrue() {
        var bindingContext = new BindingContext();
        var pmo = new TestBindClosableInitialTruePmo();
        var component = (LinkkiSection)VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(component.isClosable());
        assertThat(component.isClosed());
    }

    @Test
    void testBindClosable_InitialFalse() {
        var bindingContext = new BindingContext();
        var pmo = new TestBindClosableInitialFalsePmo();
        var component = (LinkkiSection)VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(component.isClosable());
        assertThat(component.isOpen());
    }

    @BindClosable
    @UISection
    static class TestBindClosableDefaultPmo {

        @UILabel(position = 10)
        public String getLabel() {
            return "I am a label.";
        }
    }

    @BindClosable(initial = true)
    @UISection
    static class TestBindClosableInitialTruePmo {

        @UILabel(position = 10)
        public String getLabel() {
            return "I am a label.";
        }
    }

    @BindClosable(initial = false)
    @UISection
    static class TestBindClosableInitialFalsePmo {

        @UILabel(position = 10)
        public String getLabel() {
            return "I am a label.";
        }
    }
}