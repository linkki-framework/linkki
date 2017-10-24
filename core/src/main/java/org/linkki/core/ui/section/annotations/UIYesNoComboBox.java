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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import org.linkki.core.ui.components.ItemCaptionProvider;
import org.linkki.core.ui.messages.Messages;
import org.linkki.core.ui.section.annotations.adapters.YesNoComboBoxBindingDefinition;

/**
 * A combo box for boolean or Boolean values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(YesNoComboBoxBindingDefinition.class)
public @interface UIYesNoComboBox {

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
     * Specifies the width of the field. Use CSS units like em, px or %.
     * <p>
     * For example: "25em" or "100%".
     */
    String width() default "-1px";

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
     * Specifies which {@link ItemCaptionProvider} should be used to convert boolean values into
     * String captions.
     * <p>
     * Default value prints the boolean values in the system locale (for example "yes"/"no" in
     * English or "ja"/nein" in German).
     */
    Class<? extends ItemCaptionProvider<?>> itemCaptionProvider() default BooleanCaptionProvider.class;

    public class BooleanCaptionProvider implements ItemCaptionProvider<Object> {

        @Override
        @Nonnull
        public String getCaption(Object o) {
            return (o instanceof Boolean) ? booleanToCaption((Boolean)o) : ""; //$NON-NLS-1$
        }

        private String booleanToCaption(Boolean bool) {
            return bool ? Messages.getString("BooleanCaptionProvider.True") //$NON-NLS-1$
                    : Messages.getString("BooleanCaptionProvider.False"); //$NON-NLS-1$
        }
    }
}

