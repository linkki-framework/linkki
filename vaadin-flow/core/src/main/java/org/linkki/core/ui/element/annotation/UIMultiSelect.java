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
import java.util.Collection;
import java.util.Set;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
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
import org.linkki.core.ui.aspects.ValueAspectDefinition;
import org.linkki.core.ui.element.annotation.UIMultiSelect.MultiSelectAspectCreator;
import org.linkki.core.ui.element.annotation.UIMultiSelect.MultiSelectComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;

/**
 * Creates a {@link MultiSelectComboBox} that allows the selection of multiple values with the
 * specified parameters.
 * <p>
 * Setter and getter require a return type of {@link Set Set&lt;VALUE_TYPE&gt;}. Because of that
 * there is no ordering guarantee for the selected values.
 * <p>
 * Unlike the content of a {@link UIComboBox}, the content of a {@link UIMultiSelect} is set to
 * {@link AvailableValuesType#DYNAMIC} and therefore a method
 * {@code get[PropertyName]AvailableValues()} is always required. The type of this method can any
 * kind of {@link Collection}. But keep in mind that some sub-types of {@link Collection} do not
 * provide any ordering guarantees.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty
@LinkkiComponent(MultiSelectComponentDefinitionCreator.class)
@LinkkiAspect(MultiSelectAspectCreator.class)
@LinkkiPositioned
public @interface UIMultiSelect {

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
     * Defines if an UI-Component is editable, using values of {@link EnabledType}
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
     * Specifies which {@link ItemCaptionProvider} should be used to convert the values into String
     * captions.
     * <p>
     * Default value assumes that the value class has a method "getName" and uses this method for
     * the String representation.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default DefaultCaptionProvider.class;

    /**
     * Aspect definition creator for the {@link UIMultiSelect} annotation.
     */
    class MultiSelectAspectCreator implements AspectDefinitionCreator<UIMultiSelect> {

        @Override
        public LinkkiAspectDefinition create(UIMultiSelect annotation) {
            var availableValuesAspectDefinition = new AvailableValuesAspectDefinition<MultiSelectComboBox<Object>>(
                    AvailableValuesType.DYNAMIC, MultiSelectComboBox::setItems);
            var enabledAspectDefinition = new EnabledAspectDefinition(annotation.enabled());
            var requiredAspectDefinition = new RequiredAspectDefinition(
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

    }

    class MultiSelectComponentDefinitionCreator implements ComponentDefinitionCreator<UIMultiSelect> {

        @Override
        public LinkkiComponentDefinition create(UIMultiSelect annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                MultiSelectComboBox<?> multiselect = ComponentFactory.newMultiSelect();
                multiselect.setClearButtonVisible(true);
                multiselect.setItemLabelGenerator(ItemCaptionProvider
                        .instantiate(annotation::itemCaptionProvider)::getUnsafeCaption);
                multiselect.setWidth(annotation.width());
                return multiselect;
            };
        }
    }
}
