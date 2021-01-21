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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
 * Layout component containing multiple tabs that are identified by the {@link Tab} component combined
 * with the component contained on the tab (see {@link #getContent(Tab)}).
 * <p>
 * The component can be created with a vertical or horizontal tab bar layout.
 * <p>
 * {@link Tab Tabs} can be added to this component with the
 * {@link #addTab(String, Component, Component, int)} methods. The {@link Tab tabs} added to it can be
 * selected with the {@link #setSelectedIndex(int)} or {@link #setSelectedTab(Tab)} methods. The first
 * added {@link Tab} component will be automatically selected, firing a {@link SelectedChangeEvent}.
 * {@link #removeTab(Tab) Removing} the selected tab from the component changes the selection to the
 * next available tab.
 */
public class LinkkiTabLayout extends Composite<Component> {

    private static final long serialVersionUID = 1L;

    private final HasComponents rootComponent;

    private final Tabs tabs = new Tabs();

    private final Map<Tab, Component> tabComponents = new HashMap<>();

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
        if (orientation == Orientation.HORIZONTAL) {
            VerticalLayout layout = new VerticalLayout();
            layout.setPadding(false);
            tabs.setWidthFull();
            rootComponent = layout;
        } else {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setPadding(false);
            tabs.setHeightFull();
            rootComponent = layout;
        }

        tabs.setOrientation(orientation);
        tabs.addSelectedChangeListener(e -> {
            Optional.ofNullable(e.getPreviousTab())
                    .ifPresent(t -> tabComponents.get(t).setVisible(false));
            Optional.ofNullable(e.getSelectedTab())
                    .ifPresent(t -> tabComponents.get(t).setVisible(true));
        });

        rootComponent.add(tabs);
    }

    @Override
    protected Component initContent() {
        return (Component)rootComponent;
    }

    /**
     * @see Tabs#addSelectedChangeListener(ComponentEventListener)
     */
    public Registration addSelectedTabChangeListener(ComponentEventListener<SelectedChangeEvent> listener) {
        return tabs.addSelectedChangeListener(listener);
    }

    /**
     * Creates a new tab and appends it to the tab layout.
     * 
     * @param id the {@link Tab#getId() ID} of the tab component
     * @param caption the {@link Tab#getLabel() caption} of the tab
     * @param tabContent the component displayed when the tab is active
     * @return the created tab
     */
    public Tab addTab(String id, String caption, Component tabContent) {
        return addTab(id, caption, tabContent, tabs.getComponentCount());
    }

    /**
     * Creates a new tab and inserts it at the given index.
     * 
     * @param id the {@link Tab#getId() ID} of the tab component
     * @param caption the {@link Tab#getLabel() caption} of the tab
     * @param tabContent the component displayed when the tab is active
     * @param index the index where the tab is inserted
     * @return the created tab
     */
    public Tab addTab(String id, String caption, Component tabContent, int index) {
        Tab tab = new Tab(caption);
        tabComponents.put(tab, tabContent);
        tab.setId(id);
        tabs.addComponentAtIndex(index, tab);

        if (tabs.getSelectedTab() != tab) {
            // only hide content if tab is not currently displayed
            tabContent.setVisible(false);
        }

        rootComponent.add(tabContent);
        return tab;
    }

    /**
     * Creates a new tab and appends it to the tab layout.
     * 
     * @param id the {@link Tab#getId() ID} of the tab component
     * @param tabCaption the component used as the tab caption
     * @param tabContent the component displayed when the tab is active
     * @return the created tab
     */
    public Tab addTab(String id, Component tabCaption, Component tabContent) {
        return addTab(id, tabCaption, tabContent, tabs.getComponentCount());
    }

    /**
     * Creates a new tab and appends it to the tab layout.
     * 
     * @param id the {@link Tab#getId() ID} of the tab component
     * @param tabCaption the component used as the tab caption
     * @param tabContent the component displayed when the tab is active
     * @param index the index where the tab is inserted
     * @return the created tab
     */
    public Tab addTab(String id, Component tabCaption, Component tabContent, int index) {
        Tab tab = addTab(id, "", tabContent, index);
        tab.add(tabCaption);
        return tab;
    }

    /**
     * Removes a tab from the tab layout. If the removed tab was selected, the next available tab is
     * selected.
     */
    public void removeTab(Tab tab) {
        tabs.remove(tab);

        Component content = tabComponents.remove(tab);
        rootComponent.remove(content);
    }

    /**
     * Removes all tabs from the tab layout.
     */
    public void removeAllTabs() {
        tabs.removeAll();

        for (Component c : tabComponents.values()) {
            rootComponent.remove(c);
        }
        tabComponents.clear();
    }

    /**
     * Returns a list of all tabs belonging to this tab layout.
     */
    public List<Tab> getTabs() {
        return tabs.getChildren()
                .filter(c -> c instanceof Tab)
                .map(c -> (Tab)c)
                .collect(Collectors.toList());
    }

    public Component getContent(Tab tab) {
        if (tabComponents.containsKey(tab)) {
            return tabComponents.get(tab);
        } else {
            throw new IllegalArgumentException("todo");
        }
    }

    /**
     * @see Tabs#setSelectedIndex(int)
     */
    public void setSelectedIndex(int index) {
        tabs.setSelectedIndex(index);
    }

    /**
     * @see Tabs#getSelectedIndex()
     */
    public int getSelectedIndex() {
        return tabs.getSelectedIndex();
    }

    /**
     * @see Tabs#setSelectedTab(Tab)
     */
    public void setSelectedTab(@CheckForNull Tab tab) {
        tabs.setSelectedTab(tab);
    }

    /**
     * @see Tabs#getSelectedTab()
     */
    @CheckForNull
    public Tab getSelectedTab() {
        return tabs.getSelectedTab();
    }

    /**
     * Returns the {@link Tabs} component belonging to this tab layout.
     */
    public Tabs getTabsComponent() {
        return tabs;
    }
}