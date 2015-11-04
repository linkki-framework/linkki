package org.linkki.framework.ui.application.menu;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.framework.ui.application.ApplicationHeader;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.MenuBar;

@UIScoped
public class ApplicationMenu extends MenuBar {

    private static final long serialVersionUID = 1L;

    @Inject
    private Instance<ApplicationMenuItemDefinition> itemDefs;

    public void init(ApplicationHeader header) {
        addStyleName(ApplicationStyles.BORDERLESS);
        setSizeUndefined();
        SortedSet<ApplicationMenuItemDefinition> sorted = new TreeSet<ApplicationMenuItemDefinition>();
        itemDefs.forEach(sorted::add);
        sorted.forEach(item -> item.createItem(this, header.getApplicationLayout()));
        header.addComponent(this);
    }

}
