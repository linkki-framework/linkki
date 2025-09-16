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

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.ui.aspects.HelperTextAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindHelperText.BindHelperTextAspectDefinitionCreator;
import org.linkki.core.ui.aspects.types.HelperTextType;

/**
 * Annotation to bind a helper text to a UI element.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindHelperTextAspectDefinitionCreator.class)
public @interface BindHelperText {

    /**
     * The helper text that is displayed on the UI element. The value is ignored if
     * {@link #helperTextType()} is {@link HelperTextType#DYNAMIC}.
     */
    String value() default "";

    /**
     * Defines how the helper text is determined.
     * <ul>
     * <li>{@link HelperTextType#STATIC} – Always uses the value given in {@link #value()}.</li>
     * <li>{@link HelperTextType#DYNAMIC} – Ignores {@link #value()} and determines the helper text
     * dynamically via the method {@code get<PropertyName>HelperText()}.</li>
     * <li>{@link HelperTextType#AUTO} – Uses {@link #value()} if it is not empty, otherwise behaves
     * like {@link HelperTextType#DYNAMIC}.</li>
     * </ul>
     */
    HelperTextType helperTextType() default HelperTextType.AUTO;

    class BindHelperTextAspectDefinitionCreator implements AspectDefinitionCreator<BindHelperText> {
        @Override
        public LinkkiAspectDefinition create(BindHelperText annotation) {
            return new HelperTextAspectDefinition(annotation.value(), annotation.helperTextType());
        }
    }
}
