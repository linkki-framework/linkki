/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.page;

import com.vaadin.ui.ComponentContainer;

/**
 * A page is a container component displayed in the UI that consists of sections.
 */
public interface Page extends ComponentContainer {

    /** Creates the content (sections) of this page. */
    void createContent();

    /**
     * Reloads the data bindings of the content (sections) displayed on this page. No sections are
     * removed/added.
     */
    void reloadBindings();
}
