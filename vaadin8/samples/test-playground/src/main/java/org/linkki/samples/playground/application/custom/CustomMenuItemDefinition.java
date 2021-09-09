/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/
package org.linkki.samples.playground.application.custom;

import java.util.List;

import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

/**
 * Custom menu item definition to test out the API for more complex items.
 * <p>
 * Single item:
 * 
 * <pre>
 * Name
 * |
 * v
 * New
 * </pre>
 * 
 * Multiple items:
 * 
 * <pre>
 * Name
 * |
 * v
 * New - Item1
 *       Item2
 * </pre>
 *
 */
// tag::class[]
public class CustomMenuItemDefinition extends ApplicationMenuItemDefinition {

    private List<MySubSubMenuItem> subSubMenuItems;

    public CustomMenuItemDefinition(String name, int position, List<MySubSubMenuItem> subSubMenuItems) {
        super(name, position);
        this.subSubMenuItems = subSubMenuItems;
    }

    private List<MySubSubMenuItem> getSubSubMenuItems() {
        return subSubMenuItems;
    }

    @Override
    protected MenuItem internalCreateItem(ApplicationMenu menu) {
        MenuItem newApplicationMenuItem = menu.addItem(getName(), null, null);

        MenuItem newMenuItem = newApplicationMenuItem.addItem("New", null);

        if (getSubSubMenuItems().size() > 1) {
            getSubSubMenuItems().forEach(i -> newMenuItem.addItem(i.getName(), i.getCommand()));
        } else if (getSubSubMenuItems().size() == 1) {
            MySubSubMenuItem onlyItem = getSubSubMenuItems().get(0);
            newMenuItem.addItem(onlyItem.getName(), onlyItem.getCommand());
        }
        return newApplicationMenuItem;
    }

    public static class MySubSubMenuItem {
        private String name;

        public MySubSubMenuItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Command getCommand() {
            return i -> Notification.show(name);
        }
    }
}
// end::class[]