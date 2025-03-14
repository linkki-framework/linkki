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

import java.io.Serial;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.nls.NlsText;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
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
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

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

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed
     */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the UI element
     */
    String label() default "";

    /**
     * Specifies the source of the available values, for each of which a button will be displayed.
     *
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_EXCL_NULL;

    /**
     * Defines whether an UI-Component is editable, using values of {@link EnabledType}
     */
    EnabledType enabled() default ENABLED;

    /**
     * Marks mandatory fields visually
     */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert {@link #content()} into
     * String captions.
     * <p>
     * Default value assumes that the value class has a method "getName" and uses this method for
     * the String representation.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default RadioButtonsCaptionProvider.class;

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
     * Specifies the alignment of the radio buttons. The alignment can be either
     * {@link AlignmentType#VERTICAL}, which is the default alignment, or
     * {@link AlignmentType#HORIZONTAL}.
     */
    AlignmentType buttonAlignment() default AlignmentType.VERTICAL;

    /**
     * Aspect definition creator for the {@link UIRadioButtons} annotation.
     */
    class RadioButtonsAspectDefinitionCreator implements AspectDefinitionCreator<UIRadioButtons> {

        @Override
        public LinkkiAspectDefinition create(UIRadioButtons annotation) {

            var converter = new OptionalToValueConverter();
            var availableValuesAspectDefinition =
                    new AvailableValuesAspectDefinition<RadioButtonGroup<Optional<Object>>>(
                            annotation.content(),
                            (component, values) -> component
                                    .setItems(values.stream()
                                            .map(v -> converter.convertToPresentation(v,
                                                                                      new ValueContext(new Binder<>())))
                                            .toList()));

            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    availableValuesAspectDefinition,
                    new ValueAspectDefinition(converter),
                    new VisibleAspectDefinition(annotation.visible()),
                    new DerivedReadOnlyAspectDefinition());
        }
    }

    /**
     * This converter addresses an issue with Vaadin's {@link RadioButtonGroup} where setting a null value
     * deselects all buttons, see {@link RadioButtonGroup#setValue}.
     * By converting the value to {@link Optional}, it ensures that null values are properly handled
     * in the UI, maintaining the selection state of radio buttons.
     */
    class OptionalToValueConverter implements Converter<Optional<Object>, Object> {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * @param value The value to convert. Can be null if the value set by the model is not a
         *              available value.
         */
        @Override
        public Result<Object> convertToModel(@CheckForNull Optional<Object> value, ValueContext context) {
            return Result.ok(value == null ? null : value.orElse(null));
        }

        @Override
        public Optional<Object> convertToPresentation(@CheckForNull Object value, ValueContext context) {
            return Optional.ofNullable(value);
        }
    }

    /**
     * Component definition for the {@link UIRadioButtons} annotation.
     */
    class RadioButtonsComponentDefinitionCreator implements ComponentDefinitionCreator<UIRadioButtons> {

        @Override
        public LinkkiComponentDefinition create(UIRadioButtons annotation, AnnotatedElement annotatedElement) {
            return pmo -> ComponentFactory.newRadioButtonGroup(annotation::itemCaptionProvider,
                    annotation.buttonAlignment());
        }
    }

    /**
     * Caption provider that displays <code>null</code> with user-friendly text.
     */
    class RadioButtonsCaptionProvider extends ItemCaptionProvider.DefaultCaptionProvider {
        @Override
        public String getNullCaption() {
            return NlsText.getString("RadioButtonsCaptionProvider.Null"); // $NON-NLS-1$
        }
    }
}
