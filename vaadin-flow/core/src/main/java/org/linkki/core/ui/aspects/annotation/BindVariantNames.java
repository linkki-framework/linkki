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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.aspects.BindVariantNamesAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindVariantNames.BindVariantNamesAspectDefinitionCreator;

import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.grid.GridVariant;

/**
 * This aspect sets one or multiple Vaadin defined variant names using
 * {@link HasTheme#addThemeName(String)}.
 */
@Retention(RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@LinkkiAspect(BindVariantNamesAspectDefinitionCreator.class)
public @interface BindVariantNames {

    /**
     * The variant names are defined across different variant enums, such as {@link GridVariant}.
     * Multiple variant names can be provided as an array:
     * <code>@BindVariantNames({ VARIANT_NAME_1, VARIANT_NAME_2 })</code>
     */
    String[] value() default {};

    class BindVariantNamesAspectDefinitionCreator implements AspectDefinitionCreator<BindVariantNames> {

        @Override
        public LinkkiAspectDefinition create(BindVariantNames annotation) {
            return new BindVariantNamesAspectDefinition(annotation.value());
        }
    }
}
