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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.DefaultCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UIRadioButtons.RadioButtonsAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UIRadioButtons.RadioButtonsComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.TextRenderer;

/**
 * Radio buttons for selecting a single value. Creates a
 * {@link com.vaadin.flow.component.radiobutton.RadioButtonGroup}.
 */
@Retention(RUNTIME)
@Target(METHOD)
@LinkkiPositioned
@LinkkiAspect(RadioButtonsAspectDefinitionCreator.class)
@LinkkiBoundProperty
@LinkkiComponent(RadioButtonsComponentDefinitionCreator.class)
public @interface UIRadioButtons {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default "";

    /**
     * Specifies the source of the available values, for each of which a button will be displayed.
     * 
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_EXCL_NULL;

    /** Defines whether an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert {@link #content()} into
     * String captions.
     * <p>
     * Default value assumes that the value class has a method "getName" and uses this method for the
     * String representation.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    @LinkkiBoundProperty.ModelObject
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default "";

    /**
     * Specifies the alignment of the radio buttons. The alignment can be either
     * {@link AlignmentType#VERTICAL}, which is the default alignment, or
     * {@link AlignmentType#HORIZONTAL}.
     */
    AlignmentType buttonAlignment() default AlignmentType.VERTICAL;

    /**
     * Aspect definition creator for the {@link UIRadioButtons} annotation.
     */
    static class RadioButtonsAspectDefinitionCreator implements AspectDefinitionCreator<UIRadioButtons> {

        @Override
        public LinkkiAspectDefinition create(UIRadioButtons annotation) {

            AvailableValuesAspectDefinition<RadioButtonGroup<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                    annotation.content(), RadioButtonGroup::setItems);

            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    availableValuesAspectDefinition,
                    new ValueAspectDefinition(),
                    new VisibleAspectDefinition(annotation.visible()),
                    new DerivedReadOnlyAspectDefinition());
        }
    }

    /**
     * Component definition for the {@link UIRadioButtons} annotation.
     */
    public static class RadioButtonsComponentDefinitionCreator implements ComponentDefinitionCreator<UIRadioButtons> {

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public LinkkiComponentDefinition create(UIRadioButtons annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                RadioButtonGroup<?> radioButtons = new RadioButtonGroup<>();
                radioButtons.setRenderer(new TextRenderer(
                        ItemCaptionProvider.instantiate(annotation::itemCaptionProvider)::getUnsafeCaption));
                AlignmentType alignment = annotation.buttonAlignment();

                if (alignment.equals(AlignmentType.VERTICAL)) {
                    radioButtons.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
                }
                return radioButtons;
            };
        }
    }
}
