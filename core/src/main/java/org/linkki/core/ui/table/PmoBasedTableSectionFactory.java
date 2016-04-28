/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinding;
import org.linkki.core.ui.section.annotations.UISection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class PmoBasedTableSectionFactory<T extends PresentationModelObject> {

    private ContainerPmo<T> containerPmo;
    private BindingContext bindingContext;

    public PmoBasedTableSectionFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext) {
        this.containerPmo = containerPmo;
        this.bindingContext = bindingContext;
    }

    public TableSection<T> createSection() {
        Table table = createTable();
        Optional<ButtonPmo> addItemPmo = containerPmo.getAddItemButtonPmo();
        TableSection<T> section = createEmptySection(addItemPmo);
        section.setTable(table);
        return section;
    }

    private Table createTable() {
        PmoBasedTableFactory<T> tableFactory = new PmoBasedTableFactory<>(containerPmo, bindingContext);
        return tableFactory.createTable();
    }

    private TableSection<T> createEmptySection(Optional<ButtonPmo> addItemButtonPmo) {
        UISection sectionDefinition = containerPmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + containerPmo.getClass() + " must be annotated with @UISection!");
        return new TableSection<>(sectionDefinition.caption(), createAddItemButton(addItemButtonPmo));
    }

    private Optional<Button> createAddItemButton(Optional<ButtonPmo> buttonPmo) {
        return buttonPmo.map(b -> ButtonPmoBinding.createBoundButton(bindingContext, b));
    }

}
