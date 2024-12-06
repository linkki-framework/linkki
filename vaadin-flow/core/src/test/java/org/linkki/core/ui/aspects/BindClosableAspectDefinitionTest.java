package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.section.LinkkiSection;

class BindClosableAspectDefinitionTest {

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testCreateComponentValueSetter(boolean initial) {
        var component = new LinkkiSection("caption");
        var valueSetter = new BindClosableAspectDefinition(initial)
                .createComponentValueSetter(new NoLabelComponentWrapper(component));

        valueSetter.accept(initial);

        assertThat(component.isClosable()).isTrue();
        assertThat(component.isClosed()).isEqualTo(initial);
    }

}