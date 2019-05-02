package org.linkki.samples.binding.annotation;
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


/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.linkki.core.binding.LinkkiBindingException;
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
import org.linkki.core.ui.element.annotation.ValueAspectDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.samples.binding.annotation.UIRadioButtonGroup.UIRadioButtonGroupBoundPropertyCreator;
import org.linkki.samples.binding.annotation.UIRadioButtonGroup.UIRadioButtonGroupComponentDefinitionCreator;
import org.linkki.samples.binding.annotation.UIRadioButtonGroup.UIRadioButtonGroupFieldAspectDefinitionCreator;

import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.themes.ValoTheme;

// tag::custom-annotation[]
@Retention(RUNTIME) // <1>
@Target(METHOD) // <1>
@LinkkiPositioned // <2>
@LinkkiAspect(UIRadioButtonGroupFieldAspectDefinitionCreator.class) // <3>
@LinkkiAspect(ValueAspectDefinitionCreator.class) // <3>
@LinkkiBoundProperty(UIRadioButtonGroupBoundPropertyCreator.class) // <4>
@LinkkiComponent(UIRadioButtonGroupComponentDefinitionCreator.class) // <5>
public @interface UIRadioButtonGroup {

    /**
     * Defines the position of the UI element.
     * 
     * @return the defined position
     */
    @LinkkiPositioned.Position // <2>
    int position();

    /**
     * The label that is displayed to describe the {@link RadioButtonGroup}. The default is an empty
     * String.
     * 
     * @return the label
     */
    String label() default ""; // <3>

    /**
     * Specifies the source of the available values, the content of the combo box.
     * 
     * @see AvailableValuesType
     * 
     * @return the type of the content
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_INCL_NULL;

    /**
     * Defines whether the {@link RadioButtonGroup} is enabled, disabled or dynamically enabled. The
     * default value is {@link EnabledType#ENABLED}.
     * 
     * @return the {@link EnabledType}
     */
    EnabledType enabled() default ENABLED;

    /**
     * Defines the {@link RequiredType} of the {@link RadioButtonGroup}. The default value is
     * {@link RequiredType#NOT_REQUIRED}.
     * 
     * @return the specified {@link RequiredType}
     */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Defines the {@link VisibleType} of the {@link RadioButtonGroup}. The default value is
     * {@link VisibleType#VISIBLE}.
     * 
     * @return the specified {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * An {@link ItemCaptionProvider} provides the caption of each radio button. By default the
     * {@link DefaultCaptionProvider} which invokes the {@code getName()} method is used.
     * 
     * @return the specified {@link ItemCaptionProvider}
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;

    /**
     * If provided <b>linkki</b> uses the {@code ModelObject} for model binding.
     * 
     * @return the name of the {@code ModelObject}
     */
    String modelObject() default ModelObject.DEFAULT_NAME; // <4>

    /**
     * If provided <b>linkki</b> accesses the given model attribute for model binding.
     * 
     * @return the name of the {@code ModelAttribute}
     */
    String modelAttribute() default "";

    /**
     * Specifies the alignment of the radio buttons. The alignment can be either
     * {@link AlignmentType#VERTICAL}, which is the default alignment, or
     * {@link AlignmentType#HORIZONTAL}
     * 
     * @return the {@link AlignmentType} that is set
     */
    AlignmentType buttonAlignment() default AlignmentType.VERTICAL;

    // end::custom-annotation[]
    // tag::component-definition[]
    public static class UIRadioButtonGroupComponentDefinitionCreator
            implements ComponentDefinitionCreator<UIRadioButtonGroup> {

        @Override
        public LinkkiComponentDefinition create(UIRadioButtonGroup annotation, AnnotatedElement annotatedElement) {
            return new UIRadioButtonGroupComponentDefinition(getItemCaptionProvider(annotation),
                    annotation.buttonAlignment());
        }

        private ItemCaptionProvider<?> getItemCaptionProvider(UIRadioButtonGroup uiRadioButtonGroup) {
            try {
                return uiRadioButtonGroup.itemCaptionProvider().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new LinkkiBindingException(
                        "Cannot instantiate item caption provider " + uiRadioButtonGroup.itemCaptionProvider().getName()
                                + " using default constructor.",
                        e);
            }
        }
    }

    class UIRadioButtonGroupComponentDefinition implements LinkkiComponentDefinition {

        private ItemCaptionProvider<?> itemCaptionProvider;
        private AlignmentType alignment;

        public UIRadioButtonGroupComponentDefinition(ItemCaptionProvider<?> itemCaptionProvider,
                AlignmentType alignment) {
            this.itemCaptionProvider = itemCaptionProvider;
            this.alignment = alignment;
        }

        @Override
        public Object createComponent(Object pmo) {
            RadioButtonGroup<?> radioButtonGroup = new RadioButtonGroup<>();
            radioButtonGroup.setItemCaptionGenerator(itemCaptionProvider::getUnsafeCaption);
            if (alignment.equals(AlignmentType.HORIZONTAL)) {
                radioButtonGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
            }
            return radioButtonGroup;
        }
    }
    // end::component-definition[]

    // tag::bound-property[]
    public static class UIRadioButtonGroupBoundPropertyCreator
            implements BoundPropertyCreator<UIRadioButtonGroup> {

        @Override
        public BoundProperty createBoundProperty(UIRadioButtonGroup annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement)
                    .withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }
    }
    // end::bound-property[]

    // tag::aspect-definition[]
    public class UIRadioButtonGroupFieldAspectDefinitionCreator implements AspectDefinitionCreator<UIRadioButtonGroup> {

        @Override
        public LinkkiAspectDefinition create(UIRadioButtonGroup annotation) {

            AvailableValuesAspectDefinition<?> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<RadioButtonGroup<Object>>(
                    annotation.content(),
                    RadioButtonGroup<Object>::setDataProvider);

            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    availableValuesAspectDefinition,
                    new VisibleAspectDefinition(annotation.visible()),
                    new DerivedReadOnlyAspectDefinition());
        }
    }
    // end::aspect-definition[]
}
