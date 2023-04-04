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

package org.linkki.samples.playground.table.columnwidth;

import java.util.Collections;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.samples.playground.table.columnwidth.ColumnWidthTablePmo.ColumnWidthRowPmo;

@UISection(caption = "Table with UITableColumn Annotation using width and flexGrow")
public class ColumnWidthTablePmo implements ContainerPmo<ColumnWidthRowPmo> {

    @Override
    public List<ColumnWidthRowPmo> getItems() {
        return Collections.singletonList(new ColumnWidthRowPmo());
    }

    @Override
    public int getPageLength() {
        return 1;
    }

    public static class ColumnWidthRowPmo {

        @UITableColumn(width = 50)
        @UILabel(position = 10, label = "1")
        public String getColumnWithWidth() {
            return "50px";
        }

        @UITableColumn(flexGrow = 3)
        @UILabel(position = 20, label = "2")
        public String getColumnWithFlexGrow() {
            return "flex grow 3";
        }

        @UILabel(position = 30, label = "3")
        public String getColumnWithNoTableColumnAnnotation1() {
            return "No UITableColumn";
        }

        @UILabel(position = 40, label = "4")
        public String getColumnWithNoTableColumnAnnotation2() {
            return "No UITableColumn";
        }

        @UITableColumn(width = 200, flexGrow = 1)
        @UILabel(position = 50, label = "5")
        public String getColumnWithWidthAndFlexGrow() {
            return "200px & flex grow 1";
        }
    }
}
