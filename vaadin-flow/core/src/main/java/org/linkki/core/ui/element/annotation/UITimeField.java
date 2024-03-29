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
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
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

import com.vaadin.flow.component.timepicker.TimePicker;

/**
 * A field for time input, in accordance with {@link TimePicker}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty
@LinkkiComponent(UITimeField.TimeFieldComponentDefinitionCreator.class)
@LinkkiAspect(UITimeField.TimeFieldAspectCreator.class)
@LinkkiPositioned
public @interface UITimeField {

    /**
     * Mandatory attribute, defines display order of UI elements.
     */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the UI element.
     */
    String label() default DERIVED_BY_LINKKI;

    /**
     * Defines if a UI component is editable, using values of {@link EnabledType}.
     */
    EnabledType enabled() default ENABLED;

    /**
     * Marks mandatory fields visually.
     */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}.
     */
    VisibleType visible() default VISIBLE;

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding.
     */
    @LinkkiBoundProperty.ModelObjectProperty
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * Name of a property in the class of the bound {@link ModelObject} to use model binding.
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default StringUtils.EMPTY;

    /**
     * Defines the time interval (in the specified {@link #precision()}) between the items displayed
     * in the time picker overlay. It also specifies the amount by which the time
     * increases/decreases using the Up/Down arrow keys (when the overlays are disabled).
     *
     * @see TimePicker#setStep(Duration)
     */
    long step() default 60L;

    /**
     * The precision for the time values, e.g., minutes or seconds. Defaults to minutes.
     *
     */
    ChronoUnit precision() default ChronoUnit.MINUTES;

    class TimeFieldAspectCreator implements AspectDefinitionCreator<UITimeField> {

        @Override
        public LinkkiAspectDefinition create(UITimeField annotation) {
            var enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            var requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);
            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    new VisibleAspectDefinition(annotation.visible()),
                    new ValueAspectDefinition(),
                    new DerivedReadOnlyAspectDefinition());
        }
    }

    class TimeFieldComponentDefinitionCreator implements ComponentDefinitionCreator<UITimeField> {

        @Override
        public LinkkiComponentDefinition create(UITimeField annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newTimeField(annotation.step(), annotation.precision());
        }
    }
}
