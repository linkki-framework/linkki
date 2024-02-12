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
package org.linkki.core.vaadin.component.tablayout;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet.LinkkiTabSheetBuilder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.shared.Registration;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Layout component containing multiple {@link LinkkiTabSheet tab sheets}.
 * <p>
 * The component can be created with a vertical or horizontal tab bar layout.
 * <p>
 * The {@link LinkkiTabSheet tab sheets} added to it can be selected with the
 * {@link #setSelectedIndex(int)} or {@link #setSelectedTabSheet(String)} methods. The first added
 * sheet will be automatically selected, firing a {@link SelectedChangeEvent}.
 * {@link #removeTabSheet(LinkkiTabSheet) Removing} the selected sheet from the component changes
 * the selection to the next available tab sheet.
 */
@Tag("linkki-tab-layout")
@JsModule("./src/linkki-tab-layout.ts")
@CssImport("./styles/linkki-tab-layout.css")
public class LinkkiTabLayout extends HtmlComponent implements AfterNavigationObserver {

    public static final String THEME_VARIANT_SOLID = "solid";
    static final String PROPERTY_ORIENTATION = "orientation";

    private static final long serialVersionUID = 1L;

    private final Tabs tabsComponent;
    private final Div contentWrapper;

    private final Map<Tab, LinkkiTabSheet> tabSheets = new LinkedHashMap<>();

    /**
     * Constructs a tab layout with a {@link Orientation#HORIZONTAL horizontal} tab component.
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
        tabsComponent.setOrientation(orientation);
        tabsComponent.addSelectedChangeListener(e -> {
            Optional.ofNullable(e.getPreviousTab()).ifPresent(this::unselect);
            Optional.ofNullable(e.getSelectedTab()).ifPresent(t -> select(t, e));
        });
        tabsComponent.getElement().setAttribute("slot", "tabs");

        contentWrapper = new Div();
        contentWrapper.getElement().setAttribute("slot", "content");

        getElement().setAttribute(PROPERTY_ORIENTATION, orientation.name().toLowerCase());
        getElement().appendChild(tabsComponent.getElement(), contentWrapper.getElement());

        setSizeFull();
    }

    private void unselect(Tab tab) {
        tabSheets.get(tab).unselect();
    }

    private void select(Tab tab, SelectedChangeEvent e) {
        tabSheets.get(tab).select(contentWrapper, e);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateSheetVisibility();
    }

    /**
     * Updates the visibility of all tab sheets according to the value returned by their
     * {@link LinkkiTabSheetBuilder#visibleWhen(java.util.function.BooleanSupplier) visibility
     * supplier}. If the currently selected tab is not visible anymore, the first visible tab is
     * selected.
     * <p>
     * Automatically called during {@link #afterNavigation(AfterNavigationEvent)}.
     */
    public void updateSheetVisibility() {
        tabSheets.entrySet().forEach(e -> e.getKey().setVisible(e.getValue().isVisible()));
        updateSelectionAfterVisibleChange();
    }

    private void updateSelectionAfterVisibleChange() {
        Tab selectedTab = getTabsComponent().getSelectedTab();

        if (selectedTab != null && !tabSheets.get(selectedTab).isVisible()) {
            // selected tab is not visible -> select first visible tab
            tabSheets.keySet().stream()
                    .filter(Tab::isVisible)
                    .findFirst()
                    .ifPresentOrElse(tab -> getTabsComponent().setSelectedTab(tab),
                                     () -> getTabsComponent().setSelectedTab(null));
        }
    }

    /**
     * @see Tabs#addSelectedChangeListener(ComponentEventListener)
     */
    public Registration addSelectedChangeListener(ComponentEventListener<SelectedChangeEvent> listener) {
        return getTabsComponent().addSelectedChangeListener(listener);
    }

    /**
     * Inserts a new sheet at the given index.
     * 
     * @param tabSheet the {@link LinkkiTabSheet} to be added
     * @param index the index where the tab is inserted
     */
    public void addTabSheet(LinkkiTabSheet tabSheet, int index) {
        tabSheets.put(tabSheet.getTab(), tabSheet);
        getTabsComponent().addTabAtIndex(index, tabSheet.getTab());
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
        getTabsComponent().remove(tab);
        if (tabSheet.isContentLoaded()) {
            contentWrapper.remove(tabSheets.get(tab).getContent());
        }
        tabSheets.remove(tab);
    }

    /**
     * Removes all tabs from the tab layout.
     */
    public void removeAllTabSheets() {
        getTabsComponent().removeAll();

        tabSheets.values().forEach(tabSheet -> contentWrapper.removeAll());
        tabSheets.clear();
    }

    /**
     * @see Tabs#setSelectedIndex(int)
     */
    public void setSelectedIndex(int index) {
        getTabsComponent().setSelectedIndex(index);
    }

    /**
     * @see Tabs#getSelectedIndex()
     */
    public int getSelectedIndex() {
        return getTabsComponent().getSelectedIndex();
    }

    /**
     * Selects the tab sheet with the given ID. The content of the tab sheet will be created is this
     * is the first time the tab sheet is selected.
     *
     * @param id the ID of the tab sheet to select
     * @throws IllegalArgumentException if none of the tab sheets has the given ID
     */
    public void setSelectedTabSheet(String id) {
        getTabsComponent().setSelectedTab(getTabSheet(id)
                .orElseThrow(() -> new IllegalArgumentException("None of the tab sheets has the id " + id)).getTab());
    }

    /**
     * Gets the currently selected tab sheet.
     *
     * @return the selected tab sheet
     * @throws NoSuchElementException if there is no selected tab. This can only happen if no tab
     *             sheet as been added yet, or all tab sheets was deleted/unselected.
     */
    public LinkkiTabSheet getSelectedTabSheet() {
        return Optional.ofNullable(tabsComponent.getSelectedTab()).map(tabSheets::get)
                .orElseThrow(() -> new NoSuchElementException("No selected tab"));
    }

    /**
     * Gets the tab sheet with the given ID
     * 
     * @param id ID of the tab sheet
     * @return tab sheet with the given id or {@link Optional#empty()} if none of the tab sheets has
     *         the given ID.
     */
    public Optional<LinkkiTabSheet> getTabSheet(String id) {
        return tabSheets.keySet().stream()
                .filter(t -> t.getId().get().equals(id))
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

    /**
     * Returns the wrapper for the content of the sheet.
     */
    public Component getContent() {
        return contentWrapper;
    }

    /**
     * Creates a new vertical {@link LinkkiTabLayout} with a solid tab bar on the left. This is
     * equivalent to adding the theme {@value #THEME_VARIANT_SOLID} to a vertical
     * {@link LinkkiTabLayout}.
     */
    public static LinkkiTabLayout newSidebarLayout() {
        LinkkiTabLayout sidebarLayout = new LinkkiTabLayout(Orientation.VERTICAL);
        sidebarLayout.getElement().getThemeList().add(THEME_VARIANT_SOLID);
        return sidebarLayout;
    }

}