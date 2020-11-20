package org.linkki.core.ui.aspects;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.util.handler.Handler;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;

/**
 * Aspect Definition for a {@link Component Component's} behaviour in read-only mode.
 */
public class BindReadOnlyBehaviorAspectDefinition implements LinkkiAspectDefinition {

    private final ReadOnlyBehaviorType value;

    public BindReadOnlyBehaviorAspectDefinition(ReadOnlyBehaviorType value) {
        this.value = value;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        return () -> {
            setComponentStatus((Component)componentWrapper.getComponent(),
                               propertyDispatcher.isPushable(Aspect.of(VALUE_ASPECT_NAME)));
        };
    }

    private void setComponentStatus(Component component, boolean writable) {
        switch (value) {
            case DISABLED:
                component.setEnabled(component.isEnabled() && writable);
                break;
            case INVSIBLE:
                component.setVisible(component.isVisible() && writable);
                break;
            case WRITABLE:
                if (component instanceof HasValue) {
                    HasValue<?> hasValueComponent = (HasValue<?>)component;
                    hasValueComponent.setReadOnly(false);
                }
        }
    }
}