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

package org.linkki.core.ui.creation.table;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;

/**
 * Wraps a vaadin {@link Grid}.
 * 
 * @param <ROW> a class annotated with linkki annotations used as PMO for a row in the table
 */
public class GridComponentWrapper<ROW> extends AbstractGridComponentWrapper<ROW> {

    private static final long serialVersionUID = 1L;

    private final List<ROW> items = new ArrayList<>();

    public GridComponentWrapper(String id, Grid<ROW> grid) {
        super(id, grid);
        grid.setDataProvider(new ListDataProvider<>(items));
    }

    /**
     * Sets the items displayed in the {@link Grid} by updating the items in the data provider if the
     * given items are not the ones currently displayed.
     */
    @Override
    public void setItems(List<ROW> newItems) {
        if (hasItemListChanged(newItems)) {
            items.clear();
            items.addAll(newItems);
            getComponent().getDataProvider().refreshAll();
        }
    }

    private boolean hasItemListChanged(List<ROW> newItems) {
        return !this.items.equals(newItems);
    }

}
