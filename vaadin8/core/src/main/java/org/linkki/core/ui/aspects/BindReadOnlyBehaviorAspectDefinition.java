package org.linkki.core.ui.aspects;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior.ReadOnlyBehaviorType;
import org.linkki.util.handler.Handler;

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
            if (!propertyDispatcher.isPushable(Aspect.of(VALUE_ASPECT_NAME))) {
                setComponentStatus((Component)componentWrapper.getComponent());
            }
        };
    }

    private void setComponentStatus(Component component) {
        switch (value) {
            case DISABLED:
                component.setEnabled(false);
                break;
            case INVSIBLE:
                component.setVisible(false);
                break;
        }
    }
}