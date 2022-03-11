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

    public static ApplicationMenuItemDefinition createMenuItem(String name,
            String id,
            List<MySubSubMenuItem> subSubMenuItems) {
        return new ApplicationMenuItemDefinition(name, id,
                Collections.singletonList(createSubMenuItem(subSubMenuItems)));
    }

    private static ApplicationMenuItemDefinition createSubMenuItem(List<MySubSubMenuItem> subSubMenuItems) {
        String subMenuName = "New";
        if (subSubMenuItems.size() > 1) {
            return new ApplicationMenuItemDefinition(subMenuName, "new",
                    subSubMenuItems.stream()
                            .map(i -> new ApplicationMenuItemDefinition(i.getName(), i.getId(),
                                    () -> i.getCommand().apply()))
                            .collect(Collectors.toList()));
        } else if (subSubMenuItems.size() == 1) {
            MySubSubMenuItem onlyItem = subSubMenuItems.get(0);
            return new ApplicationMenuItemDefinition(onlyItem.getName(), onlyItem.getId(), onlyItem.getCommand());
        } else {
            return new ApplicationMenuItemDefinition(subMenuName, "new", Handler.NOP_HANDLER);
        }
    }

    public static class MySubSubMenuItem {
        private String name;
        private String id;

        public MySubSubMenuItem(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public Handler getCommand() {
            return () -> Notification.show(name);
        }
    }
}
// end::class[]
