/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.core.ui.aspects.annotation;

import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.SUFFIX;
import static org.linkki.core.uicreation.UiCreator.getComponentId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition;
import org.linkki.core.ui.aspects.types.ToggletipType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.ComponentAnnotationReader;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.shared.Tooltip;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Embeds another PMO in the current layout.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiLayout(UINestedToggleTip.NestedToggleTipLayoutDefinitionCreator.class)
@LinkkiComponent(UINestedToggleTip.NestedToggleTipComponentDefinitionCreator.class)
@LinkkiPositioned
@LinkkiAspect(UINestedToggleTip.NestedToggletipAspectDefinitionCreator.class)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
public @interface UINestedToggleTip {

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed
     */
    @LinkkiPositioned.Position int position();

    String value() default StringUtils.EMPTY;

    /**
     * Defines how the tooltip text should be retrieved
     */
    ToggletipType toggletipType() default ToggletipType.AUTO;

    ToogletipPosition toggletipPosition() default SUFFIX;

    Tooltip.TooltipPosition tooltipPosition() default Tooltip.TooltipPosition.TOP;

    VaadinIcon icon() default VaadinIcon.QUESTION_CIRCLE_O;

    String label() default "";

    class NestedToggleTipComponentDefinitionCreator implements ComponentDefinitionCreator<UINestedToggleTip> {
        @Override
        public LinkkiComponentDefinition create(@NonNull UINestedToggleTip annotation,
                @NonNull AnnotatedElement annotatedElement) {
            return pmo -> new HorizontalLayout();
        }
    }

    class NestedToggleTipLayoutDefinitionCreator implements LayoutDefinitionCreator<UINestedToggleTip> {

        @Override
        public LinkkiLayoutDefinition create(@NonNull UINestedToggleTip annotation, @NonNull AnnotatedElement annotatedElement) {
            return (parentComponent, pmo, bindingContext) -> {
                final var wrapper = (HorizontalLayout)parentComponent;
                final var method = (Method)annotatedElement;

                final var componentDefAnnotation = ComponentAnnotationReader.getComponentDefinitionAnnotations(method)
                        .stream()
                        .filter(c -> !c.annotationType()
                                .isAssignableFrom(UINestedToggleTip.class))
                        .reduce((a, b) -> {
                            throw new IllegalStateException("Multiple component definition annotations found: " + a + ", " + b);
                        })
                        .orElseThrow(() -> new IllegalStateException("No component definition annotation found"));

                final Function<Component, NoLabelComponentWrapper> creatorFunction = (Component c) -> new NoLabelComponentWrapper(
                        c, WrapperType.FIELD);

                final var component = (Component)ComponentAnnotationReader.getComponentDefinition(componentDefAnnotation, method)
                        .createComponent(pmo);

                var bindingDescriptor = BindingDescriptor.forMethod(method);
                final var componentWrapper = creatorFunction.apply(component);
                componentWrapper.setId(getComponentId(bindingDescriptor.getBoundProperty(), pmo));

                bindingContext.bind(pmo, bindingDescriptor, componentWrapper);
                if (componentWrapper.getComponent() instanceof HasSize hasSize) {
                    hasSize.setWidth("100%");
                }
                final var button = ComponentFactory.newButton(annotation.icon()
                        .create(), Collections.emptyList());

                switch (annotation.toggletipPosition()) {
                    case PREFIX -> {
                        wrapper.add(button);
                        wrapper.add(componentWrapper.getComponent());
                    }
                    case SUFFIX -> {
                        wrapper.add(componentWrapper.getComponent());
                        wrapper.add(button);
                    }
                }

                bindingContext.updateUi();
            };
        }
    }

    class NestedToggletipAspectDefinitionCreator implements AspectDefinitionCreator<UINestedToggleTip> {

        @Override
        public LinkkiAspectDefinition create(@NonNull UINestedToggleTip annotation) {
            return new CompositeAspectDefinition(new ToggletipAspectDefinition(annotation),
                    new LabelAspectDefinition(annotation.label()));
        }

    }

    class ToggletipAspectDefinition extends ModelToUiAspectDefinition<String> {

        private static final String NAME = "toggletip";
        private final UINestedToggleTip annotation;
        private Tooltip tooltip;
        private Button button;

        public ToggletipAspectDefinition(UINestedToggleTip annotation) {
            this.annotation = annotation;
        }

        public Tooltip extractTooltip(ComponentWrapper componentWrapper) {
            if (componentWrapper.getComponent() instanceof HorizontalLayout layout) {
                final var component = getComponent(layout);
                if (component instanceof HasTooltip hasTooltip) {
                    return hasTooltip.getTooltip();
                } else {
                    throw new IllegalArgumentException("Component " + component.getClass()
                            .getSimpleName() + //
                            " does not implement HasTooltip");
                }
            }
            throw new IllegalArgumentException("Component " + componentWrapper.getComponent()
                    .getClass()
                    .getSimpleName() + //
                    " does not implement HasTooltip");
        }

        private Component getComponent(HorizontalLayout parentLayout) {
            return switch (annotation.toggletipPosition()) {
                case PREFIX -> parentLayout.getComponentAt(1);
                case SUFFIX -> parentLayout.getComponentAt(0);
            };
        }

        private Component getToggletipComponent(HorizontalLayout parentLayout) {
            return switch (annotation.toggletipPosition()) {
                case PREFIX -> parentLayout.getComponentAt(0);
                case SUFFIX -> parentLayout.getComponentAt(1);
            };
        }

        @Override
        public Consumer<String> createComponentValueSetter(@NonNull ComponentWrapper componentWrapper) {
            return text -> {
                initializeTooltip(componentWrapper);
                Optional.ofNullable(tooltip)
                        .ifPresent(t -> t.setText(text));
            };
        }

        private void initializeTooltip(ComponentWrapper componentWrapper) {
            if (componentWrapper.getComponent() instanceof HorizontalLayout layout && layout.getComponentCount() == 0
                    || !(componentWrapper.getComponent() instanceof HorizontalLayout)) {
                return;
            }

            if (tooltip == null && button == null) {
                button = extractToggletipButton(componentWrapper);
                tooltip = extractTooltip(componentWrapper);
                tooltip.withPosition(annotation.tooltipPosition())
                        .withManual(true);

                button.addClickListener(event -> tooltip.setOpened(!tooltip.isOpened()));
            }
        }

        private Button extractToggletipButton(ComponentWrapper componentWrapper) {
            if (componentWrapper.getComponent() instanceof HorizontalLayout layout) {
                final var component = getToggletipComponent(layout);
                if (component instanceof Button buttonComponent) {
                    return buttonComponent;
                } else {
                    throw new IllegalArgumentException("Component " + component.getClass()
                            .getSimpleName() + //
                            " does not implement Button");
                }
            }
            throw new IllegalArgumentException("Component " + componentWrapper.getComponent()
                    .getClass()
                    .getSimpleName() + //
                    " is not the wrapper horizontal layout");
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

}
