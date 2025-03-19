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

import static org.linkki.util.Objects.requireNonNull;

import java.io.Serial;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H3;

/**
 * A card displaying a component in {@link BoardLayout} with a caption.
 *
 * @since 2.8.0
 */
@Tag("linkki-board-component")
@JsModule("./src/linkki-board-component.ts")
public class BoardComponent extends HtmlComponent implements HasTheme {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String ATTR_SLOT = "slot";
    private static final String SLOT_HEADER = "header";

    /**
     * Creates a new {@link BoardComponent} with {@link BoardComponentVariant#MEDIUM medium size}.
     * 
     * @param header a short description of the component
     * @param component component to be displayed
     */
    public BoardComponent(String header, Component component) {
        this(header, component, BoardComponentVariant.MEDIUM);
    }

    /**
     * Creates a new {@link BoardComponent} with the given {@link BoardComponentVariant}.
     * 
     * @param header a short description of the component
     * @param component component to be displayed
     * @param variant the theme variant, one of {@link BoardComponentVariant}.
     */
    public BoardComponent(String header, Component component, BoardComponentVariant variant) {
        requireNonNull(header, "header must not be null");
        requireNonNull(component, "component must not be null");
        requireNonNull(variant, "variant must not be null");

        setThemeName(variant.getVariantName());
        setHeader(header);
        addElement(component);
    }

    private void addElement(Component component) {
        getElement().appendChild(component.getElement());
    }

    private BoardComponent setHeader(String header) {
        return setHeader(new H3(header));
    }

    private BoardComponent setHeader(Component header) {
        getHeader().ifPresent(c -> getElement().removeChild(c.getElement()));
        header.getElement().setAttribute(ATTR_SLOT, SLOT_HEADER);
        addElement(header);
        return this;
    }

    private Optional<Component> getHeader() {
        return getChildren()
                .filter(c -> c.getElement().getAttribute(ATTR_SLOT).equals(SLOT_HEADER))
                .findFirst();
    }

    /**
     * Creates a new {@link BoardComponent} with {@link BoardComponentVariant#MEDIUM medium size}.
     * 
     * @param caption a short description of the component
     * @param pmo PMO of the component
     */
    public static BoardComponent withPmo(String caption, Object pmo) {
        return withPmo(caption, pmo, BoardComponentVariant.MEDIUM);
    }

    /**
     * Creates a new {@link BoardComponent} with the given {@link BoardComponentVariant}.
     * 
     * @param caption a short description of the component
     * @param pmo PMO of the component
     * @param variant the theme variant, one of {@link BoardComponentVariant}.
     */
    public static BoardComponent withPmo(String caption, Object pmo, BoardComponentVariant variant) {
        return new BoardComponent(caption,
                VaadinUiCreator.createComponent(pmo, new BindingContext(BoardComponent.class.getName())),
                variant);
    }

    public enum BoardComponentVariant {

        MEDIUM("medium"),
        LARGE("large");

        private final String variantName;

        BoardComponentVariant(String variantName) {
            this.variantName = variantName;
        }

        public String getVariantName() {
            return variantName;
        }

    }

}