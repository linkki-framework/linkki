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
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.ButtonInvokeAspectDefinition;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UIButton.ButtonAspectCreator;
import org.linkki.core.ui.element.annotation.UIButton.ButtonComponentDefinitionCreator;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;

/**
 * Marks a method which is carried out when the {@link UIButton} is clicked.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(ButtonComponentDefinitionCreator.class)
@LinkkiAspect(ButtonAspectCreator.class)
@LinkkiPositioned
public @interface UIButton {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the button */
    String label() default "";

    /**
     * Static text displayed on the button. If the value should be determined dynamically, use
     * {@link CaptionType#DYNAMIC} instead and ignore this attribute.
     */
    String caption() default DERIVED_BY_LINKKI;

    /**
     * Defines how the value of caption should be retrieved, using values of {@link CaptionType}.
     * <p>
     * Despite in other annotations like {@link BindCaption} the default here is
     * {@link CaptionType#STATIC} (and NOT {@link CaptionType#AUTO} because it is a common use case to
     * have a button with only an icon but no caption.
     * 
     */
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

    /** Defines the button's icon, using constants in {@link VaadinIcons} */
    VaadinIcons icon() default VaadinIcons.PLUS;

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
     * Aspect definition creator for the {@link UIButton} annotation.
     */
    static class ButtonAspectCreator implements AspectDefinitionCreator<UIButton> {

        @Override
        public LinkkiAspectDefinition create(UIButton annotation) {
            return new CompositeAspectDefinition(
                    new LabelAspectDefinition(annotation.label()),
                    new EnabledAspectDefinition(annotation.enabled()),
                    new VisibleAspectDefinition(annotation.visible()),
                    new CaptionAspectDefinition(annotation.captionType(), annotation.caption()),
                    new ButtonInvokeAspectDefinition());
        }

    }

    static class ButtonComponentDefinitionCreator implements ComponentDefinitionCreator<UIButton> {

        @Override
        public LinkkiComponentDefinition create(UIButton annotation, AnnotatedElement annotatedElement) {
            return pmo -> {
                Button button = ComponentFactory.newButton();
                if (annotation.showIcon()) {
                    button.setIcon(annotation.icon());
                }
                button.addStyleNames(annotation.styleNames());
                if (annotation.shortcutKeyCode() != -1) {
                    button.setClickShortcut(annotation.shortcutKeyCode(), annotation.shortcutModifierKeys());
                }
                return button;
            };
        }
    }

}
