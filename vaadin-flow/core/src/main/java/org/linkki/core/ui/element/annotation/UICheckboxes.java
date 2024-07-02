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
import org.linkki.core.ui.aspects.DerivedReadOnlyAspectDefinition;
import org.linkki.core.ui.aspects.GenericAvailableValuesAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UICheckboxes.CheckboxesAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UICheckboxes.CheckboxesComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.data.renderer.TextRenderer;

/**
 * Defines a group of checkboxes for related binary choices using
 * {@link com.vaadin.flow.component.checkbox.CheckboxGroup}. The getter and setter method must be of
 * type {@code Set<VALUE_TYPE>}.
 */
@Retention(RUNTIME)
@Target(METHOD)
@LinkkiPositioned
@LinkkiAspect(CheckboxesAspectDefinitionCreator.class)
@LinkkiComponent(CheckboxesComponentDefinitionCreator.class)
@LinkkiBoundProperty
public @interface UICheckboxes {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default "";

    /** Defines whether a UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert available values into
     * String captions.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;

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
     * Specifies the alignment of the checkboxes. The alignment can be either
     * {@link AlignmentType#VERTICAL}, which is the default alignment, or
     * {@link AlignmentType#HORIZONTAL}.
     */
    AlignmentType checkboxesAlignment() default AlignmentType.VERTICAL;

    /**
     * Creates the aspect definition for the {@link UICheckboxes} annotation. This class defines all
     * necessary aspects for a checkbox group, such as label, enabled state, required state,
     * read-only state, visibility, values, and available values.
     */
    class CheckboxesAspectDefinitionCreator implements AspectDefinitionCreator<UICheckboxes> {

        @Override
        public LinkkiAspectDefinition create(UICheckboxes annotation) {
            var enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            var requiredAspectDefinition = new RequiredAspectDefinition(annotation.required(), enabledAspectDefinition);
            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    new GenericAvailableValuesAspectDefinition(AvailableValuesType.DYNAMIC),
                    new ValueAspectDefinition(),
                    new DerivedReadOnlyAspectDefinition(),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    new VisibleAspectDefinition(annotation.visible()));
        }
    }

    /**
     * Component definition for the {@link UICheckboxes} annotation. This class creates a
     * {@link com.vaadin.flow.component.checkbox.CheckboxGroup} with a
     * {@link com.vaadin.flow.data.renderer.TextRenderer}.
     */
    class CheckboxesComponentDefinitionCreator implements ComponentDefinitionCreator<UICheckboxes> {

        @Override
        public LinkkiComponentDefinition create(UICheckboxes annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                CheckboxGroup<?> checkboxes = new CheckboxGroup<>();
                checkboxes.setRenderer(new TextRenderer<>(
                        ItemCaptionProvider.instantiate(annotation::itemCaptionProvider)::getUnsafeCaption));
                AlignmentType alignmentType = annotation.checkboxesAlignment();
                if (alignmentType.equals(AlignmentType.VERTICAL)) {
                    checkboxes.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
                }
                return checkboxes;
            };
        }
    }
}
