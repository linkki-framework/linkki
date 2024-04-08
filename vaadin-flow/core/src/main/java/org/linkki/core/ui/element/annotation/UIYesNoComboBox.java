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
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.nls.NlsText;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
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
import org.linkki.core.ui.converters.NullHandlingConverterWrapper;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.converter.Converter;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A combo box for boolean or Boolean values.
 *
 * @deprecated as the functionality is supported by {@link UIComboBox}. Use @{@link UIComboBox} instead.
 */
@Deprecated(since = "2.6.0")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty
@LinkkiComponent(org.linkki.core.ui.element.annotation.UIYesNoComboBox.YesNoComboBoxComponentDefinitionCreator.class)
@LinkkiAspect(org.linkki.core.ui.element.annotation.UIYesNoComboBox.YesNoComboBoxAspectCreator.class)
@LinkkiPositioned
public @interface UIYesNoComboBox {

    /**
     * Mandatory attribute that defines the order in which UI-Elements are displayed
     */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the UI element
     */
    String label() default DERIVED_BY_LINKKI;

    /**
     * Defines if a UI-Component is editable, using values of {@link EnabledType}
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
     * Specifies the width of the field. Use CSS units like em, px or %.
     * <p>
     * For example: "25em" or "100%".
     */
    String width() default "-1px";

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
     * Specifies which {@link ItemCaptionProvider} should be used to convert boolean values into
     * String captions.
     * <p>
     * Default value prints the boolean values in the system locale (for example "yes"/"no" in
     * English or "ja"/nein" in German).
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default BooleanCaptionProvider.class;

    /**
     * Aspect definition creator for the {@link UIYesNoComboBox} annotation.
     */
    class YesNoComboBoxAspectCreator implements AspectDefinitionCreator<UIYesNoComboBox> {

        @Override
        public LinkkiAspectDefinition create(UIYesNoComboBox annotation) {
            AvailableValuesAspectDefinition<ComboBox<Object>> availableValuesAspectDefinition =
                    new AvailableValuesAspectDefinition<>(
                            AvailableValuesType.ENUM_VALUES_INCL_NULL, ComboBox::setItems) {

                        @SuppressWarnings("unchecked")
                        @Override
                        protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
                            boolean hasNullItem = items.removeIf(Objects::isNull);
                            ((ComboBox<Object>)componentWrapper.getComponent())
                                    .setClearButtonVisible(hasNullItem);
                        }

                    };

            EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            RequiredAspectDefinition requiredAspectDefinition = new RequiredAspectDefinition(
                    annotation.required(),
                    enabledAspectDefinition);

            return new CompositeAspectDefinition(new LabelAspectDefinition(annotation.label()),
                    enabledAspectDefinition,
                    requiredAspectDefinition,
                    availableValuesAspectDefinition,
                    new VisibleAspectDefinition(annotation.visible()),
                    new YesNoComboBoxValueAspectDefinition(),
                    new DerivedReadOnlyAspectDefinition());
        }

    }

    class YesNoComboBoxValueAspectDefinition extends ValueAspectDefinition {

        @Override
        protected Converter<?, ?> getConverter(Type presentationType, Type modelType) {
            if (modelType instanceof Class<?> && ((Class<?>)modelType).isPrimitive()) {
                return new NullHandlingConverterWrapper<>(super.getConverter(presentationType, modelType));
            } else {
                return super.getConverter(presentationType, modelType);
            }
        }

        /**
         * Do not set any warning message as it should be expected that only valid values can be
         * selected.
         */
        @Override
        protected MessageList getInvalidInputMessage(Object value) {
            return new MessageList();
        }
    }

    class YesNoComboBoxComponentDefinitionCreator implements ComponentDefinitionCreator<UIYesNoComboBox> {

        @Override
        public LinkkiComponentDefinition create(UIYesNoComboBox annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                ComboBox<Object> comboBox = ComponentFactory.newComboBox();
                comboBox.setItemLabelGenerator(ItemCaptionProvider
                        .instantiate(annotation::itemCaptionProvider)::getUnsafeCaption);
                comboBox.setAllowCustomValue(false);
                comboBox.setWidth(annotation.width());
                return comboBox;
            };
        }
    }

    /**
     * @deprecated Use {@link DefaultCaptionProvider} instead.
     */
    @Deprecated(since = "2.6.0")
    class BooleanCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        @NonNull
        public String getCaption(Object o) {
            return (o instanceof Boolean) ? booleanToCaption((Boolean)o) : ""; // $NON-NLS-1$
        }

        private String booleanToCaption(Boolean bool) {
            return bool ? NlsText.getString("DefaultCaptionProvider.True") // $NON-NLS-1$
                    : NlsText.getString("DefaultCaptionProvider.False"); // $NON-NLS-1$
        }
    }
}