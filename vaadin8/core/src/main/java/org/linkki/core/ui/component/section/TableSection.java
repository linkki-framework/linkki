/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.ui.component.section;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;

import com.vaadin.ui.Button;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * A section containing a single table. This kind of section is created by the
 * {@link PmoBasedSectionFactory} if the presentation model object is a {@link ContainerPmo}.
 */
@SuppressWarnings("deprecation")
public class TableSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    @Nullable
    private com.vaadin.v7.ui.Table table;

    /***
     * @deprecated since 2019-04-09; Use {@link TableSection#TableSection(String, boolean)} instead, as
     *             the {@code addItemButton} should be added by the {@link LinkkiLayoutDefinition} via
     *             {@link #addHeaderButton(Button)}.
     */
    @Deprecated
    public TableSection(String caption, boolean closeable, Optional<Button> addItemButton) {
        super(caption, closeable, addItemButton);
    }

    public TableSection(String caption, boolean closeable) {
        super(caption, closeable);
    }

    /**
     * Returns the table shown in the section.
     */
    public com.vaadin.v7.ui.Table getTable() {
        return table;
    }

    public void setTable(com.vaadin.v7.ui.Table table) {
        this.table = requireNonNull(table, "table must not be null");
        addComponent(table);
        setExpandRatio(table, 1f);
        table.setSizeFull();
    }

    @Override
    public com.vaadin.v7.ui.Table getSectionContent() {
        return getTable();
    }

    @Override
    public String toString() {
        return "TableSection based on " + (table == null ? null : table.getContainerDataSource());
    }
}
