/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public abstract class TabSheetArea extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private TabSheet tabsheet = new TabSheet();

    public TabSheetArea() {
        super();
        addComponent(tabsheet);
    }

    public TabSheetArea(Component... children) {
        super(children);
    }

    /**
     * Adds the component as a tab (with the given caption) in this TabSheet.
     * 
     * @param child
     * @param caption
     */
    protected void addTab(Component child, String caption) {
        tabsheet.addTab(child, caption);
    }

    /**
     * Adds the given {@link TabSheetArea} as a tab (with the given caption) in this TabSheet and
     * lets it create its contents.
     * 
     * @param area
     * @param caption
     */
    protected void addTab(TabSheetArea area, String caption) {
        addTab((Component)area, caption);
        area.createContent();
    }

    /**
     * Creates the content (children) of this tab sheet.
     */
    public abstract void createContent();

}