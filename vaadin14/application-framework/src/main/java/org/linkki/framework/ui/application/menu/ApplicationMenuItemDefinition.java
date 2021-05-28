/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.framework.ui.application.menu;

import static java.util.Objects.requireNonNull;

import org.linkki.framework.ui.application.ApplicationHeader;

import com.vaadin.flow.component.contextmenu.MenuItem;

/**
 * Defines an item in the application's main menu.
 * 
 * @see ApplicationMenu
 * @see ApplicationHeader
 */
public abstract class ApplicationMenuItemDefinition implements Comparable<ApplicationMenuItemDefinition> {

    private final String name;
    private final int position;

    public ApplicationMenuItemDefinition(String name, int position) {
        this.name = requireNonNull(name, "name must not be null");
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public MenuItem createItem(ApplicationMenu menu) {
        MenuItem item = internalCreateItem(menu);
        // TODO LIN-2207 Styling
        // item.setStyleName(LinkkiApplicationTheme.APPLICATION_MENU);
        return item;
    }

    protected abstract MenuItem internalCreateItem(ApplicationMenu menu);

    @Override
    public int compareTo(ApplicationMenuItemDefinition other) {
        requireNonNull(other, "other must not be null");
        int positionCompare = this.position - other.position;
        if (positionCompare == 0) {
            return this.name.compareTo(other.name);
        } else {
            return positionCompare;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + position;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApplicationMenuItemDefinition other = (ApplicationMenuItemDefinition)obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (position != other.position) {
            return false;
        }
        return true;
    }

}
