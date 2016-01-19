package org.linkki.framework.ui.application.menu;

import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.framework.ui.application.ApplicationLayout;

import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Defines an item in the applications main menu.
 */
public abstract class ApplicationMenuItemDefinition implements Comparable<ApplicationMenuItemDefinition> {

    private String name;
    private int position;

    public ApplicationMenuItemDefinition(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public MenuItem createItem(ApplicationMenu menu, ApplicationLayout layout) {
        MenuItem item = internalCreateItem(menu, layout);
        item.setStyleName(ApplicationStyles.APPLICATION_MENU);
        return item;
    }

    protected abstract MenuItem internalCreateItem(ApplicationMenu menu, ApplicationLayout layout);

    @Override
    public int compareTo(ApplicationMenuItemDefinition other) {
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (position != other.position) {
            return false;
        }
        return true;
    }

}
