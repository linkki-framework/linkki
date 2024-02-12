/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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

    private CustomMenuItemDefinitionCreator() {
        throw new IllegalStateException("Utility class");
    }

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
