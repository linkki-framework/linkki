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
package org.linkki.core.vaadin.component.section;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.core.vaadin.component.HasCaption;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A section consists of a header displaying a caption and a body/content containing controls to view
 * and edit data. Optionally the section can be closed and opened. When the section is closed only the
 * header is shown.
 */
@Tag("linkki-section")
@CssImport("./styles/linkki-section.css")
@JsModule("./src/linkki-section.ts")
public class LinkkiSection extends HtmlComponent implements HasCaption {

    public static final String THEME_VARIANT_HORIZONTAL = "horizontal";

    static final String SLOT_HEADER_COMPONENTS = "header-components";
    static final String SLOT_CLOSE_TOGGLE = "close-toggle";
    static final String SLOT_CONTENT = "content";

    private static final long serialVersionUID = 1L;

    private final H4 captionLabel;
    private final Button closeButton;
    private final Div content;

    private boolean open = true;

    /**
     * Creates a new section with the given caption that cannot be closed. If {@code caption} is
     * {@code null} or empty, no caption will be shown.
     * 
     * @param caption the caption to display for this section
     */
    public LinkkiSection(@CheckForNull String caption) {
        this(caption, false, 1);
    }

    /**
     * Creates a new section with the given caption. If {@code caption} is {@code null} or empty, no
     * caption will be shown.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param columns number of columns in which the content components are displayed
     */
    @SuppressWarnings("deprecation")
    public LinkkiSection(@CheckForNull String caption, boolean closeable, int columns) {
        setClassName(LinkkiTheme.SECTION);

        captionLabel = createCaption();
        captionLabel.getElement().setAttribute("slot", SLOT_HEADER_COMPONENTS);

        closeButton = createOpenCloseButton(this::switchOpenStatus);
        closeButton.setVisible(closeable);
        closeButton.getElement().setAttribute("slot", SLOT_CLOSE_TOGGLE);

        setCaption(caption);
        content = new Div();
        content.getElement().setAttribute("slot", SLOT_CONTENT);

        getElement().appendChild(captionLabel.getElement(), closeButton.getElement(), content.getElement());
        getStyle().set("--section-item-width",
                       "calc(100% / " + columns + " - var(--linkki-section-horizontal-gap))");
    }

    private static H4 createCaption() {
        H4 h4 = new H4();
        h4.addClassName(LinkkiTheme.SECTION_CAPTION_TEXT);
        h4.getStyle().set("margin", "0");
        return h4;
    }

    private static Button createOpenCloseButton(Handler toggleCloseOpen) {
        Button button = ComponentFactory.newButton(VaadinIcon.ANGLE_DOWN.create(), Collections.emptyList());
        button.addClickListener(e -> toggleCloseOpen.apply());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return button;
    }

    /**
     * Updates the caption of this section. If there is no caption, a new one will be added. If the new
     * caption is {@code null} or empty, any existing caption label will be removed.
     * 
     * @param caption the caption text
     */
    @Override
    public void setCaption(@CheckForNull String caption) {
        captionLabel.setText(caption);
        captionLabel.setVisible(!StringUtils.isEmpty(caption));
    }

    @Override
    public String getCaption() {
        return captionLabel.getText();
    }

    /**
     * Adds a button to the header using the given button PMO. The new button is added on the left, in
     * front of the components added earlier. However, the caption text will always be the leftmost
     * item, if it is present.
     */
    public void addHeaderButton(Button button) {
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        button.getElement().setAttribute("slot", SLOT_HEADER_COMPONENTS);
        getElement().insertChild(1, button.getElement());
    }

    /**
     * Adds a component to the header. The new component is added on the right, after the components
     * added earlier. However, the hide/expand arrow will always be the rightmost item, if it is
     * present.
     */
    public void addHeaderComponent(Component component) {
        if (component instanceof Button) {
            ((Button)component).addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        }

        component.getElement().setAttribute("slot", SLOT_HEADER_COMPONENTS);
        getElement().appendChild(component.getElement());
    }

    /**
     * Returns all components in the header, not including the caption.
     * 
     * @since 2.0.0
     */
    public List<Component> getHeaderComponents() {
        return getChildren().filter(c -> SLOT_HEADER_COMPONENTS.contentEquals(c.getElement().getAttribute("slot")))
                .collect(Collectors.toList());
    }

    /**
     * Returns <code>true</code> if the section is open.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Returns <code>true</code> if the section is closed.
     */
    public boolean isClosed() {
        return !open;
    }

    /**
     * Opens the section.
     */
    public void open() {
        if (isOpen()) {
            return;
        }
        switchOpenStatus();
    }

    /**
     * Closes the section.
     */
    public void close() {
        if (isClosed()) {
            return;
        }
        switchOpenStatus();
    }

    protected void switchOpenStatus() {
        open = !open;
        closeButton.setIcon(open ? VaadinIcon.ANGLE_DOWN.create() : VaadinIcon.ANGLE_RIGHT.create());
        getContentWrapper().setVisible(open);
    }

    /**
     * @implSpec Implementations of this method have to return the section's content, which is the
     *           {@link Component} added to the section. The section's header is not part of the content
     *           and has to be excluded.
     * 
     * @return the content of this section
     * @deprecated use {@link #getContentWrapper()} instead.
     */
    @Deprecated(since = "2.0.0")
    protected Component getSectionContent() {
        return content;
    }

    /**
     * Returns the section's content. The section's header is not part of the content.
     * 
     * @return the content of this section
     * @since 2.0.0
     */
    public Div getContentWrapper() {
        return content;
    }

}
