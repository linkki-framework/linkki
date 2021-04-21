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
package org.linkki.framework.ui.component.sidebar;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.linkki.framework.ui.LinkkiApplicationTheme;

import com.vaadin.event.SerializableEventListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.util.ReflectTools;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Component consisting of a sidebar and a content area.
 * 
 * @implSpec Elements must be added with one of the {@code addSheets} methods or
 *           {@link #addSheet(SidebarSheet)} method. The first added sheet is selected by default, but
 *           another can be chosen via {@link #select(SidebarSheet)}.
 *           <p>
 *           The {@link SidebarLayout} can be styled with the
 *           {@link LinkkiApplicationTheme#SIDEBAR_LAYOUT}, {@link LinkkiApplicationTheme#SIDEBAR} and
 *           {@link LinkkiApplicationTheme#SIDEBAR_CONTENT} style classes. Selected buttons in the
 *           sidebar are additionally styled with {@link LinkkiApplicationTheme#SIDEBAR_SELECTED}.
 */
public class SidebarLayout extends CssLayout {

    private static final long serialVersionUID = -8283594476427895723L;

    private static final Method SELECTION_CHANGE_METHOD = ReflectTools.findMethod(SelectionListener.class,
                                                                                  "selectionChange",
                                                                                  SidebarSelectionEvent.class);

    private final CssLayout sidebar;

    private final CssLayout contentArea;

    private final List<SidebarSheet> sidebarSheets = new ArrayList<>();

    @CheckForNull
    private SidebarSheet selected;

    public SidebarLayout() {
        this.sidebar = new CssLayout();
        this.contentArea = new CssLayout();

        // must be set for the splitpanels inside to zoom correctly
        setSizeFull();
        contentArea.setSizeFull();

        setStyleName(LinkkiApplicationTheme.SIDEBAR_LAYOUT);
        sidebar.setStyleName(LinkkiApplicationTheme.SIDEBAR);
        contentArea.setStyleName(LinkkiApplicationTheme.SIDEBAR_CONTENT);

        addComponent(sidebar);
        addComponent(contentArea);
    }

    public void addSheets(Stream<SidebarSheet> sheets) {
        sheets.forEach(this::addSheet);
    }

    public void addSheets(Iterable<SidebarSheet> sheets) {
        sheets.forEach(this::addSheet);
    }

    public void addSheets(@NonNull SidebarSheet... sheets) {
        addSheets(Arrays.stream(sheets));
    }

    public void addSheet(SidebarSheet sheet) {
        sidebarSheets.add(sheet);
        sheet.getButton().addClickListener(e -> selectInternal(sheet, true));
        sidebar.addComponent(sheet.getButton());

        if (selected == null) {
            select(sheet);
        }
    }

    /**
     * Adds a selection listener to this {@link SidebarLayout}. The listener is called when the
     * selection is changed either by the user or programmatically.
     *
     * @param listener The selection listener, not null
     * @return a registration for the listener
     */
    public Registration addSelectionListener(SelectionListener listener) {
        return addListener(SidebarSelectionEvent.class, listener, SELECTION_CHANGE_METHOD);
    }

    /**
     * Selects the sheet with the given ID.
     * <p>
     * The method does nothing if there is no sheet with the specified ID.
     * 
     * @param id The ID of a sheet contained in this layout
     */
    public void select(String id) {
        getSidebarSheet(id).ifPresent(this::select);
    }

    /**
     * Selects the given sheet.
     * 
     * @param sheet A sheet contained in this layout
     */
    public void select(SidebarSheet sheet) {
        selectInternal(sheet, false);
    }

    void selectInternal(SidebarSheet sheet, boolean userOriginated) {
        if (!sidebarSheets.contains(sheet)) {
            throw new IllegalArgumentException("The sheet " + sheet + " is not part of this SidebarLayout");
        }
        if (selected == sheet) {
            return;
        }
        Component content = sheet.getContent();
        if (content.getParent() == null) {
            contentArea.addComponent(content);
        } else if (content.getParent() != contentArea) {
            throw new IllegalStateException("Content of the selected sidebar sheet already has a parent!");
        }

        SidebarSheet oldValue = selected;
        if (oldValue != null) {
            oldValue.unselect();
        }

        this.selected = sheet;
        sheet.select();
        fireEvent(new SidebarSelectionEvent(this, selected, oldValue, userOriginated));
    }

    /**
     * Returns the selected {@link SidebarSheet}. If the {@link SidebarLayout} is instantiated with any
     * sheets there must always be a selected one.
     * 
     * @return The selected {@link SidebarSheet}.
     * 
     * @throws NoSuchElementException if there is no {@link SidebarSheet} at all.
     */
    public SidebarSheet getSelected() {
        if (selected != null) {
            return selected;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Return all registered {@link SidebarSheet}.
     * 
     * @return The list of available {@link SidebarSheet}s in the order they were added
     */
    public List<SidebarSheet> getSidebarSheets() {
        return Collections.unmodifiableList(sidebarSheets);
    }

    /**
     * Returns the sidebar sheet with the specified ID if it exists.
     * 
     * @param id The ID of the requested sheet
     * @return the requested sheet identified by the specified ID
     */
    public Optional<SidebarSheet> getSidebarSheet(String id) {
        return sidebarSheets.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    /**
     * For testing purposes only.
     */
    /* private */ CssLayout getContentArea() {
        return contentArea;
    }

    /**
     * For testing purposes only.
     */
    /* private */ CssLayout getSidebar() {
        return sidebar;
    }

    /**
     * A listener that could be registered in the {@link SidebarLayout} to get notified when an other
     * {@link SidebarSheet} gets selected.
     */
    @FunctionalInterface
    public interface SelectionListener extends SerializableEventListener {

        void selectionChange(SidebarSelectionEvent e);

    }

    /**
     * A selection change event that gets triggered by {@link SelectionListener}.
     */
    public static class SidebarSelectionEvent extends EventObject implements SelectionEvent<SidebarSheet> {

        private static final long serialVersionUID = 1L;

        private final SidebarSheet selected;

        @CheckForNull
        private final SidebarSheet oldValue;

        private final boolean userOriginated;

        public SidebarSelectionEvent(SidebarLayout source, SidebarSheet selected,
                @CheckForNull SidebarSheet oldValue,
                boolean userOriginated) {
            super(source);
            this.selected = selected;
            this.oldValue = oldValue;
            this.userOriginated = userOriginated;
        }

        @Override
        public boolean isUserOriginated() {
            return userOriginated;
        }

        public SidebarSheet getSelectedSheet() {
            return selected;
        }

        @Override
        public Optional<SidebarSheet> getFirstSelectedItem() {
            return Optional.ofNullable(selected);
        }

        @Override
        public Set<SidebarSheet> getAllSelectedItems() {
            Optional<SidebarSheet> selectedItem = getFirstSelectedItem();
            if (selectedItem.isPresent()) {
                return Collections.singleton(selectedItem.get());
            } else {
                return Collections.emptySet();
            }
        }

        @CheckForNull
        public SidebarSheet getOldValue() {
            return oldValue;
        }

    }

}
