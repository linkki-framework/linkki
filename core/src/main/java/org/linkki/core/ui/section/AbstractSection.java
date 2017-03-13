package org.linkki.core.ui.section;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ButtonPmo;
import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * A section consists of a header displaying a caption and a body/content containing controls to
 * view and edit data. Optionally the section can be closed and opened. When the section is closed
 * only the header is shown.
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
        if (StringUtils.isNotEmpty(caption) || editButton.isPresent()) {
            createHeader(caption, closeable);
        } else {
            createSpacer();
        }
    }

    private void createHeader(String caption, boolean closeable) {
        HorizontalLayout newHeader = new HorizontalLayout();
        this.header = newHeader;
        newHeader.setSpacing(true);
        addComponent(newHeader);
        newHeader.addStyleName(ApplicationStyles.SECTION_CAPTION);

        Label l = new Label(caption);
        l.addStyleName(ApplicationStyles.SECTION_CAPTION_TEXT);
        newHeader.addComponent(l);
        newHeader.setComponentAlignment(l, Alignment.MIDDLE_LEFT);

        editButton.ifPresent(b -> newHeader.addComponent(b));

        if (closeable) {
            createOpenCloseButton();
        }

        Label line = new Label("<hr/>", ContentMode.HTML);
        line.addStyleName(ApplicationStyles.SECTION_CAPTION_LINE);
        newHeader.addComponent(line);
        newHeader.setComponentAlignment(line, Alignment.MIDDLE_LEFT);
    }

    /**
     * The spacer consists of a layout and a label. The layout is needed to force vaadin to
     * correctly calculate the height when the content below is set to height: 100%
     */
    private void createSpacer() {
        VerticalLayout verticalLayout = new VerticalLayout();
        Label spacer = new Label();
        verticalLayout.addComponent(spacer);
        addComponent(verticalLayout);
    }

    public boolean isEditButtonAvailable() {
        return editButton.isPresent();
    }

    private void createOpenCloseButton() {
        final OpenCloseButtonPmo buttonPmo = new OpenCloseButtonPmo(this::switchOpenStatus);
        openCloseButton = ComponentFactory.newButton(buttonPmo.getButtonIcon(), buttonPmo.getStyleNames());
        openCloseButton.addClickListener(e -> buttonPmo.onClick());
        if (header != null) {
            header.addComponent(openCloseButton);
        }
    }

    /**
     * Adds a new button to the header for the given button PMO. The new button is added before the
     * close button. If the section does not have a close button it is added at the end of the
     * header.
     */
    public void addHeaderButton(Button button) {
        addBeforeCloseButton(button);
    }

    private void addBeforeCloseButton(Button headerButton) {
        if (openCloseButton != null) {
            if (header != null) {
                header.addComponent(headerButton, header.getComponentIndex(openCloseButton));
            }
        } else {
            if (header != null) {
                header.addComponent(headerButton, header.getComponentCount() - 1);
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
     * Closed the section.
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
            openCloseButton.setIcon(open ? FontAwesome.ANGLE_DOWN : FontAwesome.ANGLE_RIGHT);
        }
        setAllChildComponentsVisibilityBasedOnOpenStatus(this);
    }

    protected void setAllChildComponentsVisibilityBasedOnOpenStatus(AbstractLayout layout) {
        for (Component c : layout) {
            if (c != header) {
                c.setVisible(open);
            }
        }
    }

    /** PMO for the {@link #openCloseButton}. */
    private static class OpenCloseButtonPmo implements ButtonPmo {

        private Runnable switchRunnable;

        public OpenCloseButtonPmo(Runnable switchRunnable) {
            super();
            this.switchRunnable = requireNonNull(switchRunnable, "switchRunnable must not be null");
        }

        @Override
        public void onClick() {
            switchRunnable.run();
        }

        @Override
        public Resource getButtonIcon() {
            return FontAwesome.ANGLE_DOWN;
        }
    }

}
