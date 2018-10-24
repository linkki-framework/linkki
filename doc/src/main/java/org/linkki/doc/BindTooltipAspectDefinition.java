package org.linkki.doc;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.BindTooltipType;
import org.linkki.util.handler.Handler;

//tag::BindTooltipAspectDefinition[]
public class BindTooltipAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "Tooltip";

    private BindTooltip annotation;

    @Override
    public void initialize(Annotation toolTipAnnotation) {
        this.annotation = (BindTooltip)toolTipAnnotation;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<String> setter = componentWrapper::setTooltip;
        Aspect<String> aspect = createAspect();
        return () -> setter.accept(propertyDispatcher.pull(aspect));
    }

    public Aspect<String> createAspect() {
        if (annotation.tooltipType() == BindTooltipType.STATIC) {
            return Aspect.of(NAME, annotation.value());
        } else {
            return Aspect.of(NAME);
        }
    }

}
// end::BindTooltipAspectDefinition[]