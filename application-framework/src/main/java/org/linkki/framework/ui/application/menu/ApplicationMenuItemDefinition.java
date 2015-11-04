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
        return this.position - other.position;
    }

}
