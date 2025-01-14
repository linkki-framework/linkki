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

package de.faktorzehn.commons.linkki.search.annotation;

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
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;

/**
 * This annotation is similar to {@link de.faktorzehn.commons.linkki.ui.menu.UIMenuList} but has
 * special settings for the column of additional actions in a search result table.
 * 
 * @see de.faktorzehn.commons.linkki.ui.menu.UIMenuList
 */
@SuppressWarnings("deprecation")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiPositioned
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiComponent(UISearchResultAction.SearchResultActionComponentDefinitionCreator.class)
@LinkkiAspect(UISearchResultAction.SearchResultListAspectDefinitionCreator.class)
public @interface UISearchResultAction {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position() default Integer.MAX_VALUE;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    class SearchResultActionComponentDefinitionCreator
            implements ComponentDefinitionCreator<UISearchResultAction> {

        @Override
        public LinkkiComponentDefinition create(UISearchResultAction annotation,
                AnnotatedElement annotatedElement) {
            return pmo -> {
                de.faktorzehn.commons.linkki.ui.menu.SingleItemMenuBar menuBar = new de.faktorzehn.commons.linkki.ui.menu.SingleItemMenuBar(
                        "", VaadinIcon.ELLIPSIS_DOTS_H);
                menuBar.getContent().addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
                return menuBar;
            };
        }
    }

    class SearchResultListAspectDefinitionCreator
            implements AspectDefinitionCreator<UISearchResultAction> {

        @Override
        public LinkkiAspectDefinition create(UISearchResultAction annotation) {
            return new CompositeAspectDefinition(
                    new VisibleAspectDefinition(annotation.visible()),
                    new de.faktorzehn.commons.linkki.ui.menu.MenuItemsAspectDefinition());
        }

    }

}
