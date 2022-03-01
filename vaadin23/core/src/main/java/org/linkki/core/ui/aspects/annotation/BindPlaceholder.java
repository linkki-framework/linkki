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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.InheritedAspect;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.aspects.PlaceholderAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder.BindPlaceholderAspectDefinitionCreator;
import org.linkki.core.ui.aspects.types.PlaceholderType;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;


/**
 * BindPlaceholder can add a placeholder to UI elements which have a setPlaceholder method. Common UI
 * elements which support BindPlaceholder are {@link UIIntegerField}, {@link UIDoubleField},
 * {@link UITextField} or {@link UITextArea}. BindPlaceholder can only be inherited when applied on
 * class level.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@InheritedAspect
@LinkkiAspect(BindPlaceholderAspectDefinitionCreator.class)
public @interface BindPlaceholder {

    /** The displayed text for {@link PlaceholderType#STATIC} */
    String value() default StringUtils.EMPTY;

    /** Defines how the placeholder should be retrieved */
    PlaceholderType placeholderType() default PlaceholderType.AUTO;

    class BindPlaceholderAspectDefinitionCreator implements AspectDefinitionCreator<BindPlaceholder> {

        @Override
        public LinkkiAspectDefinition create(BindPlaceholder annotation) {
            return new PlaceholderAspectDefinition(annotation.value(), annotation.placeholderType());
        }
    }
}
