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
package org.linkki.core.vaadin.component.area;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.linkki.core.vaadin.component.page.Page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

/**
 * A base class for all areas that use a {@link Tabs} containing multiple pages.
 * 
 * By default, the constructor uses {@link #setSizeFull()} to ensure that the tabs are fixed while
 * scrolling.
 *
 * Warning: Calling {@link #setSizeFull()} in a page which is added as tab would break the layout of the
 * according page! Setting any defined height creates the same effect.
 *
 * If the area is created via injection framework, the {@link #init()} method is called automatically
 * and ensures that the {@link #createContent()} method is called.
 * 
 * Note: If the area is not injected you need to call {@link #init()} manually!
 */
public abstract class TabSheetArea extends Tabs implements Area {

    private static final long serialVersionUID = 1L;

    public TabSheetArea() {
        this(true);
    }

    /**
     * Note that the preserveHeader behavior only works if subclasses do <em>not</em> call
     * {@link #setSizeUndefined()} in their constructor and added {@link Page}s do not set a defined
     * height, e.g. by using {@link #setHeight(String)} or {@link #setSizeFull()}.
     * 
     * @param preserveHeader Determines the scroll behavior. If <code>true</code> only the contents of
     *            each tab are scrolled. The tab headers themselves always remain visible. If
     *            <code>false</code> everything is scrolled, including the tab headers. This means tab
     *            headers can be scrolled out of view with this setting.
     */
    public TabSheetArea(boolean preserveHeader) {
        super();
        // TODO LIN-2065
        // addSelectedTabChangeListener(e -> reloadBindings());
        if (preserveHeader) {
            setSizeFull();
        }
    }

    /**
     * Creates the actual UI. This cannot be done in the constructor, because clients can provide
     * subclasses with custom pages that are not available in this super-class. In order to be able to
     * create a UI, the initialization must be performed <em>after</em> constructors, subclass
     * constructors and dependency injection (constructor and field injection). Hence a separate
     * init-method. It is annotated as post-construct so the DI framework can call it automatically.
     * 
     * Must be called manually if no dependency injection framework is used.
     */
    @PostConstruct
    public final void init() {
        createContent();
    }

    /**
     * Removes the tab that contains the given component.
     * 
     * @param tabPage the component contained in the tab to remove
     */
    protected void removeTab(Tab tabPage) {
        remove(tabPage);
    }

    /**
     * Returns the tab pages that are contained in the tabs of this TabSheet.
     * 
     * @return the tab pages that are contained in the tabs of this TabSheet.
     */
    protected List<Tab> getTabs() {
        return getChildren()
                .filter(c -> c instanceof Tab)
                .map(c -> (Tab)c)
                .collect(Collectors.toList());
    }

    @Override
    public void reloadBindings() {
        Component selectedTab = getSelectedTab();
        if (selectedTab != null && selectedTab instanceof Page) {
            ((Page)selectedTab).reloadBindings();
        }
    }

}