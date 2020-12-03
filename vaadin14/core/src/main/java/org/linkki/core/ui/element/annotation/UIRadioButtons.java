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
import org.linkki.core.ui.element.annotation.UIRadioButtons.UIRadioButtonsAspectDefinitionCreator;
import org.linkki.core.ui.element.annotation.UIRadioButtons.UIRadioButtonsBoundPropertyCreator;
import org.linkki.core.ui.element.annotation.UIRadioButtons.UIRadioButtonsComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.TextRenderer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Radio buttons for selecting a single value. Creates a
 * {@link com.vaadin.flow.component.radiobutton.RadioButtonGroup}.
 */
@Retention(RUNTIME)
@Target(METHOD)
@LinkkiPositioned
@LinkkiAspect(UIRadioButtonsAspectDefinitionCreator.class)
@LinkkiAspect(ValueAspectDefinitionCreator.class)
@LinkkiBoundProperty(UIRadioButtonsBoundPropertyCreator.class)
@LinkkiComponent(UIRadioButtonsComponentDefinitionCreator.class)
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
     * Specifies whether a component is shown, using values of {@link VisibleType}
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
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    /**
     * Specifies the alignment of the radio buttons. The alignment can be either
     * {@link AlignmentType#VERTICAL}, which is the default alignment, or
     * {@link AlignmentType#HORIZONTAL}.
     */
    AlignmentType buttonAlignment() default AlignmentType.VERTICAL;

    /**
     * Component definition for the {@link UIRadioButtons} annotation.
     */
    public static class UIRadioButtonsComponentDefinitionCreator
            implements ComponentDefinitionCreator<UIRadioButtons> {

        @Override
        public LinkkiComponentDefinition create(UIRadioButtons annotation, AnnotatedElement annotatedElement) {
            return new UIRadioButtonsDefinition(getItemCaptionProvider(annotation),
                    annotation.buttonAlignment());
        }

        private ItemCaptionProvider<?> getItemCaptionProvider(UIRadioButtons uiRadioButtons) {
            try {
                return uiRadioButtons.itemCaptionProvider().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new LinkkiBindingException(
                        "Cannot instantiate item caption provider " + uiRadioButtons.itemCaptionProvider().getName()
                                + " using default constructor.",
                        e);
            }
        }
    }

    /**
     * Component definition creator for the {@link UIRadioButtons} annotation.
     */
    static class UIRadioButtonsDefinition implements LinkkiComponentDefinition {

        private ItemCaptionProvider<?> itemCaptionProvider;
        // TODO LIN-2048
        @SuppressFBWarnings("URF_UNREAD_FIELD")
        private AlignmentType alignment;

        public UIRadioButtonsDefinition(ItemCaptionProvider<?> itemCaptionProvider,
                AlignmentType alignment) {
            this.itemCaptionProvider = itemCaptionProvider;
            this.alignment = alignment;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Object createComponent(Object pmo) {
            RadioButtonGroup<?> radioButtons = new RadioButtonGroup<>();
            radioButtons.setRenderer(new TextRenderer(itemCaptionProvider::getUnsafeCaption));

            // TODO LIN-2048
            // if (alignment.equals(AlignmentType.HORIZONTAL)) {
            // radioButtons.addClassName(ValoTheme.OPTIONGROUP_HORIZONTAL);
            // }
            return radioButtons;
        }
    }

    static class UIRadioButtonsBoundPropertyCreator
            implements BoundPropertyCreator<UIRadioButtons> {

        @Override
        public BoundProperty createBoundProperty(UIRadioButtons annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement)
                    .withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }
    }

    /**
     * Aspect definition creator for the {@link UIRadioButtons} annotation.
     */
    static class UIRadioButtonsAspectDefinitionCreator implements AspectDefinitionCreator<UIRadioButtons> {

        @Override
        public LinkkiAspectDefinition create(UIRadioButtons annotation) {

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
}
