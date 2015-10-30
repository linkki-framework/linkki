/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button;

import de.faktorzehn.ipm.web.ButtonPmo;
import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.ButtonPmoBinding;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcherFactory;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;

public class PmoBasedTableSectionFactory<T extends PresentationModelObject> {

    private static final PropertyDispatcherFactory DISPATCHER_FACTORY = new PropertyDispatcherFactory();

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
        Table table = createTable();
        Optional<ButtonPmo> addItemPmo = containerPmo.getAddItemButtonPmo();
        TableSection<T> section = createEmptySection(addItemPmo);
        section.setTable(table);
        return section;
    }

    private Table createTable() {
        PmoBasedTableFactory<T> tableFactory = new PmoBasedTableFactory<>(containerPmo, bindingContext,
                this::createPropertyDispatcher);
        return tableFactory.createTable();
    }

    private TableSection<T> createEmptySection(Optional<ButtonPmo> addItemButtonPmo) {
        UISection sectionDefinition = containerPmo.getClass().getAnnotation(UISection.class);
        checkNotNull(sectionDefinition, "PMO " + containerPmo.getClass() + " must be annotated with @UISection!");
        return new TableSection<>(sectionDefinition.caption(), createAddItemButton(addItemButtonPmo));
    }

    private Optional<Button> createAddItemButton(Optional<ButtonPmo> buttonPmo) {
        return buttonPmo.map(b -> ButtonPmoBinding.createBoundButton(bindingContext, b, createPropertyDispatcher(b)));
    }

    /**
     * Creates a new {@link PropertyDispatcher} chain for the given PMO.
     */
    protected PropertyDispatcher createPropertyDispatcher(final Object pmo) {
        return DISPATCHER_FACTORY.defaultDispatcherChain(pmo, propertyBehaviorProvider);
    }

}
