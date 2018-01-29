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

import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.aspect.definition.CaptionAspectDefinition;
import org.linkki.core.ui.section.annotations.UICheckBox.CheckBoxCaptionAspect;
import org.linkki.core.ui.section.annotations.adapters.CheckboxBindingDefinition;

/**
 * In accordance to {@link com.vaadin.ui.CheckBox}, bound to a boolean property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(CheckboxBindingDefinition.class)
@LinkkiAspect(CheckBoxCaptionAspect.class)
public @interface UICheckBox {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed. */
    int position();

    /**
     * Provides a label on the left side of the check box.
     * <p>
     * Will not be displayed unless {@link #noLabel()} is {@code false}
     */
    String label() default "";

    /**
     * Provides a caption on the right side of the check box.
     * <p>
     * Use an empty String as caption if no caption is needed.
     * <p>
     * For a label on the left, set {@link #noLabel()} to {@code false} and use {@link #label()} for
     * the label text.
     * <p>
     * If the check box is used inside a {@link UITableColumn}, the {@link #label()} will be
     * displayed in the column header while the {@link #caption()} will be displayed inside the
     * table cell together with the check box.
     */
    String caption();

    boolean noLabel() default true;

    /** Defines if an UI-Component is editable, using values of {@link EnabledType}. */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}.
     */
    VisibleType visible() default VISIBLE;

    /**
     * The name of the model object that is to be bound if multiple model objects are included for
     * model binding.
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding.
     */
    String modelAttribute() default "";

    class CheckBoxCaptionAspect extends CaptionAspectDefinition {

        @SuppressWarnings("null")
        private UICheckBox checkBoxAnnotation;

        @Override
        public void initialize(Annotation annotation) {
            this.checkBoxAnnotation = (UICheckBox)annotation;
        }

        @Override
        protected String getStaticCaption() {
            return checkBoxAnnotation.caption();
        }

        @Override
        public CaptionType getCaptionType() {
            return CaptionType.STATIC;
        }

    }
}
