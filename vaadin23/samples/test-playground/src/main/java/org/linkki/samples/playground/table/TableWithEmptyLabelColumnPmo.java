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

import java.util.Arrays;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.samples.playground.table.TableWithEmptyLabelColumnPmo.EmptyLabelColumnRowPmo;

@UISection(caption = "Table with empty label columns")
public class TableWithEmptyLabelColumnPmo implements ContainerPmo<EmptyLabelColumnRowPmo> {

    @Override
    public List<EmptyLabelColumnRowPmo> getItems() {
        return Arrays
                .asList(new EmptyLabelColumnRowPmo(), new EmptyLabelColumnRowPmo(), new EmptyLabelColumnRowPmo(),
                        new EmptyLabelColumnRowPmo(), new EmptyLabelColumnRowPmo(), new EmptyLabelColumnRowPmo());
    }

    @Override
    public int getPageLength() {
        return 5;
    }

    public static class EmptyLabelColumnRowPmo {

        @UITableColumn(width = 200)
        @UILabel(position = 10, label = "1")
        public String getColumn1() {
            return "";
        }

        @UITableColumn(width = 25)
        @UILabel(position = 20, label = "2")
        public String getColumn2() {
            return "";
        }
    }
}
