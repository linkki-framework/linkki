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

package org.linkki.samples.playground.table;

import java.time.LocalDate;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "Table with sortable columns")
public class SortableTablePmo implements ContainerPmo<SortableRowPmo> {

    private List<SortableRowPmo> rows;

    public SortableTablePmo() {
        rows = List.of(new SortableRowPmo("abc", 3, LocalDate.of(2020, 5, 3)),
                       new SortableRowPmo("abc", 7, LocalDate.of(2007, 12, 9)),
                       new SortableRowPmo("xyz", 5, LocalDate.of(2024, 1, 7)),
                       new SortableRowPmo("defg", 2, LocalDate.of(1985, 3, 9)));
    }

    @Override
    public List<SortableRowPmo> getItems() {
        return rows;
    }

}
