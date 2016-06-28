/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.application;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.Navigator.EmptyView;

/**
 * An extension of the {@link EmptyView} that adds a {@code @CDIView} annotation so that it can be
 * displayed with a {@link com.vaadin.cdi.CDIViewProvider CDIViewProvider}.
 */
@CDIView
public class EmptyCdiView extends EmptyView {

    private static final long serialVersionUID = 1L;

}
