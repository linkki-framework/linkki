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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.jspecify.annotations.NonNull;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.vaadin.component.BadgeSeverity;
import org.linkki.util.Consumers;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.dom.ThemeList;

/**
 * Sets the severity of a {@link org.linkki.core.ui.element.annotation.UIBadge} dynamically.
 * <p>
 * The severity is always determined by an aspect method with aspect name
 * {@value BadgeSeverityAspectDefinition#NAME}, e.g. {@code get<PropertyName>BadgeSeverity()} with
 * return type {@link BadgeSeverity}.
 * <p>
 * As the represented severity is not set as an attribute on the DOM element but rather as a theme
 * name, this annotation is intentionally not named {@code BindSeverity}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindBadgeSeverity.BindBadgeSeverityAspectDefinitionCreator.class)
public @interface BindBadgeSeverity {

    class BindBadgeSeverityAspectDefinitionCreator implements AspectDefinitionCreator<BindBadgeSeverity> {

        @Override
        public LinkkiAspectDefinition create(@NonNull BindBadgeSeverity annotation) {
            return new BadgeSeverityAspectDefinition();
        }
    }

    /**
     * Dynamically sets the theme name of a {@link BadgeSeverity} on a component's
     * {@link ThemeList}, replacing any previously set severity theme as severities are mutually
     * exclusive.
     */
    class BadgeSeverityAspectDefinition extends ModelToUiAspectDefinition<BadgeSeverity> {

        public static final String NAME = "badgeSeverity";

        private static final List<String> SEVERITY_THEMES = Arrays.stream(BadgeSeverity.values())
                .map(BadgeSeverity::getVariantName)
                .filter(Objects::nonNull)
                .toList();

        @Override
        public Aspect<BadgeSeverity> createAspect() {
            return Aspect.of(NAME);
        }

        @Override
        protected Consumer<BadgeSeverity> createComponentValueSetter(ComponentWrapper componentWrapper) {
            if (componentWrapper.getComponent() instanceof HasElement component) {
                var themeList = component.getElement().getThemeList();
                return severity -> {
                    var variantName = severity == null ? null : severity.getVariantName();
                    var currentVariantName = SEVERITY_THEMES.stream().filter(themeList::contains).findFirst()
                            .orElse(null);
                    if (!Objects.equals(variantName, currentVariantName)) {
                        SEVERITY_THEMES.forEach(themeList::remove);
                        if (variantName != null) {
                            themeList.add(variantName);
                        }
                    }
                };
            } else {
                return Consumers.nopConsumer();
            }
        }
    }
}
