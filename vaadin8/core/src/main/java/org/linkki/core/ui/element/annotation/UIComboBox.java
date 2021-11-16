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
import java.lang.reflect.AnnotatedElement;
import java.util.List;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
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
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UIComboBox.ComboBoxAspectCreator;
import org.linkki.core.ui.element.annotation.UIComboBox.ComboBoxComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;

/**
 * Creates a ComboBox with the specified parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty
@LinkkiComponent(ComboBoxComponentDefinitionCreator.class)
@LinkkiAspect(ComboBoxAspectCreator.class)
@LinkkiPositioned
public @interface UIComboBox {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default DERIVED_BY_LINKKI;

    /**
     * Specifies the source of the available values, the content of the combo box.
     * 
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_INCL_NULL;

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
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
    @LinkkiBoundProperty.ModelObject
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    @LinkkiBoundProperty.ModelAttribute
    String modelAttribute() default "";

    /**
     * Specifies which {@link ItemCaptionProvider} should be used to convert {@link #content()} into
     * String captions.
     * <p>
     * Default value assumes that the value class has a method "getName" and uses this method for the
     * String representation.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;

    /**
     * Aspect definition creator for the {@link UIComboBox} annotation.
     */
    static class ComboBoxAspectCreator implements AspectDefinitionCreator<UIComboBox> {

        @Override
        public LinkkiAspectDefinition create(UIComboBox annotation) {
            AvailableValuesAspectDefinition<ComboBox<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<ComboBox<Object>>(
                    annotation.content(), this::setDataProvider) {

                @Override
                @SuppressWarnings("unchecked")
                protected void handleNullItems(ComponentWrapper componentWrapper, List<?> items) {
                    boolean dynamicItemsEmpty = annotation.content() == AvailableValuesType.DYNAMIC && items.isEmpty();
                    boolean hasNullItem = items.removeIf(i -> i == null);
                    ((ComboBox<Object>)componentWrapper.getComponent())
                            .setEmptySelectionAllowed(hasNullItem || dynamicItemsEmpty);
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
                    new ValueAspectDefinition(),
                    new DerivedReadOnlyAspectDefinition());
        }

        private void setDataProvider(ComboBox<Object> comboBox, ListDataProvider<Object> dataProvider) {
            int size = dataProvider.getItems().size();
            // ComboBox with more than 500 values are not allowed without paging due to DoS prevention
            if (size > 500) {
                comboBox.setPageLength(15);
            }
            comboBox.setDataProvider(dataProvider);
        }

    }

    static class ComboBoxComponentDefinitionCreator implements ComponentDefinitionCreator<UIComboBox> {

        @Override
        public LinkkiComponentDefinition create(UIComboBox annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                ComboBox<?> comboBox = ComponentFactory.newComboBox();
                comboBox.setItemCaptionGenerator(getItemCaptionProvider(annotation)::getUnsafeCaption);
                comboBox.setEmptySelectionCaption(getItemCaptionProvider(annotation).getNullCaption());
                comboBox.setWidth(annotation.width());
                comboBox.setPopupWidth(null);
                comboBox.setPageLength(0);
                return comboBox;
            };
        }

        private ItemCaptionProvider<?> getItemCaptionProvider(UIComboBox uiComboBox) {
            return ItemCaptionProvider.instantiate(uiComboBox.itemCaptionProvider());
        }
    }
}
