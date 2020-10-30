package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior.ReadOnlyBehaviorType;
import org.linkki.core.ui.uiframework.Vaadin8ComponentWrapperFactory;
import org.mockito.Mockito;

import com.vaadin.ui.Button;

public class BindReadOnlyBehaviorAspectDefinitionTest {

    private Button button = new Button();
    private ComponentWrapper componentWrapper = Vaadin8ComponentWrapperFactory.INSTANCE.createComponentWrapper(button);
    private PropertyDispatcher propertyDispatcher = Mockito.mock(PropertyDispatcher.class);

    @Test
    public void testCreateUiUpdater_ReadOnlyDisabled() {
        BindReadOnlyBehaviorAspectDefinition aspectDefinition = new BindReadOnlyBehaviorAspectDefinition(
                ReadOnlyBehaviorType.DISABLED);
        setDispatcherReadOnly();
        aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper).apply();

        assertThat(button.isEnabled(), is(false));
        assertThat(button.isVisible(), is(true));
    }

    @Test
    public void testCreateUiUpdater_WriteableDisabled() {
        BindReadOnlyBehaviorAspectDefinition aspectDefinition = new BindReadOnlyBehaviorAspectDefinition(
                ReadOnlyBehaviorType.DISABLED);
        setDispatcherWritable();
        aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper).apply();

        assertThat(button.isEnabled(), is(true));
        assertThat(button.isVisible(), is(true));
    }

    @Test
    public void testCreateUiUpdater_ReadOnlyInvisible() {
        BindReadOnlyBehaviorAspectDefinition aspectDefinition = new BindReadOnlyBehaviorAspectDefinition(
                ReadOnlyBehaviorType.INVSIBLE);
        setDispatcherReadOnly();
        aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper).apply();

        assertThat(button.isEnabled(), is(true));
        assertThat(button.isVisible(), is(false));
    }

    @Test
    public void testCreateUiUpdater_WriteableInvisible() {
        BindReadOnlyBehaviorAspectDefinition aspectDefinition = new BindReadOnlyBehaviorAspectDefinition(
                ReadOnlyBehaviorType.INVSIBLE);
        setDispatcherWritable();
        aspectDefinition.createUiUpdater(propertyDispatcher, componentWrapper).apply();

        assertThat(button.isEnabled(), is(true));
        assertThat(button.isVisible(), is(true));
    }

    private void setDispatcherReadOnly() {
        Mockito.when(propertyDispatcher.isPushable(Mockito.any())).thenReturn(false);
    }

    private void setDispatcherWritable() {
        Mockito.when(propertyDispatcher.isPushable(Mockito.any())).thenReturn(true);
    }
}