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
package org.linkki.core.ui.element.annotation;

import static org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition.DERIVED_BY_LINKKI;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;

import com.vaadin.flow.component.HasSize;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.PrimitiveAwareValueAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.converters.FormattedStringToLongConverter;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.ui.element.annotation.UILongField.LongFieldAspectCreator;
import org.linkki.core.ui.element.annotation.UILongField.LongFieldComponentDefinitionCreator;
import org.linkki.core.vaadin.component.ComponentFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.text.DecimalFormat;

/**
 * A text field for displaying formatted Long.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiPositioned
@LinkkiBoundProperty
@LinkkiComponent(LongFieldComponentDefinitionCreator.class)
@LinkkiAspect(LongFieldAspectCreator.class)
public @interface UILongField {

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed
     * */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the UI element
     * */
    String label() default DERIVED_BY_LINKKI;

    /**
     * Defines if an UI-Component is editable, using values of {@link EnabledType}
     * */
    EnabledType enabled() default ENABLED;

    /**
     * Marks mandatory fields visually
     * */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Defines the maximal count of characters which can be displayed
     * */
    int maxLength() default 0;

    /**
     * Specifies the width of the field using a number and a CSS unit, for example "5em" or "50%".
     * <p>
     * This value is set to empty String by default which means it is undefined and the actual width
     * depends on the layout.
     *
     * @see HasSize#setWidth(String)
     */
    String width() default "";

    /**
     * Format for the UI representation of the value. See {@link DecimalFormat} for the
     * documentation of the pattern.
     */
    String format() default "";

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    @LinkkiBoundProperty.ModelObjectProperty
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default "";

    /**
     * Aspect definition creator for the {@link UILongField} annotation.
     */
    static class LongFieldAspectCreator implements AspectDefinitionCreator<UILongField> {

        @Override
        public LinkkiAspectDefinition create(UILongField annotation) {
            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition
            );

            return new CompositeAspectDefinition(
                    new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    new PrimitiveAwareValueAspectDefinition(
                            new FormattedStringToLongConverter(annotation.format())
                    ),
                    new DerivedReadOnlyAspectDefinition()
            );
        }
    }

    static class LongFieldComponentDefinitionCreator implements ComponentDefinitionCreator<UILongField> {

        @Override
        public LinkkiComponentDefinition create(UILongField annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newNumberFieldWithFormattingPattern(
                    annotation.maxLength(),
                    annotation.width(),
                    annotation.format()
            );
        }
    }

}
