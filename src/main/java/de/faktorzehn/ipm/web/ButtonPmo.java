package de.faktorzehn.ipm.web;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

/** A presentation model object for a button. */
@FunctionalInterface
public interface ButtonPmo {

    /** Executes the button click action. */
    void onClick();

    /** Returns the icon to display for the button. */
    default Resource buttonIcon() {
        return FontAwesome.PENCIL;
    }

}
