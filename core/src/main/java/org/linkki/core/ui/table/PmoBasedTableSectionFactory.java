/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.ui.table;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonPmoBinding;
import org.linkki.core.nls.pmo.PmoLabelType;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.adapters.UISectionDefinition;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * A factory to create sections showing a single table based on a PMO.
 */
public class PmoBasedTableSectionFactory<@NonNull T> {

    private PmoNlsService pmoNlsService;

    private final ContainerPmo<T> containerPmo;
    private final BindingContext bindingContext;

    /**
     * Creates a new factory.
     */
    public PmoBasedTableSectionFactory(ContainerPmo<T> containerPmo, BindingContext bindingContext) {
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
        pmoNlsService = PmoNlsService.get();
    }

    /**
     * Creates a new section showing a table, table structure and content is defined by the factory's
     * {@link ContainerPmo}.
     */
    public TableSection<T> createSection() {
        Table table = createTable();
        Optional<ButtonPmo> addItemPmo = containerPmo.getAddItemButtonPmo();
        TableSection<T> section = createTableSection(table, addItemPmo);
        return section;
    }

    private Table createTable() {
        PmoBasedTableFactory<T> tableFactory = new PmoBasedTableFactory<>(containerPmo, bindingContext);
        return tableFactory.createTable();
    }

    private TableSection<T> createTableSection(Table table, Optional<ButtonPmo> addItemButtonPmo) {
        UISection sectionDefinition = UISectionDefinition.from(containerPmo);

        String caption = pmoNlsService.getLabel(PmoLabelType.SECTION_CAPTION, containerPmo.getClass(), null,
                                                sectionDefinition.caption());

        return new TableSection<>(caption, sectionDefinition.closeable(), createAddItemButton(addItemButtonPmo), table);
    }

    private Optional<Button> createAddItemButton(Optional<ButtonPmo> buttonPmo) {
        return buttonPmo.map(b -> ButtonPmoBinding.createBoundButton(bindingContext, b));
    }
}
