package org.linkki.doc;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.linkki.util.handler.Handler;

//tag::ToolTipAspectDefinition[]
public class ToolTipAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = "toolTip";

    private UIToolTip annotation;

    @Override
    public void initialize(Annotation toolTipAnnotation) {
        this.annotation = (UIToolTip)toolTipAnnotation;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<String> setter = componentWrapper::setTooltip;
        Aspect<String> aspect = createAspect();
        return () -> setter.accept(propertyDispatcher.pull(aspect));
    }

    public Aspect<String> createAspect() {
        if (annotation.toolTipType() == ToolTipType.STATIC) {
            return Aspect.of(NAME, annotation.text());
        } else {
            return Aspect.of(NAME);
        }
    }

}
// end::ToolTipAspectDefinition[]