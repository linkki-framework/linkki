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
package org.linkki.core.ui.section;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.ButtonPmoBuilder;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * A section consists of a header displaying a caption and a body/content containing controls to view
 * and edit data. Optionally the section can be closed and opened. When the section is closed only the
 * header is shown.
 */
public abstract class AbstractSection extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    @Nullable
    private HorizontalLayout header;
    @Nullable
    private Button openCloseButton;
    private boolean open = true;
    private Optional<Button> editButton = Optional.empty();

    /**
     * Creates a new, section with the given caption that cannot be closed.
     * 
     * @param caption the caption to display for this section
     */
    public AbstractSection(String caption) {
        this(caption, false, Optional.empty());
    }

    /**
     * Creates a new section with the given caption.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public AbstractSection(String caption, boolean closeable) {
        this(caption, closeable, Optional.empty());
    }

    /**
     * Creates a new section with the given caption.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editButton If present the section has an edit button in the header.
     */
    public AbstractSection(String caption, boolean closeable, Optional<Button> editButton) {
        super();
        requireNonNull(caption, "caption must not be null");
        this.editButton = requireNonNull(editButton, "editButton must not be null");
        setMargin(false);
        setSpacing(false);
        if (StringUtils.isNotEmpty(caption) || editButton.isPresent()) {
            this.openCloseButton = closeable ? createOpenCloseButton(this::switchOpenStatus) : null;
            this.header = createHeader(caption, editButton, Optional.ofNullable(openCloseButton));
            header.setMargin(false);
            addComponent(header);
        }
    }

    private static HorizontalLayout createHeader(String caption,
            Optional<Button> editButton,
            Optional<Button> openCloseButton) {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        headerLayout.addStyleName(ApplicationStyles.SECTION_CAPTION);

        if (StringUtils.isNotEmpty(caption)) {
            Label captionLabel = new Label(caption);
            captionLabel.addStyleName(ApplicationStyles.SECTION_CAPTION_TEXT);
            headerLayout.addComponent(captionLabel);
        }

        editButton.ifPresent(b -> addHeaderButton(headerLayout, b));
        openCloseButton.ifPresent(b -> addHeaderButton(headerLayout, b));

        Label line = new Label("<hr/>", ContentMode.HTML);
        line.setWidth("100%");
        line.addStyleName(ApplicationStyles.SECTION_CAPTION_LINE);
        headerLayout.addComponent(line);
        headerLayout.setExpandRatio(line, 1);

        return headerLayout;
    }

    private static Button createOpenCloseButton(Handler toggleCloseOpen) {
        Button button = ComponentFactory.newButton(VaadinIcons.ANGLE_DOWN, ButtonPmoBuilder.DEFAULT_STYLES);
        button.addStyleName(ApplicationStyles.BUTTON_TEXT);
        button.addClickListener(e -> toggleCloseOpen.apply());
        return button;
    }

    /*
     * Cannot delegate to addHeaderButton with index as they do not call each other in
     * AbstractOrderedLayout
     */
    private static void addHeaderButton(AbstractOrderedLayout header, Button button) {
        button.addStyleName(ApplicationStyles.BUTTON_TEXT);
        header.addComponent(button);
        // Set the spacing to false as the button already has a margin around it.
        header.setSpacing(false);
    }

    private static void addHeaderButton(AbstractOrderedLayout header, Button button, int index) {
        button.addStyleName(ApplicationStyles.BUTTON_TEXT);
        header.addComponent(button, index);
        // Set the spacing to false as the button already has a margin around it.
        header.setSpacing(false);
    }

    public boolean isEditButtonAvailable() {
        return editButton.isPresent();
    }

    /**
     * Adds a new button to the header using the given button PMO. The new button is added before the
     * close button. If the section does not have a close button it is added at the end of the header.
     */
    public void addHeaderButton(Button button) {
        addBeforeCloseButton(button);
    }

    @SuppressWarnings("null")
    private void addBeforeCloseButton(Button headerButton) {
        if (openCloseButton != null) {
            if (header != null) {
                addHeaderButton(header, headerButton, header.getComponentIndex(openCloseButton));
            }
        } else {
            if (header != null) {
                addHeaderButton(header, headerButton, header.getComponentCount() - 1);
            }
        }
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
        if (openCloseButton != null) {
            openCloseButton.setIcon(open ? VaadinIcons.ANGLE_DOWN : VaadinIcons.ANGLE_RIGHT);
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
