/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/
package org.linkki.samples.playground.application.custom;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.notification.Notification;

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
public class CustomMenuItemDefinitionCreator {

    public static ApplicationMenuItemDefinition createMenuItem(String name, List<MySubSubMenuItem> subSubMenuItems) {
        return new ApplicationMenuItemDefinition(name, Collections.singletonList(createSubMenuItem(subSubMenuItems)));
    }

    private static ApplicationMenuItemDefinition createSubMenuItem(List<MySubSubMenuItem> subSubMenuItems) {
        String subMenuName = "New";
        if (subSubMenuItems.size() > 1) {
            return new ApplicationMenuItemDefinition(subMenuName,
                    subSubMenuItems.stream()
                            .map(i -> new ApplicationMenuItemDefinition(i.getName(), () -> i.getCommand().apply()))
                            .collect(Collectors.toList()));
        } else if (subSubMenuItems.size() == 1) {
            MySubSubMenuItem onlyItem = subSubMenuItems.get(0);
            return new ApplicationMenuItemDefinition(onlyItem.getName(), onlyItem.getCommand());
        } else {
            return new ApplicationMenuItemDefinition(subMenuName, Handler.NOP_HANDLER);
        }
    }

    public static class MySubSubMenuItem {
        private String name;

        public MySubSubMenuItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Handler getCommand() {
            return () -> Notification.show(name);
        }
    }
}
// end::class[]
