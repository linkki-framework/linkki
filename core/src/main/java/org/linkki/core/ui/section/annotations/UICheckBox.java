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
package org.linkki.core.ui.section.annotations;

import static org.linkki.core.ui.section.annotations.EnabledType.ENABLED;
import static org.linkki.core.ui.section.annotations.RequiredType.NOT_REQUIRED;
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.section.annotations.UICheckBox.CheckBoxCaptionAspectCreator;
import org.linkki.core.ui.section.annotations.adapters.CheckboxBindingDefinition;
import org.linkki.core.ui.section.annotations.aspect.FieldAspectDefinitionCreator;
import org.linkki.core.ui.section.annotations.aspect.CaptionAspectDefinition;

/**
 * In accordance to {@link com.vaadin.ui.CheckBox}, bound to a boolean property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(CheckboxBindingDefinition.class)
@LinkkiAspect(CheckBoxCaptionAspectCreator.class)
@LinkkiAspect(FieldAspectDefinitionCreator.class)
public @interface UICheckBox {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed. */
    int position();

    /**
     * Provides a label on the left side of the check box.
     * <p>
     * Normally a check box does not have a label on the left side but a {@link #caption()} which rests
     * on the right side of the box.
     */
    String label() default "";

    /**
     * Provides a caption on the right side of the check box.
     * <p>
     * Use an empty String as caption if no caption is needed.
     * <p>
     * For a label on the left, use {@link #label()} for the label text.
     * <p>
     * If the check box is used inside a {@link UITableColumn}, the {@link #label()} will be displayed
     * in the column header while the {@link #caption()} will be displayed inside the table cell
     * together with the check box.
     */
    String caption();

    /** Defines if an UI-Component is editable, using values of {@link EnabledType}. */
    EnabledType enabled() default ENABLED;

    /** Marks mandatory fields visually */
    RequiredType required() default NOT_REQUIRED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}.
     */
    VisibleType visible() default VISIBLE;

    /**
     * The name of the model object that is to be bound if multiple model objects are included for model
     * binding.
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding.
     */
    String modelAttribute() default "";

    class CheckBoxCaptionAspectCreator implements LinkkiAspect.Creator<UICheckBox> {

        @Override
        public LinkkiAspectDefinition create(UICheckBox annotation) {
            return new CaptionAspectDefinition(CaptionType.STATIC, annotation.caption());
        }

    }

}
