/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import com.vaadin.server.Resource;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;

public class PmoBasedTableSectionFactory<T extends PresentationModelObject> {

    private ContainerPmo<T> containerPmo;
    private BindingContext bindingContext;
    private PropertyBehaviorProvider propertyBehaviorProvider;

    public PmoBasedTableSectionFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext,
            PropertyBehaviorProvider propertyBehaviorProvider) {

        this.containerPmo = containerPmo;
        this.bindingContext = bindingContext;
        this.propertyBehaviorProvider = propertyBehaviorProvider;
    }

    public TableSection<T> createSection() {
        TableSection<T> section = createEmptySection();
        PmoBasedTable<T> table = createTable();
        section.setTable(table);
        return section;
    }

    private PmoBasedTable<T> createTable() {
        PmoBasedTableFactory<T> tableFactory = new PmoBasedTableFactory<>(containerPmo, bindingContext,
                propertyBehaviorProvider);
        return tableFactory.createTable();
    }

    /** Returns the icon to display for the add item button. */
    private Resource addItemIcon() {
        UITable tableAnnotation = containerPmo.getClass().getAnnotation(UITable.class);
        if (tableAnnotation != null) {
            return tableAnnotation.addItemIcon();
        } else {
            return UITable.DEFAULT_ADD_ITEM_ICON;
        }
    }

    // package private for testing
    TableSection<T> createEmptySection() {
        UISection sectionDefinition = containerPmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + containerPmo.getClass() + " must be annotated with @UISection!");
        return new TableSection<>(sectionDefinition.caption(), containerPmo.addItemAction(), addItemIcon());
    }

}
