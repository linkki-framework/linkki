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

package org.linkki.core.vaadin.component.board;

import java.io.Serial;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * Dashboard-like responsive layout displaying {@link BoardComponent} as cards.
 *
 * @since 2.8.0
 */
@Tag("linkki-board-layout")
@JsModule("./src/linkki-board-layout.ts")
public class BoardLayout extends HtmlComponent {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an empty {@link BoardLayout}. Use {@link #add(BoardComponent...)} to add some
     * {@link BoardComponent board components}.
     * 
     * @see #BoardLayout(BoardComponent...)
     */
    public BoardLayout() {
        setSizeFull();
    }

    /**
     * Creates a new {@link BoardLayout} with the given {@link BoardComponent components}.
     * <p>
     * The provided components are displayed in the row direction.
     * 
     * @param boardComponents components to be displayed
     */
    public BoardLayout(BoardComponent... boardComponents) {
        this();
        add(boardComponents);
    }

    /**
     * Add one or more {@link BoardComponent board components} to this {@link BoardLayout}.
     * <p>
     * The provided components are displayed in the row direction.
     * 
     * @apiNote The method is final to be safely called in the constructor
     * 
     * @param boardComponents components to be displayed
     * @return This {@link BoardLayout} for method chaining
     */
    public final BoardLayout add(BoardComponent... boardComponents) {
        for (BoardComponent boardComponent : boardComponents) {
            getElement().appendChild(boardComponent.getElement());
        }
        return this;
    }

}
