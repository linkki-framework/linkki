/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinding;
import org.linkki.core.ui.section.annotations.UISection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * A factory to create sections showing a single table based on a PMO.
 */
public class PmoBasedTableSectionFactory<T> {

    private final ContainerPmo<T> containerPmo;
    private final BindingContext bindingContext;

    /**
     * Creates a new factory.
     */
    public PmoBasedTableSectionFactory(@Nonnull ContainerPmo<T> containerPmo, @Nonnull BindingContext bindingContext) {
        this.containerPmo = requireNonNull(containerPmo);
        this.bindingContext = requireNonNull(bindingContext);
    }

    /**
     * Creates a new section showing a table, table structure and content is defined by the
     * factory's {@link ContainerPmo}.
     */
    @Nonnull
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
        return new TableSection<>(sectionDefinition.caption(), sectionDefinition.closeable(),
                createAddItemButton(addItemButtonPmo));
    }

    private Optional<Button> createAddItemButton(Optional<ButtonPmo> buttonPmo) {
        return buttonPmo.map(b -> ButtonPmoBinding.createBoundButton(bindingContext, b));
    }

}
