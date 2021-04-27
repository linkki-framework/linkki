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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.core.vaadin.component.HasCaption;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A section consists of a header displaying a caption and a body/content containing controls to view
 * and edit data. Optionally the section can be closed and opened. When the section is closed only the
 * header is shown.
 */
@CssImport(value = "./styles/linkki-section.css", include = "@vaadin/vaadin-lumo-styles/all-imports")
public abstract class AbstractSection extends VerticalLayout implements HasCaption {

    private static final long serialVersionUID = 1L;

    private final HorizontalLayout header;
    private final List<Component> headerComponents = new ArrayList<>();
    private final H4 captionLabel;
    private final Button closeButton;

    private boolean open = true;

    /**
     * Creates a new section with the given caption that cannot be closed. If {@code caption} is
     * {@code null} or empty, no caption will be shown.
     * 
     * @param caption the caption to display for this section
     */
    public AbstractSection(@CheckForNull String caption) {
        this(caption, false);
    }

    /**
     * Creates a new section with the given caption. If {@code caption} is {@code null} or empty, no
     * caption will be shown.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public AbstractSection(@CheckForNull String caption, boolean closeable) {
        setMargin(false);
        setSpacing(false);
        setPadding(false);
        setClassName(LinkkiTheme.SECTION);

        captionLabel = createCaption();
        closeButton = createOpenCloseButton(this::switchOpenStatus);
        closeButton.setVisible(closeable);

        header = createHeader();
        updateHeader();
        add(header);

        setCaption(caption);
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        headerLayout.addClassName(LinkkiTheme.SECTION_CAPTION);

        headerLayout.add(captionLabel);
        headerLayout.add(closeButton);

        headerLayout.setMargin(false);

        return headerLayout;
    }

    private static H4 createCaption() {
        return new H4();
    }

    private static Button createOpenCloseButton(Handler toggleCloseOpen) {
        // TODO LIN-2249 ButtonPmoBuilder.DEFAULT_STYLES
        Button button = ComponentFactory.newButton(VaadinIcon.ANGLE_DOWN.create(), Collections.emptyList());
        button.addClassName(LinkkiTheme.BUTTON_TEXT);
        button.addClickListener(e -> toggleCloseOpen.apply());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return button;
    }

    private boolean shouldHeaderBePresent() {
        return !StringUtils.isEmpty(getCaption())
                || this.closeButton.isVisible()
                || !this.headerComponents.isEmpty();
    }

    private void updateHeader() {
        header.setVisible(shouldHeaderBePresent());
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

        updateHeader();
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
        button.addClassName(LinkkiTheme.BUTTON_TEXT);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerComponents.add(button);
        header.addComponentAtIndex(1, button);
        updateHeader();
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

        headerComponents.add(component);
        header.addComponentAtIndex(header.getComponentCount() - 1, component);
        updateHeader();
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
        getSectionContent().setVisible(open);
    }

    /**
     * @implSpec Implementations of this method have to return the section's content, which is the
     *           {@link Component} added to the section. The section's header is not part of the content
     *           and has to be excluded.
     * 
     * @return the content of this section
     */
    protected abstract Component getSectionContent();

}
