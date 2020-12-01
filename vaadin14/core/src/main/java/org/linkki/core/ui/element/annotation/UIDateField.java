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

import static org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition.DERIVED_BY_LINKKI;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.time.LocalDate;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition.BindingDefinitionBoundPropertyCreator;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.converters.TwoDigitYearLocalDateConverter;
import org.linkki.core.ui.element.annotation.UIDateField.DateFieldValueAspectCreator;
import org.linkki.core.ui.element.bindingdefinitions.DateFieldBindingDefinition;
import org.linkki.core.uicreation.BindingDefinitionComponentDefinition;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.data.Converter;

/**
 * A field for date input, in accordance with {@link com.vaadin.ui.DateField}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(DateFieldBindingDefinition.class)
@LinkkiBoundProperty(BindingDefinitionBoundPropertyCreator.class)
@LinkkiComponent(BindingDefinitionComponentDefinition.Creator.class)
@LinkkiAspect(FieldAspectDefinitionCreator.class)
@LinkkiAspect(DateFieldValueAspectCreator.class)
@LinkkiPositioned
public @interface UIDateField {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default DERIVED_BY_LINKKI;

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    /**
     * Defines the date format, default format of the UI locale is used if no format is specified.
     * linkki uses {@link DateFormat#SHORT} for interpreting.
     */
    String dateFormat() default "";

    /**
     * Aspect definition creator for the {@link UIDateField} annotation.
     */
    static class DateFieldValueAspectCreator implements AspectDefinitionCreator<UIDateField> {

        @Override
        public LinkkiAspectDefinition create(UIDateField annotation) {

            TwoDigitYearLocalDateConverter twoDigitYearConverter = new TwoDigitYearLocalDateConverter();

            return new ValueAspectDefinition() {
                @Override
                protected Converter<?, ?> getConverter(Type presentationType, Type modelType) {
                    @SuppressWarnings("unchecked")
                    Converter<LocalDate, ?> superConverter = (Converter<LocalDate, ?>)super.getConverter(presentationType,
                                                                                                         modelType);
                    return twoDigitYearConverter.chain(superConverter);
                }
            };
        }
    }
}
