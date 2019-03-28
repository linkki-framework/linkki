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
package org.linkki.core.ui.element.annotation;

import static org.linkki.core.defaults.ui.element.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.element.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.element.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.DecimalFormat;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition.BindingDefinitionBoundPropertyCreator;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.converters.FormattedDoubleToStringConverter;
import org.linkki.core.ui.element.annotation.UIDoubleField.DoubleValueAspectCreator;
import org.linkki.core.ui.element.aspects.ValueAspectDefinition;
import org.linkki.core.ui.element.bindingdefinitions.DoubleFieldBindingDefinition;
import org.linkki.core.uicreation.BindingDefinitionComponentDefinition;

import com.vaadin.server.Sizeable;

/**
 * A text field for displaying formatted numbers.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(DoubleFieldBindingDefinition.class)
@LinkkiBoundProperty(BindingDefinitionBoundPropertyCreator.class)
@LinkkiComponent(BindingDefinitionComponentDefinition.Creator.class)
@LinkkiAspect(FieldAspectDefinitionCreator.class)
@LinkkiAspect(DoubleValueAspectCreator.class)
public @interface UIDoubleField {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    int position();

    /** Provides a description label next to the UI element */
    String label();

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /** Defines the maximal count of characters that can be displayed */
    int maxLength() default 0;

    /**
     * Specifies the width of the field using a number and a CSS unit, for example "5em" or "50%".
     * <p>
     * This value is set to empty String by default which means it is undefined and the actual width
     * depends on the layout.
     * 
     * @see Sizeable#setWidth(String)
     */
    String width() default "";

    /**
     * Format for the UI representation of the value. See {@link DecimalFormat} for the documentation of
     * the pattern.
     */
    String format() default "#,##0.00##";

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    class DoubleValueAspectCreator implements AspectDefinitionCreator<UIDoubleField> {


        @Override
        public LinkkiAspectDefinition create(UIDoubleField annotation) {
            FormattedDoubleToStringConverter converter = new FormattedDoubleToStringConverter(annotation.format());
            return new ValueAspectDefinition(converter);
        }

    }

}
