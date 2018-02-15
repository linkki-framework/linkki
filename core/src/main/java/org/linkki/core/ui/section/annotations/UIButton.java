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
import static org.linkki.core.ui.section.annotations.VisibleType.VISIBLE;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.aspect.definition.CompositeAspectDefinition;
import org.linkki.core.ui.section.annotations.UIButton.ButtonAspectDefinition;
import org.linkki.core.ui.section.annotations.adapters.ButtonBindingDefinition;
import org.linkki.core.ui.section.annotations.aspect.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.CaptionAspectDefinition;
import org.linkki.core.ui.section.annotations.aspect.EnabledAspectForBindingDefinition;
import org.linkki.core.ui.section.annotations.aspect.VisibleAspectForBindingDefinition;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.FontAwesome;

/**
 * Marks a method which is carried out when the {@link UIButton} is clicked.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(ButtonBindingDefinition.class)
@LinkkiAspect(ButtonAspectDefinition.class)
public @interface UIButton {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    int position();

    /** Provides a description label next to the button */
    String label() default "";

    /** Displays a label next to the button. Buttons don't show their label by default */
    boolean showLabel() default false;

    /**
     * Static text displayed on the button. If the value should be determined dynamically, use
     * {@link CaptionType#DYNAMIC} instead and ignore this attribute
     */
    String caption() default "";

    /** Defines how the value of caption should be retrieved, using values of {@link EnabledType} */
    CaptionType captionType() default CaptionType.STATIC;

    /**
     * @deprecated Deprecated, use captionType=CaptionType.STATIC instead. Will be removed in next
     *             version!
     */
    @Deprecated
    boolean showCaption() default true;

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /** Defines the button's icon, using constants in {@link FontAwesome} */
    FontAwesome icon() default FontAwesome.PLUS;

    /** If <code>true</code>, the button will be displayed with the defined {@link #icon()} */
    boolean showIcon() default false;

    /**
     * Defines a list of CSS class names that are added to the created UI component.
     */
    String[] styleNames() default {};

    /**
     * Set a short cut for the button, use constants in {@link KeyCode}
     */
    int shortcutKeyCode() default -1;

    /**
     * Set a modifier for the short cut. Only useful in combination with a {@link #shortcutKeyCode()}.
     * Use constants from {@link ModifierKey}.
     */
    int[] shortcutModifierKeys() default {};

    /**
     * Aspect definition for {@link UIButton} annotation.
     */
    class ButtonAspectDefinition extends CompositeAspectDefinition {

        public ButtonAspectDefinition() {
            super(new EnabledAspectForBindingDefinition(),
                    new VisibleAspectForBindingDefinition(),
                    new ButtonCaptionAspectDefinition(),
                    new ButtonInvokeAspectDefinition());
        }
    }

    class ButtonCaptionAspectDefinition extends CaptionAspectDefinition {

        @SuppressWarnings("null")
        private UIButton buttonAnnotation;

        @Override
        public void initialize(Annotation annotation) {
            this.buttonAnnotation = (UIButton)annotation;
        }

        @Override
        protected String getStaticCaption() {
            return buttonAnnotation.caption();
        }

        @Override
        public CaptionType getCaptionType() {
            return buttonAnnotation.captionType();
        }
    }
}
