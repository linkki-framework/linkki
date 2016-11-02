/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.application;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.cdi.internal.Conventions;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * Subclass of the origin vaadin navigator to fix a cdi problem when switching to the same view.
 * 
 * Switching to the same view does not clear the view scope
 * (https://github.com/vaadin/cdi/issues/166). To get a clean view scope we first navigate to the
 * {@link EmptyCdiView} before navigating to the correct new view.
 *
 */
public class CdiFixNavigator extends Navigator {

    private static final long serialVersionUID = 1L;

    public CdiFixNavigator(UI ui, ComponentContainer container) {
        super(ui, container);
    }

    @Override
    public void navigateTo(String navigationState) {
        String currentFragment = getState();
        if (currentFragment != null && getViewName(navigationState).equals(getViewName(currentFragment))) {
            super.navigateTo(Conventions.deriveMappingForView(EmptyCdiView.class));
        }
        super.navigateTo(navigationState);
    }

    String getViewName(String fragment) {
        return StringUtils.substringBefore(fragment, "/");
    }
}
