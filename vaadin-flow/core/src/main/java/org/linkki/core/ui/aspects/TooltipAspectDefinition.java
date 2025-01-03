package org.linkki.core.ui.aspects;

import static org.linkki.core.ui.aspects.ToggletipAspectDefinition.extractTooltip;
import static org.linkki.core.ui.aspects.annotation.BindTooltip.DEFAULT_DELAY;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindTooltip;
import org.linkki.util.Consumers;

import com.vaadin.flow.component.shared.Tooltip;

public class TooltipAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "tooltip";

    private final BindTooltip annotation;

    public TooltipAspectDefinition(BindTooltip annotation) {
        this.annotation = annotation;
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        final var tooltip = extractTooltip(componentWrapper);
        if (tooltip == null) {
            return Consumers.nopConsumer();
        }

        return getStringConsumer(tooltip);
    }

    @edu.umd.cs.findbugs.annotations.NonNull
    private Consumer<String> getStringConsumer(Tooltip tooltip) {
        return label -> {
            tooltip//
                    .withText(label)
                    .withPosition(annotation.position());

            setIfPresent(tooltip::setFocusDelay, annotation.focusDelay());
            setIfPresent(tooltip::setHideDelay, annotation.hideDelay());
            setIfPresent(tooltip::setHoverDelay, annotation.hoverDelay());
        };
    }

    private void setIfPresent(IntConsumer consumer, int value) {
        if (value != DEFAULT_DELAY) {
            consumer.accept(value);
        }
    }

    @Override
    public Aspect<String> createAspect() {
        return switch (annotation.tooltipType()) {
            case AUTO -> annotation.value() == null || StringUtils.isEmpty(annotation.value()) ?
                    Aspect.of(NAME) :
                    Aspect.of(NAME, annotation.value());
            case STATIC -> Aspect.of(NAME, annotation.value());
            case DYNAMIC -> Aspect.of(NAME);
        };
    }
}