/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component.sidebar;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.ui.CssLayout;

/**
 * Component consisting of a sidebar and a content area.
 * <p>
 * Elements must be added with one of the {@code addSheets} methods or
 * {@link #addSheet(SidebarSheet)} method. The first added sheet is selected by default, but another
 * can be chosen via {@link #select(SidebarSheet)}.
 * <p>
 * The {@link SidebarLayout} can be styled with the {@link LinkkiStyles#SIDEBAR_LAYOUT},
 * {@link LinkkiStyles#SIDEBAR} and {@link LinkkiStyles#SIDEBAR_CONTENT} style classes. Selected
 * buttons in the sidebar are additionally styled with {@link LinkkiStyles#SIDEBAR_SELECTED}.
 */
public class SidebarLayout extends CssLayout {

    private static final long serialVersionUID = -8283594476427895723L;

    private final CssLayout sidebar;

    private final CssLayout contentArea;

    private Optional<SidebarSheet> selected = Optional.empty();

    public SidebarLayout() {
        this.sidebar = new CssLayout();
        this.contentArea = new CssLayout();

        // must be set for the splitpanels inside to zoom correctly
        setSizeFull();
        contentArea.setSizeFull();

        setStyleName(LinkkiStyles.SIDEBAR_LAYOUT);
        sidebar.setStyleName(LinkkiStyles.SIDEBAR);
        contentArea.setStyleName(LinkkiStyles.SIDEBAR_CONTENT);

        addComponent(sidebar);
        addComponent(contentArea);
    }

    public void addSheets(Stream<SidebarSheet> sheets) {
        sheets.forEach(this::addSheet);
    }

    public void addSheets(Iterable<SidebarSheet> sheets) {
        sheets.forEach(this::addSheet);
    }

    public void addSheets(SidebarSheet... sheets) {
        addSheets(Arrays.stream(sheets));
    }

    public void addSheet(SidebarSheet sheet) {
        sheet.unselect();
        sheet.getButton().addClickListener(e -> select(sheet));

        sidebar.addComponent(sheet.getButton());
        contentArea.addComponent(sheet.getContent());

        if (!selected.isPresent()) {
            select(sheet);
        }
    }

    /**
     * Selects the given sheet.
     * 
     * @param sheet a sheet contained in this layout
     */
    public void select(SidebarSheet sheet) {
        selected.ifPresent(SidebarSheet::unselect);
        this.selected = Optional.of(sheet);
        sheet.select();
    }

    /**
     * Returns the selected {@link SidebarSheet}. If the {@link SidebarLayout} is instantiated with
     * any sheets there must always be a selected one.
     * 
     * @return The selected {@link SidebarSheet}
     * 
     * @throws NoSuchElementException if there is no {@link SidebarSheet} at all.
     */
    public SidebarSheet getSelected() {
        return selected.get();
    }

}
