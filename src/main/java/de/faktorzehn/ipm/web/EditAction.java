package de.faktorzehn.ipm.web;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

/**
 * Action that is executed when the user clicks on the edit button in a section.
 */
@FunctionalInterface
public interface EditAction {

    /**
     * Executes the action.
     */
    void exec();

    /** Returns the icon for the button executing this action. */
    default Resource buttonIcon() {
        return FontAwesome.PENCIL;
    }

}
