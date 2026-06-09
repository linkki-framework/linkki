package org.linkki.core.ui.aspects;

import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindValueChangeMode;
import org.linkki.core.ui.uiframework.VaadinComponentWrapperFactory;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

class BindValueChangeModeAspectDefinitionTest {

    private final TextField textField = new TextField();
    private final ComponentWrapper componentWrapper = VaadinComponentWrapperFactory.INSTANCE
            .createComponentWrapper(textField);
    private final PropertyDispatcher dispatcher = new StaticValueDispatcher(mock(PropertyDispatcher.class));

    @EnumSource(ValueChangeMode.class)
    @ParameterizedTest
    void testCreateUiUpdater(ValueChangeMode mode) {
        var aspectDefinition = new BindValueChangeMode.BindValueChangeModeAspectDefinition(mode);
        aspectDefinition.createUiUpdater(dispatcher, componentWrapper);

        Assertions.assertThat(textField.getValueChangeMode()).isEqualTo(mode);
    }

}
