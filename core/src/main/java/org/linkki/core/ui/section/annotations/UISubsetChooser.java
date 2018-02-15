/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Set;

import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.components.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.section.annotations.UISubsetChooser.SubsetChooserAvailableValuesAspectDefinition;
import org.linkki.core.ui.section.annotations.adapters.SubsetChooserBindingDefinition;
import org.linkki.core.ui.section.annotations.aspect.AvailableValuesAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.FieldAspectDefinition;

/**
 * Creates a subset chooser, i.e. a multi-select component with a left and a right list.
 * 
 * Note that the value handled by a subset chooser must be a {@link Set} whereas the list of
 * available values can be any kind of {@link Collection}. When using this annotation you will
 * presumably need something like this:
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
@LinkkiAspect(SubsetChooserAvailableValuesAspectDefinition.class)
@LinkkiAspect(FieldAspectDefinition.class)
public @interface UISubsetChooser {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    int position();

    /** Provides a description label next to the UI element */
    String label() default "";

    boolean noLabel() default false;

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

    class SubsetChooserAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition {

        @Override
        public void initialize(Annotation annotation) {
            // does not need to do anything
        }

        @Override
        protected AvailableValuesType getAvailableValuesType() {
            return AvailableValuesType.DYNAMIC;
        }

    }
}
