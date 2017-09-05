/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.util.handler.Handler;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * A layout consisting of a sidebar and a content area in which {@link SidebarTabSheets} are
 * displayed. The {@link SidebarTabSheet#getIcon() icon} of a {@link SidebarTabSheet} is added to
 * the sidebar as a navigator element. The selected {@link SidebarTabSheet#getContent() component}
 * is then shown in the content area.
 */
public abstract class SidebarComposite extends CssLayout {

    private static final long serialVersionUID = 4896991400908887512L;

    private CssLayout sidebar;
    private CssLayout layout;

    private SidebarTabSheets sidebarTabSheets;

    public SidebarComposite() {

        this.sidebar = new CssLayout();
        this.layout = new CssLayout();

        sidebar.setStyleName(LinkkiStyles.SIDEBAR);
        layout.setStyleName(LinkkiStyles.SIDEBAR_CONTENT);

        setStyleName(LinkkiStyles.SIDEBAR_COMPOSITE);
        addComponent(sidebar);
        addComponent(layout);

        this.sidebarTabSheets = SidebarTabSheets.with();
    }

    public abstract SidebarTabSheets getTabSheets();

    public void initUI() {
        sidebarTabSheets = getTabSheets();
        sidebarTabSheets.forEach(this::addSheet);
        sidebarTabSheets.getSelected().ifPresent(this::select);
    }

    private void addSheet(SidebarTabSheet tabSheet) {
        tabSheet.getIcon().addClickListener(e -> select(tabSheet));

        if (StringUtils.isNotEmpty(tabSheet.getToolTip())) {
            tabSheet.getIcon().setDescription(tabSheet.getToolTip());
        }

        sidebar.addComponent(tabSheet.getIcon());
        layout.addComponent(tabSheet.getContent());

        tabSheet.initUI();
    }

    private void select(SidebarTabSheet tabSheet) {
        sidebarTabSheets.getSelected().ifPresent(this::unselect);

        tabSheet.getContent().removeStyleName(LinkkiStyles.SIDEBAR_CONTENT_HIDDEN);
        tabSheet.getIcon().addStyleName(LinkkiStyles.SIDEBAR_SELECTED);
        sidebarTabSheets.select(tabSheet);
    }

    private void unselect(SidebarTabSheet tabSheet) {
        tabSheet.getIcon().removeStyleName(LinkkiStyles.SIDEBAR_SELECTED);
        tabSheet.getContent().addStyleName(LinkkiStyles.SIDEBAR_CONTENT_HIDDEN);
    }

    /**
     * A list of {@link SidebarTabSheet SidebarTabsheets} with a selected item. Also provides
     * utility methods to create a {@link SidebarTabSheets}.
     */
    public static class SidebarTabSheets implements Iterable<SidebarTabSheet> {

        private List<SidebarTabSheet> tabSheets = new ArrayList<>();
        private Optional<SidebarTabSheet> selected = Optional.empty();

        private SidebarTabSheets(List<SidebarTabSheet> tabSheets) {
            this.tabSheets = tabSheets;
            if (!tabSheets.isEmpty()) {
                selected = Optional.of(tabSheets.get(0));
            }
        }

        /**
         * Creates a new {@link SidebarTabSheets} with the given {@link SidebarTabSheet
         * SidebarTabsheets}.
         */
        public static SidebarTabSheets with(SidebarTabSheet... tabSheets) {
            return new SidebarTabSheets(new ArrayList<>(Arrays.asList(tabSheets)));
        }

        /**
         * Add a component to the end of this list of {@link SidebarTabSheet SidebarTabsheets}.
         * 
         * @return this {@link SidebarTabSheets} to enable method chaining
         */
        public SidebarTabSheets and(SidebarTabSheet tabSheet) {
            tabSheets.add(tabSheet);
            return this;
        }

        public Optional<SidebarTabSheet> getSelected() {
            return selected;
        }

        public void select(SidebarTabSheet tabSheet) {
            selected = Optional.ofNullable(tabSheet);
        }

        @Override
        public Iterator<SidebarTabSheet> iterator() {
            return tabSheets.iterator();
        }
    }

    /**
     * Wrapper class for a component that is displayed in a {@link SidebarComposite}. A
     * {@link SidebarTabSheet} consists of an icon button, a {@link Component} that contains the
     * content and a {@link Handler} that is called when the UI is initialized.
     */
    public static class SidebarTabSheet {

        private Button icon;
        private Component content;
        private String tooltip;
        private Handler initUI;

        public SidebarTabSheet(Resource icon, Component content, String tooltip, Handler initUI) {
            this.icon = new Button("", icon); //$NON-NLS-1$
            this.content = content;
            this.tooltip = tooltip;
            this.initUI = initUI;
        }

        public Button getIcon() {
            return icon;
        }

        public Component getContent() {
            return content;
        }

        public String getToolTip() {
            return tooltip;
        }

        public void initUI() {
            initUI.apply();
        }
    }
}
