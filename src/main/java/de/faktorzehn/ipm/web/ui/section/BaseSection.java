package de.faktorzehn.ipm.web.ui.section;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.faktorzehn.ipm.web.EditAction;

/**
 * Base class for sections that are supported by the {@link PmoBasedSectionFactory}.
 */
public abstract class BaseSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new section with the given caption that is not closeable.
     * 
     * @param caption the caption
     */
    public BaseSection(String caption) {
        super(caption);
    }

    /**
     * Creates a new section with the given caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public BaseSection(String caption, boolean closeable) {
        super(caption, closeable);
    }

    /**
     * Creates a new, section with the given caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editAction If present the section has a button to trigger the given action.
     */
    public BaseSection(String caption, boolean closeable, Optional<EditAction> editAction) {
        super(caption, closeable, editAction);
    }

    /**
     * Adds the given label / component pair to the section.
     */
    public abstract Label add(@Nonnull String label, @Nonnull Component component);

    /**
     * Adds the component to the section without a label.
     */
    public abstract void add(@Nonnull Component component);

}
