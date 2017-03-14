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

import javax.annotation.CheckForNull;
import javax.annotation.PostConstruct;

import org.linkki.core.ui.page.Page;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * A base class for all areas that use a {@link TabSheet} containing multiple pages.
 * 
 * By default, the constructor uses {@link #setSizeFull()} to ensure that the tabs are fixed while
 * scrolling. Use {@link #setHeightUndefined()} or {@link #TabSheetArea(boolean)}to overwrite this
 * behavior.
 *
 * Warning: Calling {@link #setSizeFull()} in a page which is added as tab would break the layout of
 * the according page! Setting any defined height creates the same effect.
 *
 * If the area is created via injection framework, the {@link #init()} method is called
 * automatically and ensures that the {@link #createContent()} method is called.
 * 
 * Note: If the area is not injected you need to call {@link #init()} manually!
 */
public abstract class TabSheetArea extends VerticalLayout implements Area {

    private static final long serialVersionUID = 1L;

    private TabSheet tabSheet = new TabSheet();

    public TabSheetArea() {
        this(true);
    }

    /**
     * Note that the preserveHeader behavior only works if subclasses do <em>not</em> call
     * {@link #setSizeUndefined()} in their constructor and added {@link Page}s do not set a defined
     * height, e.g. by using {@link #setHeight(String)} or {@link #setSizeFull()}.
     * 
     * @param preserveHeader Determines the scroll behavior. If <code>true</code> only the contents
     *            of each tab are scrolled. The tab headers themselves always remain visible. If
     *            <code>false</code> everything is scrolled, including the tab headers. This means
     *            tab headers can be scrolled out of view with this setting.
     */
    public TabSheetArea(boolean preserveHeader) {
        super();
        addComponent(tabSheet);
        tabSheet.addSelectedTabChangeListener(e -> reloadBindings());
        if (preserveHeader) {
            tabSheet.setSizeFull();
            setSizeFull();
        }
        setMargin(new MarginInfo(true, false, false, false));
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
        Iterable<Component> iterable = () -> tabSheet.iterator();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(c -> c instanceof Page)
                .map(c -> (Page)c)
                .collect(Collectors.toList());
    }

    /**
     * Returns the tab pages that are contained in the tabs of this TabSheet.
     * 
     * @return the tab pages that are contained in the tabs of this TabSheet.
     */
    @CheckForNull
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