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

import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.vaadin.component.menu.SingleItemMenuBar;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;

/**
 * Marks a method which is invoked when the {@link UIMenuButton} is clicked.
 * <p>
 * A {@link UIMenuButton} is a {@link SingleItemMenuBar}. As such it provides the same functionality
 * as linkki's {@link UIButton}, but it is styled differently and is better suited to use it
 * alongside a {@link UIMenuList}.
 * <p>
 * Usage:
 * 
 * <pre>
 * <code>&#64;UIMenuButton(position = 100, caption = "Play", showIcon = true, icon = VaadinIcons.PLAY)
 * public void play() {
 *     ...
 * }
 * </code>
 * </pre>
 * 
 * @since 2.8.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiPositioned
@LinkkiAspect(UIMenuButton.MenuButtonAspectDefinitionCreator.class)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(UIMenuButton.MenuButtonComponentDefinitionCreator.class)
public @interface UIMenuButton {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a label on the left side of the button.
     */
    String label() default "";

    /**
     * Static text displayed on the menu button. If the value should be determined dynamically, use
     * {@link CaptionType#DYNAMIC} instead and ignore this attribute
     */
    String caption();

    /** Defines how the value of caption should be retrieved, using values of {@link CaptionType} */
    CaptionType captionType() default CaptionType.STATIC;

    /** Defines if a UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    /** Defines the menu button's icon, using constants in {@link VaadinIcon} */
    VaadinIcon icon();

    /**
     * If <code>true</code>, the button will be displayed with the defined {@link #icon()}
     * <p>
     * If <code>false</code>, you still have to set an {@link #icon() icon} because
     * <code>null</code> is not a valid value.
     */
    boolean showIcon() default true;

    /**
     * Defines the look of the menu button. The most common variants are:
     * <ul>
     * <li>{@link MenuBarVariant#LUMO_PRIMARY} for primary menu button</li>
     * <li>{@link MenuBarVariant#LUMO_TERTIARY_INLINE} for menu buttons that are displayed like a
     * link</li>
     * </ul>
     * 
     * @see MenuBarVariant
     */
    MenuBarVariant[] variants() default {};

    class MenuButtonComponentDefinitionCreator implements ComponentDefinitionCreator<UIMenuButton> {

        @Override
        public LinkkiComponentDefinition create(UIMenuButton annotation, AnnotatedElement annotatedElement) {
            return pmo -> new SingleItemMenuBar(annotation.caption(),
                    annotation.showIcon() ? annotation.icon() : null, annotation.variants());
        }
    }

    class MenuButtonAspectDefinitionCreator implements AspectDefinitionCreator<UIMenuButton> {

        @Override
        public LinkkiAspectDefinition create(UIMenuButton annotation) {
            return new CompositeAspectDefinition(
                    new EnabledAspectDefinition(annotation.enabled()),
                    new VisibleAspectDefinition(annotation.visible()),
                    new LabelAspectDefinition(annotation.label()),
                    new CaptionAspectDefinition(annotation.captionType(), annotation.caption()),
                    new MenuButtonInvokeAspectDefinition());
        }
    }

    class MenuButtonInvokeAspectDefinition implements LinkkiAspectDefinition {

        public static final String NAME = StringUtils.EMPTY;

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelUpdated) {
            var menuBar = (SingleItemMenuBar)componentWrapper.getComponent();
            var menuItem = menuBar.getItems().get(0);
            menuItem.addClickListener(selectedItem -> {
                propertyDispatcher.push(Aspect.of(NAME));
                modelUpdated.apply();
            });
        }
    }
}
