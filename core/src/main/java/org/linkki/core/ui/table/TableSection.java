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

import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.PmoBasedSectionFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * A section containing a single table. This kind of section is created by the
 * {@link PmoBasedSectionFactory} if the presentation model object is a {@link ContainerPmo}.
 */
public class TableSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    private final Table table;

    public TableSection(String caption, boolean closeable, Optional<Button> addItemButton, Table table) {
        super(caption, closeable, addItemButton);
        this.table = requireNonNull(table, "table must not be null");
        addComponent(table);
        setExpandRatio(table, 1f);
        table.setSizeFull();
    }

    /**
     * Returns the table shown in the section.
     */
    public Table getTable() {
        return getSectionContent();
    }

    @Override
    public String toString() {
        return "TableSection based on " + table.getContainerDataSource();
    }

    @Override
    public Table getSectionContent() {
        return table;
    }
}
