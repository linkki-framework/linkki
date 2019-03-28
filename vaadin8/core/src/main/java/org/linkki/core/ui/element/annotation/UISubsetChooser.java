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

import static org.linkki.core.defaults.ui.element.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.element.aspects.types.RequiredType.NOT_REQUIRED;
import static org.linkki.core.defaults.ui.element.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Set;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition.BindingDefinitionBoundPropertyCreator;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.defaults.ui.element.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UISubsetChooser.SubsetChooserAvailableValuesAspectCreator;
import org.linkki.core.ui.element.aspects.AvailableValuesAspectDefinition;
import org.linkki.core.ui.element.bindingdefinitions.SubsetChooserBindingDefinition;
import org.linkki.core.uicreation.BindingDefinitionComponentDefinition;

import com.vaadin.ui.TwinColSelect;

/**
 * Creates a subset chooser, i.e. a multi-select component with a left and a right list.
 * 
 * Note that the value handled by a subset chooser must be a {@link Set} whereas the list of available
 * values can be any kind of {@link Collection}. When using this annotation you will presumably need
 * something like this:
 * 
 * <pre>
 * <code>
&#64;UISubsetChooser(...)
public Set&lt;T&gt; getFoo() { ... }

public void setFoo(Set&lt;T&gt; selectedFoos) { ... }

public Set&lt;T&gt; getFooAvailableValues() { ... }
 * </code>
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(SubsetChooserBindingDefinition.class)
@LinkkiBoundProperty(BindingDefinitionBoundPropertyCreator.class)
@LinkkiComponent(BindingDefinitionComponentDefinition.Creator.class)
@LinkkiAspect(SubsetChooserAvailableValuesAspectCreator.class)
@LinkkiAspect(FieldAspectDefinitionCreator.class)
@LinkkiAspect(ValueAspectDefinitionCreator.class)
public @interface UISubsetChooser {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    int position();

    /** Provides a description label next to the UI element */
    String label();

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

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
     * Specifies which {@link ItemCaptionProvider} should be used to convert available values into
     * String captions. Uses the method "toString" per default.
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default ToStringCaptionProvider.class;

    /**
     * Specifies the width of the field. Use CSS units like em, px or %.
     * <p>
     * For example: "25em" or "100%".
     */
    String width() default "100%";

    /** Caption of the left column */
    String leftColumnCaption() default "";

    /** Caption of the right column */
    String rightColumnCaption() default "";

    class SubsetChooserAvailableValuesAspectCreator implements AspectDefinitionCreator<UISubsetChooser> {

        @Override
        public LinkkiAspectDefinition create(UISubsetChooser annotation) {
            return new AvailableValuesAspectDefinition<>(AvailableValuesType.DYNAMIC,
                    TwinColSelect<Object>::setDataProvider);
        }
    }
}
