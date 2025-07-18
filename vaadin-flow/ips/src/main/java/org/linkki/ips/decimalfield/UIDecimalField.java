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
package org.linkki.ips.decimalfield;

import static org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition.DERIVED_BY_LINKKI;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.faktorips.values.Decimal;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.ips.decimalfield.UIDecimalField.DecimalFieldAspectCreator;
import org.linkki.ips.decimalfield.UIDecimalField.DecimalFieldBoundPropertyCreator;
import org.linkki.ips.decimalfield.UIDecimalField.DecimalFieldComponentDefinitionCreator;

import com.vaadin.flow.component.HasSize;

/**
 * A text field for displaying formatted {@link Decimal} values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(DecimalFieldBoundPropertyCreator.class)
@LinkkiComponent(DecimalFieldComponentDefinitionCreator.class)
@LinkkiAspect(DecimalFieldAspectCreator.class)
@LinkkiPositioned
public @interface UIDecimalField {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default DERIVED_BY_LINKKI;

    /** Defines whether the component is editable, using values of {@link EnabledType} */
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
     * @see HasSize#setWidth(String)
     */
    String width() default "";

    /**
     * Format for the UI representation of the value.
     * 
     * @see DecimalFormat for the documentation of the pattern.
     */
    String format() default FormattedStringToDecimalConverter.DEFAULT_FORMAT;

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
     * Aspect definition creator for the {@link UIDecimalField} annotation.
     */
    static class DecimalFieldAspectCreator implements AspectDefinitionCreator<UIDecimalField> {

        @Override
        public LinkkiAspectDefinition create(UIDecimalField annotation) {
            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    new VisibleAspectDefinition(annotation.visible()),
                    new ValueAspectDefinition(new FormattedStringToDecimalConverter(annotation.format())),
                    new DerivedReadOnlyAspectDefinition());
        }

    }

    static class DecimalFieldBoundPropertyCreator implements BoundPropertyCreator<UIDecimalField> {

        @Override
        public BoundProperty createBoundProperty(UIDecimalField annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement)
                    .withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }

    }

    static class DecimalFieldComponentDefinitionCreator implements ComponentDefinitionCreator<UIDecimalField> {

        @Override
        public LinkkiComponentDefinition create(UIDecimalField annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newNumberField(annotation.maxLength(), annotation.width(), "[-+,\\.\\d]");
        }

    }

}
