package de.faktorzehn.ipm.web.ui.section;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.faktorzehn.ipm.web.EditAction;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;

/**
 * A section consists of a header displaying a caption and a body/content containing controls to
 * view and edit data. Optionally the section can be closed and opened. When the section is closed
 * only the header is shown.
 */
public abstract class AbstractSection extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private HorizontalLayout header;
    private Button openCloseButton = null;
    private boolean open = true;
    private Button editButton = null;

    /**
     * Creates a new, section with the given caption that cannot be closed.
     * 
     * @param caption the caption to display for this section
     */
    public AbstractSection(@Nonnull String caption) {
        this(caption, false, Optional.empty());
    }

    /**
     * Creates a new section with the given caption.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public AbstractSection(@Nonnull String caption, boolean closeable) {
        this(caption, closeable, Optional.empty());
    }

    /**
     * Creates a new section with the given caption.
     * 
     * @param caption the caption to display for this section
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editAction If present the section has a button to trigger the given action.
     */
    public AbstractSection(@Nonnull String caption, boolean closeable, Optional<EditAction> editAction) {
        super();
        checkNotNull(caption);
        checkNotNull(editAction);
        createHeader(caption, closeable, editAction);
    }

    private void createHeader(@Nonnull String caption, boolean closeable, Optional<EditAction> editAction) {
        header = new HorizontalLayout();
        header.setSpacing(true);
        addComponent(header);
        header.addStyleName(ApplicationStyles.SECTION_CAPTION);

        Label l = new Label(caption);
        l.addStyleName(ApplicationStyles.SECTION_CAPTION_TEXT);
        header.addComponent(l);
        header.setComponentAlignment(l, Alignment.MIDDLE_LEFT);

        if (editAction.isPresent()) {
            createEditButton(editAction.get());
        }

        if (closeable) {
            createOpenCloseButton();
        }

        Label line = new Label("<hr/>", ContentMode.HTML);
        line.addStyleName(ApplicationStyles.SECTION_CAPTION_LINE);
        header.addComponent(line);
        header.setComponentAlignment(line, Alignment.MIDDLE_LEFT);
    }

    private void createEditButton(EditAction editAction) {
        editButton = new Button(editAction.buttonIcon());
        editButton.setTabIndex(-1);
        editButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        editButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editButton.addClickListener(e -> editAction.exec());
        header.addComponent(editButton);
    }

    public boolean isEditButtonAvailable() {
        return editButton != null;
    }

    private void createOpenCloseButton() {
        openCloseButton = new Button(FontAwesome.ANGLE_DOWN);
        openCloseButton.setTabIndex(-1);
        openCloseButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        openCloseButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        openCloseButton.addClickListener(e -> switchOpenStatus());
        header.addComponent(openCloseButton);
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

}
