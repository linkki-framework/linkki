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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Layout component containing multiple {@link LinkkiTabSheet tab sheets}.
 * <p>
 * The component can be created with a vertical or horizontal tab bar layout.
 * <p>
 * The {@link LinkkiTabSheet tab sheets} added to it can be selected with the
 * {@link #setSelectedIndex(int)} or {@link #setSelectedTabSheet(String)} methods. The first added sheet
 * will be automatically selected, firing a {@link SelectedChangeEvent}.
 * {@link #removeTabSheet(LinkkiTabSheet) Removing} the selected sheet from the component changes the
 * selection to the next available tab sheet.
 */
public class LinkkiTabLayout extends Composite<Component> {

    private static final long serialVersionUID = 1L;

    private final Tabs tabsComponent;
    private final HasComponents rootComponent;

    private final Map<Tab, LinkkiTabSheet> tabSheets = new LinkedHashMap<>();

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
        LinkkiTabSheet selectedTabSheet = tabSheets.get(tab);
        selectedTabSheet.getOnSelectionHandler().apply();
        Component content = selectedTabSheet.getContent();
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
    public Registration addSelectedChangeListener(ComponentEventListener<SelectedChangeEvent> listener) {
        return tabsComponent.addSelectedChangeListener(listener);
    }

    /**
     * Inserts a new sheet at the given index.
     * 
     * @param tabSheet the {@link LinkkiTabSheet} to be added
     * @param index the index where the tab is inserted
     */
    public void addTabSheet(LinkkiTabSheet tabSheet, int index) {
        tabSheets.put(tabSheet.getTab(), tabSheet);
        tabsComponent.addComponentAtIndex(index, tabSheet.getTab());
    }

    /**
     * Inserts a new sheet at the end.
     * 
     * @param tabSheet the {@link LinkkiTabSheet} to be added
     */
    public void addTabSheet(LinkkiTabSheet tabSheet) {
        addTabSheet(tabSheet, tabsComponent.getComponentCount());
    }

    /**
     * @see #addTabSheet(LinkkiTabSheet)
     */
    public void addTabSheets(Stream<LinkkiTabSheet> sheets) {
        sheets.forEach(this::addTabSheet);
    }

    /**
     * @see #addTabSheet(LinkkiTabSheet)
     */
    public void addTabSheets(Iterable<LinkkiTabSheet> sheets) {
        sheets.forEach(this::addTabSheet);
    }

    /**
     * @see #addTabSheet(LinkkiTabSheet)
     */
    public void addTabSheets(@NonNull LinkkiTabSheet... sheets) {
        addTabSheets(Arrays.stream(sheets));
    }

    /**
     * Removes a tab from the tab layout. If the removed tab was selected, the next available tab is
     * selected.
     * 
     * @param tabSheet tab sheet to be removed
     */
    public void removeTabSheet(LinkkiTabSheet tabSheet) {
        Tab tab = tabSheet.getTab();
        tabsComponent.remove(tab);
        rootComponent.remove(tabSheets.get(tab).getContent());
        tabSheets.remove(tab);
    }

    /**
     * Removes all tabs from the tab layout.
     */
    public void removeAllTabSheets() {
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
     * Selects the tab sheet with the given ID. The content of the tab sheet will be created is this is
     * the first time the tab sheet is selected.
     *
     * @param id the ID of the tab sheet to select
     * @throws IllegalArgumentException if none of the tab sheets has the given ID
     */
    public void setSelectedTabSheet(String id) {
        tabsComponent.setSelectedTab(getTabSheet(id)
                .orElseThrow(() -> new IllegalArgumentException("None of the tab sheets has the id " + id)).getTab());
    }

    /**
     * Gets the currently selected tab sheet.
     *
     * @return the selected tab sheet
     * @throws NoSuchElementException if there is no selected tab. This can only happen if no tab sheet
     *             as been added yet, or all tab sheets was deleted/unselected.
     */
    public LinkkiTabSheet getSelectedTabSheet() {
        return Optional.ofNullable(tabsComponent.getSelectedTab()).map(tabSheets::get)
                .orElseThrow(() -> new NoSuchElementException("No selected tab"));
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
     * Returns all sheets in displayed order.
     * 
     * @return all {@link LinkkiTabSheet tab sheets}
     */
    public List<LinkkiTabSheet> getTabSheets() {
        return getTabsComponent().getChildren()
                .filter(Tab.class::isInstance)
                .map(tabSheets::get)
                .collect(Collectors.toList());
    }

    /**
     * Returns the {@link Tabs} component belonging to this tab layout.
     */
    public Tabs getTabsComponent() {
        return tabsComponent;
    }
}