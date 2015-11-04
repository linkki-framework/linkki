/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

import de.faktorzehn.ipm.web.ui.page.AbstractPage;

public abstract class TabSheetArea extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private TabSheet tabSheet = new TabSheet();

    private final Map<Component, Tab> componentTabs = Maps.newLinkedHashMap();

    public TabSheetArea() {
        super();
        addComponent(tabSheet);
    }

    /**
     * Adds a new tab with the given caption containing the given component in this TabSheet.
     * 
     * @param component the new tab's content
     * @param caption the new tab's caption
     * @return the new tab
     */
    protected Tab addTab(Component component, String caption) {
        Tab tab = tabSheet.addTab(component, caption);
        componentTabs.put(component, tab);
        return tab;
    }

    /**
     * Adds a new tab with the given caption containing the given {@link TabSheetArea} in this
     * TabSheet and lets the area create its contents.
     * 
     * @param area the new tab's content
     * @param caption the new tab's caption
     * @return the new tab
     */
    protected Tab addTab(TabSheetArea area, String caption) {
        Tab tab = addTab((Component)area, caption);
        area.createContent();
        return tab;
    }

    /**
     * Adds a new tab with the given caption containing the given {@link AbstractPage} in this
     * TabSheet and lets the page refresh its contents.
     * 
     * @param page the new tab's content
     * @param caption the new tab's caption
     * @return the new tab
     */
    protected Tab addTab(AbstractPage page, String caption) {
        Tab tab = addTab((Component)page, caption);
        page.reloadBindings();
        return tab;
    }

    /**
     * Removes the tab that contains the given component.
     * 
     * @param component the component contained in the tab to remove
     */
    protected void removeTab(Component component) {
        Tab toRemove = componentTabs.remove(component);
        tabSheet.removeTab(toRemove);
    }

    /**
     * Returns the components of the given type that are contained in one of the tabs of this
     * TabSheet.
     * 
     * @param type the type of the components
     * @return the components of the given type that are contained in one of the tabs of this
     *         TabSheet.
     */
    protected <T extends Component> List<T> getTabContents(Class<T> type) {
        // @formatter:off
        return componentTabs.keySet().stream()
                .filter(comp -> type.isInstance(comp))
                .map(type::cast)
                .collect(Collectors.toList());
        // @formatter:on
    }

    /** Creates the content (children) of this tab sheet. */
    public abstract void createContent();

    /**
     * Updates the content (children) of this tab sheet. If the children are rendered dynamically,
     * children may be added or removed. The content of the children has to be refreshed.
     */
    public abstract void updateContent();

}