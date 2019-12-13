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
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.ui.pmo.ButtonPmoBuilder;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A section consists of a header displaying a caption and a body/content containing controls to view
 * and edit data. Optionally the section can be closed and opened. When the section is closed only the
 * header is shown.
 */
public abstract class AbstractSection extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final HorizontalLayout header;
    private final List<Component> headerComponents = new ArrayList<>();
    private final Label captionLabel;
    private final Optional<Button> closeButton;

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
        setStyleName(LinkkiTheme.SECTION);

        captionLabel = createCaption();

        if (closeable) {
            Button openCloseButton = createOpenCloseButton(this::switchOpenStatus);
            closeButton = Optional.of(openCloseButton);
        } else {
            closeButton = Optional.empty();
        }

        header = createHeader();
        updateHeader();
        addComponent(header);

        setCaption(caption);
    }

    /**
     * Creates a new section with the given caption. If {@code caption} is {@code null} or empty, no
     * caption will be shown.
     * 
     * @deprecated Use {@link #AbstractSection(String, boolean)} and a call to
     *             {@link #addHeaderButton(Button)} instead.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editButton If present the section has an edit button in the header.
     */
    @Deprecated
    public AbstractSection(@CheckForNull String caption, boolean closeable, Optional<Button> editButton) {
        this(caption, closeable);
        editButton.ifPresent(this::addHeaderButton);
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        headerLayout.addStyleName(LinkkiTheme.SECTION_CAPTION);

        headerLayout.addComponent(captionLabel);

        closeButton.ifPresent(headerLayout::addComponent);

        Label line = new Label("<hr/>", ContentMode.HTML);
        line.setWidth("100%");
        line.addStyleName(LinkkiTheme.SECTION_CAPTION_LINE);
        headerLayout.addComponent(line);
        headerLayout.setExpandRatio(line, 1);
        headerLayout.setMargin(false);

        return headerLayout;
    }

    private static Label createCaption() {
        Label label = new Label();
        label.addStyleName(LinkkiTheme.SECTION_CAPTION_TEXT);
        return label;
    }

    private static Button createOpenCloseButton(Handler toggleCloseOpen) {
        Button button = ComponentFactory.newButton(VaadinIcons.ANGLE_DOWN, ButtonPmoBuilder.DEFAULT_STYLES);
        button.addStyleName(LinkkiTheme.BUTTON_TEXT);
        button.addClickListener(e -> toggleCloseOpen.apply());
        return button;
    }

    private boolean shouldHeaderBePresent() {
        return !StringUtils.isEmpty(captionLabel.getValue())
                || this.closeButton.isPresent()
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
        captionLabel.setValue(caption);
        captionLabel.setVisible(!StringUtils.isEmpty(caption));

        updateHeader();
    }

    /**
     * Adds a new button to the header using the given button PMO. The new button is added after the
     * caption text.
     */
    public void addHeaderButton(Button button) {
        button.addStyleName(LinkkiTheme.BUTTON_TEXT);
        this.headerComponents.add(button);
        header.addComponent(button, 1);

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
     * Opens the sections.
     */
    public void open() {
        if (open) {
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
        if (closeButton.isPresent()) {
            closeButton.get().setIcon(open ? VaadinIcons.ANGLE_DOWN : VaadinIcons.ANGLE_RIGHT);
        }
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
