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

package org.linkki.search.annotation;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import org.linkki.core.uicreation.layout.LayoutDefinitionCreator;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;

/**
 * Specialization of @{link NestedPmoMethodLayoutDefinitionCreator} that adds a key binding to the
 * search button after the child components have been created.
 * 
 * @since 2.8.0
 */
// TODO Should be removed with LIN-2616
public class SearchInputLayoutDefinitionCreator implements LayoutDefinitionCreator<UISearchInputLayout> {

    private static final String SEARCH = "search";

    @Override
    public LinkkiLayoutDefinition create(UISearchInputLayout annotation, AnnotatedElement annotatedElement) {
        var nestedComponentLayoutDef = new NestedPmoMethodLayoutDefinitionCreator()
                .create(annotation, annotatedElement);

        return (parentComponent, pmo, bindingContext) -> {
            nestedComponentLayoutDef.createChildren(parentComponent, pmo, bindingContext);

            // Add a key binding on the button with the ID 'search' (if present) and set the
            // scope of the key binding to the search layout. This prevents triggering the key
            // binding when the focus is outside the search input layout.
            findSearchButton((Component)parentComponent)
                    .ifPresent(b -> b.addClickShortcut(Key.ENTER)
                            .listenOn((Component)parentComponent));
        };
    }

    private Optional<Button> findSearchButton(Component parent) {
        return parent.getChildren()
                .filter(c -> c.getId().filter(SEARCH::equals).isPresent())
                .filter(Button.class::isInstance)
                .map(Button.class::cast)
                .findFirst()
                .or(() -> parent.getChildren().map(this::findSearchButton)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst());
    }
}
