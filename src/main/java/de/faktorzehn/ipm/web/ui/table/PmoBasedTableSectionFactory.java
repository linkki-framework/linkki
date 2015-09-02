/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

public class PmoBasedTableSectionFactory {

    private ContainerPmo<? extends PresentationModelObject> containerPmo;
    private BindingContext bindingContext;
    private PropertyBehaviorProvider propertyBehaviorProvider;

    public PmoBasedTableSectionFactory(ContainerPmo<? extends PresentationModelObject> containerPmo,
            BindingContext bindingContext, PropertyBehaviorProvider propertyBehaviorProvider) {

        this.containerPmo = containerPmo;
        this.bindingContext = bindingContext;
        this.propertyBehaviorProvider = propertyBehaviorProvider;
    }

    public TableSection createSection() {
        TableSection section = createEmptySection();
        PmoBasedTable<PresentationModelObject> table = createTable();
        section.setTable(table);
        return section;
    }

    private PmoBasedTable<PresentationModelObject> createTable() {
        PmoBasedTableFactory tableFactory = new PmoBasedTableFactory<>(containerPmo, bindingContext,
                propertyBehaviorProvider);
        return tableFactory.createTable();
    }

    private TableSection createEmptySection() {
        TableSection section;
        UISection sectionDefinition = containerPmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + containerPmo.getClass() + " must be annotated with @UISection!");
        section = new TableSection(sectionDefinition.caption(), containerPmo.isAddItemAvailable());
        return section;
    }

}
