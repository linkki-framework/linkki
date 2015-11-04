package org.linkki.framework.ui.application;

import javax.inject.Inject;

import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.framework.provider.SecurityContextProvider;
import org.linkki.framework.ui.application.menu.ApplicationMenu;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Header-Section of the application containing the application menu and information about the user.
 */
@UIScoped
public class ApplicationHeader extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    @Inject
    private ApplicationMenu applicationMenu;

    @Inject
    private SecurityContextProvider securityCtxProvider;

    private ApplicationLayout applicationLayout;

    public ApplicationHeader() {
    }

    protected void init(ApplicationLayout parent) {
        this.applicationLayout = parent;
        addStyleName(ApplicationStyles.APPLICATION_HEADER);
        setMargin(new MarginInfo(true, false, true, false));
        applicationMenu.init(this);

        HorizontalLayout menuWrapper = new HorizontalLayout();
        menuWrapper.addStyleName(ApplicationStyles.APPLICATION_MENU);
        addComponent(menuWrapper);

        createHelpSection(menuWrapper);

        ComponentFactory.addHorizontalFixedSizeSpacer(menuWrapper, 20);

        createUserSection(menuWrapper);

        setExpandRatio(menuWrapper, 1);
    }

    public ApplicationLayout getApplicationLayout() {
        return applicationLayout;
    }

    private void createHelpSection(HorizontalLayout menuWrapper) {
        MenuBar menuBar = new MenuBar();
        menuWrapper.addComponent(menuBar);
        menuBar.setSizeUndefined();
        menuBar.addStyleName(ApplicationStyles.BORDERLESS);
        MenuItem item = menuBar.addItem("", FontAwesome.QUESTION_CIRCLE, null);
        item.setStyleName(ApplicationStyles.APPLICATION_HEADER);
        item.addItem("Hilfe", null);
        item.addItem("Tastaturkürzel", null);
    }

    private void createUserSection(HorizontalLayout menuWrapper) {

        ComponentFactory.labelIcon(menuWrapper, FontAwesome.USER);
        // ComponentFactory.addHorizontalFixedSizeSpacer(menuWrapper, 5);

        String userName = securityCtxProvider.get().getAuthentication().getName();

        Label l = ComponentFactory.sizedLabel(menuWrapper, userName);

        menuWrapper.setComponentAlignment(l, Alignment.MIDDLE_CENTER);

        MenuBar menuBar = new MenuBar();
        menuWrapper.addComponent(menuBar);
        menuBar.setSizeUndefined();
        menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem item = menuBar.addItem("", null);
        item.setStyleName(ApplicationStyles.APPLICATION_HEADER);
        item.addItem("Preferenzen", null);
        item.addSeparator();
        item.addItem("Logout", newLogoutCommand());

    }

    private Command newLogoutCommand() {
        return selectedItem -> {
            getUI().getPage().setLocation("./logout");
        };
    }

}
