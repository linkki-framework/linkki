package org.linkki.core.ui.aspects;



import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.PREFIX;
import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.SUFFIX;

import java.util.Collections;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.aspects.annotation.BindToggletip;
import org.linkki.core.ui.wrapper.VaadinComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.shared.HasPrefix;
import com.vaadin.flow.component.shared.HasSuffix;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.shared.Tooltip;

import edu.umd.cs.findbugs.annotations.NonNull;

public class ToggletipAspectDefinition extends ModelToUiAspectDefinition<String> {

    private static final String NAME = "toggletip";
    private final BindToggletip annotation;
    private Tooltip tooltip;
    private Button button;

    public ToggletipAspectDefinition(BindToggletip annotation) {
        this.annotation = annotation;
    }

    public static Tooltip extractTooltip(ComponentWrapper componentWrapper) {
        if (!(componentWrapper.getComponent() instanceof HasTooltip component)) {
            throw new IllegalArgumentException("Component " + componentWrapper.getComponent().getClass().getSimpleName() + //
                    " does not implement HasTooltip");
        }

        return component.getTooltip();
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        return getStringConsumer(componentWrapper);
    }

    private void initializeButton(ComponentWrapper componentWrapper) {
        if (button == null) {
            button = createButton(componentWrapper);
            tooltip = extractTooltip(componentWrapper);

            if (tooltip != null) {
                tooltip//
                        .withPosition(annotation.position())
                        .withManual(true);

                button.addClickListener(event -> tooltip.setOpened(!tooltip.isOpened()));
            }
        }
    }

    private Button createButton(ComponentWrapper componentWrapper) {
        if (componentWrapper instanceof VaadinComponentWrapper) {
            final var component = ((VaadinComponentWrapper)componentWrapper).getComponent();
            if ((component instanceof HasPrefix && annotation.toggletipPosition() == PREFIX) || (component instanceof HasSuffix
                    && annotation.toggletipPosition() == SUFFIX)) {
                final var newButton = ComponentFactory.newButton(annotation.icon()
                        .create(), Collections.emptyList());
                if (annotation.toggletipPosition() == PREFIX) {
                    ((HasPrefix)component).setPrefixComponent(newButton);
                } else if (annotation.toggletipPosition() == SUFFIX) {
                    ((HasSuffix)component).setSuffixComponent(newButton);
                }

                return newButton;
            } else {
                //
                throw new IllegalArgumentException("Component %s does not implement %s or %s".formatted(component.getClass()
                        .getSimpleName(), HasPrefix.class.getSimpleName(), HasSuffix.class.getSimpleName()));
            }

        } else {
            throw new IllegalArgumentException("ComponentWrapper " + componentWrapper.getClass().getSimpleName() + //
                    " is not a VaadinComponentWrapper");
        }
    }

    @NonNull
    private Consumer<String> getStringConsumer(ComponentWrapper componentWrapper) {
        return text -> {
            initializeButton(componentWrapper);
            tooltip.setText(text);
        };
    }

    @Override
    public Aspect<String> createAspect() {
        return switch (annotation.toggletipType()) {
            case AUTO -> annotation.value() == null || StringUtils.isEmpty(annotation.value()) ?
                    Aspect.of(NAME) :
                    Aspect.of(NAME, annotation.value());
            case STATIC -> Aspect.of(NAME, annotation.value());
            case DYNAMIC -> Aspect.of(NAME);
        };
    }

}