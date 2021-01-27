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
package org.linkki.core.vaadin.component.tablayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.shared.Registration;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Layout component containing multiple {@link LinkkiTabSheet tab sheets}.
 * <p>
 * The component can be created with a vertical or horizontal tab bar layout.
 * <p>
 * {@link LinkkiTabSheet Tab sheets} can be added to this component with the
 * {@link #addTab(LinkkiTabSheet, int)} and {@link #addTab(LinkkiTabSheet)} methods. The
 * {@link LinkkiTabSheet tab sheets} added to it can be selected with the {@link #setSelectedIndex(int)}
 * or {@link #setSelectedTab(LinkkiTabSheet)} methods. The first added sheet will be automatically
 * selected, firing a {@link SelectedChangeEvent}. {@link #removeTab(LinkkiTabSheet) Removing} the
 * selected sheet from the component changes the selection to the next available tab sheet.
 */
public class LinkkiTabLayout extends Composite<Component> {

    private static final long serialVersionUID = 1L;

    private final Tabs tabsComponent;
    private final HasComponents rootComponent;

    private final Map<Tab, LinkkiTabSheet> tabSheets = new HashMap<>();

    /**
     * Constructs a tab layout with {@link Orientation#HORIZONTAL horizontal} orientation.
     */
    public LinkkiTabLayout() {
        this(Orientation.HORIZONTAL);
    }

    /**
     * Constructs a tab layout with the given orientation.
     * <p>
     * When choosing {@link Orientation#HORIZONTAL}, the tabs are placed above the content. In the
     * {@link Orientation#VERTICAL} orientation, the tabs are placed on the left of the content.
     */
    public LinkkiTabLayout(Orientation orientation) {
        tabsComponent = new Tabs();

        if (orientation == Orientation.HORIZONTAL) {
            VerticalLayout layout = new VerticalLayout();
            layout.setPadding(false);
            tabsComponent.setWidthFull();
            rootComponent = layout;
        } else {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setPadding(false);
            tabsComponent.setHeightFull();
            rootComponent = layout;
        }

        tabsComponent.setOrientation(orientation);
        tabsComponent.addSelectedChangeListener(e -> {
            Optional.ofNullable(e.getPreviousTab()).ifPresent(this::unselect);
            Optional.ofNullable(e.getSelectedTab()).ifPresent(this::select);
        });

        rootComponent.add(tabsComponent);
    }

    private void unselect(Tab tab) {
        Component content = tabSheets.get(tab).getContent();
        content.setVisible(false);
    }

    private void select(Tab tab) {
        Component content = tabSheets.get(tab).getContent();
        if (!content.getParent().isPresent()) {
            rootComponent.add(content);
        }
        content.setVisible(true);
    }

    @Override
    protected Component initContent() {
        return (Component)rootComponent;
    }

    /**
     * @see Tabs#addSelectedChangeListener(ComponentEventListener)
     */
    public Registration addSelectedTabChangeListener(ComponentEventListener<SelectedChangeEvent> listener) {
        return tabsComponent.addSelectedChangeListener(listener);
    }

    /**
     * Inserts a new sheet at the given index.
     * 
     * @param tabSheet the {@link LinkkiTabSheet} to be added
     * @param index the index where the tab is inserted
     */
    public void addTab(LinkkiTabSheet tabSheet, int index) {
        tabSheets.put(tabSheet.getTab(), tabSheet);
        tabsComponent.addComponentAtIndex(index, tabSheet.getTab());
    }

    /**
     * Inserts a new sheet at the end.
     * 
     * @param tabSheet the {@link LinkkiTabSheet} to be added
     */
    public void addTab(LinkkiTabSheet tabSheet) {
        addTab(tabSheet, tabsComponent.getComponentCount());
    }


    /**
     * Removes a tab from the tab layout. If the removed tab was selected, the next available tab is
     * selected.
     * 
     * @param tabSheet tab sheet to be removed
     */
    public void removeTab(LinkkiTabSheet tabSheet) {
        Tab tab = tabSheet.getTab();
        tabsComponent.remove(tab);
        rootComponent.remove(tabSheets.get(tab).getContent());
        tabSheets.remove(tab);
    }

    /**
     * Removes all tabs from the tab layout.
     */
    public void removeAllTabs() {
        tabsComponent.removeAll();

        tabSheets.values().forEach(tabSheet -> rootComponent.remove(tabSheet.getContent()));
        tabSheets.clear();
    }

    /**
     * @see Tabs#setSelectedIndex(int)
     */
    public void setSelectedIndex(int index) {
        tabsComponent.setSelectedIndex(index);
    }

    /**
     * @see Tabs#getSelectedIndex()
     */
    public int getSelectedIndex() {
        return tabsComponent.getSelectedIndex();
    }

    /**
     * Selects the given tab sheet. The content of the tab sheet will be created is this is the first
     * time the tab sheet is selected.
     *
     * @param selectedTab the tab sheet to select, {@code null} to unselect all
     * @throws IllegalArgumentException if {@code selectedTab} is not a child of this component
     */
    /**
     * @see Tabs#setSelectedTab(Tab)
     */
    public void setSelectedTab(@CheckForNull LinkkiTabSheet tabSheet) {
        tabsComponent.setSelectedTab(tabSheet == null ? null : tabSheet.getTab());
    }

    /**
     * Gets the currently selected tab sheet.
     *
     * @return the selected tab sheet, or {@code null} if none is selected
     */
    @CheckForNull
    public LinkkiTabSheet getSelectedTabSheet() {
        return Optional.ofNullable(tabsComponent.getSelectedTab()).map(tabSheets::get).orElse(null);
    }

    /**
     * Gets the tab sheet with the given ID
     * 
     * @param id ID of the tab sheet
     * @return tab sheet with the given id or {@link Optional#empty()} if none of the tab sheets has the
     *         given ID.
     */
    public Optional<LinkkiTabSheet> getTabSheet(String id) {
        return tabSheets.keySet().stream()
                .filter(t -> t.getId().get().contentEquals(id))
                .findFirst()
                .map(tabSheets::get);
    }

    /**
     * Returns the {@link Tabs} component belonging to this tab layout.
     */
    public Tabs getTabsComponent() {
        return tabsComponent;
    }
}