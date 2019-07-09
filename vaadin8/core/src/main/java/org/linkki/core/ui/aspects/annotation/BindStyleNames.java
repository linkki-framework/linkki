/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.aspects.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.aspects.BindStyleNamesAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindStyleNames.BindStyleNamesAspectDefinitionCreator;

import com.vaadin.ui.Component;

/**
 * This aspect sets a user defined style name using {@link Component#setStyleName(String)}. This will
 * overwrite any other user defined style names but not those from Vaadin.
 * 
 * @see Component#setStyleName(String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindStyleNamesAspectDefinitionCreator.class)
public @interface BindStyleNames {
    /**
     * The style names that may be used in CSS as style classes. Multiple style names could be provided
     * like <code>@BindStyleNames({ STYLE_NAME_1, STYLE_NAME_2 })</code>
     * <p>
     * If the value is an empty array (which is the default) the style names should be retrieved
     * dynamically. That means the style names are retrieved from the method
     * {@code get<PropertyName>StyleNames} which may return a {@link String} or {@link Collection} of
     * {@link String}.
     * 
     * @see Component#setStyleName(String)
     */
    String[] value() default {};

    class BindStyleNamesAspectDefinitionCreator implements AspectDefinitionCreator<BindStyleNames> {

        @Override
        public LinkkiAspectDefinition create(BindStyleNames annotation) {
            return new BindStyleNamesAspectDefinition(annotation.value());
        }
    }
}

