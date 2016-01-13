/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.area;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.linkki.core.ui.page.Page;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * A base class for all areas that use a {@link TabSheet} containing multiple pages.
 *
 * If the page is created via injection framework, the {@link #init()} method is called
 * automatically and ensures that the {@link #createContent()} method is called.
 * 
 * Note: If the page is not injected you need to call {@link #init()} manually!
 * 
 * @author dirmeier
 */
public abstract class TabSheetArea extends VerticalLayout implements Area {

    private static final long serialVersionUID = 1L;

    private TabSheet tabSheet = new TabSheet();

    public TabSheetArea() {
        super();
        addComponent(tabSheet);
        tabSheet.addSelectedTabChangeListener(e -> getSelectedTab().reloadBindings());
    }

    @PostConstruct
    public final void init() {
        createContent();
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    /**
     * Adds a new tab with the given caption containing the given tab page in this TabSheet.
     * 
     * @param tabPage the new tab's content
     * @param caption the new tab's caption
     * @return the new tab
     */
    protected Tab addTab(Page tabPage, String caption) {
        return tabSheet.addTab(tabPage, caption);
    }

    /**
     * Removes the tab that contains the given component.
     * 
     * @param tabPage the component contained in the tab to remove
     */
    protected void removeTab(Page tabPage) {
        tabSheet.removeComponent(tabPage);
    }

    /**
     * Returns the tab pages that are contained in the tabs of this TabSheet.
     * 
     * @return the tab pages that are contained in the tabs of this TabSheet.
     */
    protected List<Page> getTabs() {
        // @formatter:off
        Iterable<Component> iterable = () -> tabSheet.iterator();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(c -> c instanceof Page)
                .map(c -> (Page)c)
                .collect(Collectors.toList());
        // @formatter:on
    }

    /**
     * Returns the tab pages that are contained in the tabs of this TabSheet.
     * 
     * @return the tab pages that are contained in the tabs of this TabSheet.
     */
    protected Page getSelectedTab() {
        Component selectedTab = tabSheet.getSelectedTab();
        if (selectedTab instanceof Page) {
            return (Page)selectedTab;
        } else {
            return null;
        }
    }

    @Override
    public void reloadBindings() {
        Optional.ofNullable(getSelectedTab()).ifPresent(Page::reloadBindings);
    }

}