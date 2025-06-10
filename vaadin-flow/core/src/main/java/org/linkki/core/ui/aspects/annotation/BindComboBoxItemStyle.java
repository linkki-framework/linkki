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
import org.linkki.core.ui.aspects.BindComboBoxItemStyleAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindComboBoxItemStyle.BindComboBoxItemStyleAspectDefinitionCreator;

/**
 * This aspect can be used with {@link org.linkki.core.ui.element.annotation.UIComboBox} and
 * {@link org.linkki.core.ui.element.annotation.UIMultiSelect} to set a style class name for every
 * item in a combo box popup menu. The name is either statically defined in the annotation or
 * dynamically queried via the method {@code get<propertyName>ItemStyle}}.
 * <p>
 * In case of a dynamic style via method invocation the return type of the method is not a single
 * string but a {@code Function<?, String>}. The method is called only once for creating a new combo
 * box but the function is called for every item in the combo box to allow different styles for
 * every single item.
 * <p>
 * The style classes are applied only to elements in the popup menu, the selected value in the combo
 * box is never styled, only the text.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiAspect(BindComboBoxItemStyleAspectDefinitionCreator.class)
public @interface BindComboBoxItemStyle {

    /**
     * The style names that may be used as CSS style classes for the combo box popup menu items.
     * Multiple style names could be provided like
     * <code>@BindComboBoxItemStyle({ STYLE_NAME_1, STYLE_NAME_2 })</code>
     * <p>
     * If the value is an empty array (which is the default) the style names are retrieved
     * dynamically. That means the style names are retrieved from the method
     * {@code get<PropertyName>ItemStyle()} which must return a {@code Function<TYPE, String>} where
     * TYPE is the type of the combo box items. The function is called for every item in the combo
     * box pop menu.
     */
    String[] value() default {};

    /**
     * Internal aspect definition creator for {@link BindComboBoxItemStyle} to create the
     * {@link BindComboBoxItemStyleAspectDefinition}.
     */
    class BindComboBoxItemStyleAspectDefinitionCreator implements AspectDefinitionCreator<BindComboBoxItemStyle> {

        @Override
        public LinkkiAspectDefinition create(BindComboBoxItemStyle annotation) {
            return new BindComboBoxItemStyleAspectDefinition(annotation.value());
        }

    }

}
