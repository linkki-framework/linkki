/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.area;

import org.linkki.core.ui.page.Page;

import com.vaadin.ui.ComponentContainer;

/**
 * An area is a container component displayed in the UI that consists of other areas or pages.
 * 
 * @see Page
 */
public interface Area extends ComponentContainer {

    /** Creates the content (children) of this area. */
    void createContent();

    /**
     * Updates the content (children) of this area. In most cases it is enough to update the UI
     * content from the underlying PMOs by just calling <code>reloadBindings()</code>. If child
     * components are created dynamically, you have to added or remove child components.
     */
    void updateContent();

    /**
     * Reload the data bindings for the content (children) of this area. No children may be added or
     * removed.
     */
    void reloadBindings();
}
