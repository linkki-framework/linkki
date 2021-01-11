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
package org.linkki.core.vaadin.component.section;

import static java.util.Objects.requireNonNull;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;

import com.vaadin.flow.component.grid.Grid;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * A section containing a single table. This kind of section is created by the
 * {@link PmoBasedSectionFactory} if the presentation model object is a {@link ContainerPmo}.
 */
public class GridSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    @Nullable
    private Grid<?> grid;

    public GridSection(String caption, boolean closeable) {
        super(caption, closeable);
    }

    /**
     * Returns the table shown in the section.
     */
    public Grid<?> getGrid() {
        return grid;
    }

    public void setGrid(Grid<?> grid) {
        this.grid = requireNonNull(grid, "grid must not be null");
        add(grid);
        setFlexGrow(1, grid);
    }

    @Override
    public Grid<?> getSectionContent() {
        return getGrid();
    }

    @Override
    public String toString() {
        return "GridSection based on " + (grid == null ? null : grid.getId());
    }
}
