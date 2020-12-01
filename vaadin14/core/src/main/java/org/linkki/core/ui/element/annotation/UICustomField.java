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
import static org.linkki.core.binding.descriptor.aspect.base.ApplicableTypeAspectDefinition.ifComponentTypeIs;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition.BindingDefinitionBoundPropertyCreator;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.HasItemsAvailableValuesAspectDefinition;
import org.linkki.core.ui.element.annotation.UICustomField.CustomFieldAvailableValuesAspectDefinitionCreator;
import org.linkki.core.ui.element.bindingdefinitions.CustomFieldBindingDefinition;
import org.linkki.core.uicreation.BindingDefinitionComponentDefinition;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.data.HasItems;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;

/**
 * {@link UICustomField} can include other, more individual controls. The property
 * {@link UICustomField#uiControl()} selects the control class. If that class inherits {@link HasItems}
 * the values can be included from {@link AvailableValuesType}.
 * <p>
 * {@link UICustomField} only supports controls which define an constructor without parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(CustomFieldBindingDefinition.class)
@LinkkiBoundProperty(BindingDefinitionBoundPropertyCreator.class)
@LinkkiComponent(BindingDefinitionComponentDefinition.Creator.class)
@LinkkiAspect(CustomFieldAvailableValuesAspectDefinitionCreator.class)
@LinkkiAspect(FieldAspectDefinitionCreator.class)
@LinkkiAspect(ValueAspectDefinitionCreator.class)
@LinkkiPositioned
public @interface UICustomField {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI-Element */
    String label() default DERIVED_BY_LINKKI;

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Required() marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /**
     * Specifies the width of the field using a number and a CSS unit, for example "5em" or "50%".
     * <p>
     * This value is set to empty String by default which means it is undefined and the actual width
     * depends on the layout.
     * 
     * @see Sizeable#setWidth(String)
     */
    String width() default "";

    /**
     * Specifies the source of the available values, the content of the custom field if it supports a
     * content. May be a list of selectable items.
     * 
     * @see AvailableValuesType
     */
    AvailableValuesType content() default AvailableValuesType.ENUM_VALUES_INCL_NULL;

    /**
     * The class implementing the custom field. This class has to have a default constructor.
     */
    Class<? extends Component> uiControl();

    /**
     * Aspect definition creator for the {@link UICustomField} annotation.
     */
    static class CustomFieldAvailableValuesAspectDefinitionCreator implements AspectDefinitionCreator<UICustomField> {

        @Override
        public LinkkiAspectDefinition create(UICustomField annotation) {
            return ifComponentTypeIs(HasItems.class, new HasItemsAvailableValuesAspectDefinition(annotation.content()));
        }

    }

}
