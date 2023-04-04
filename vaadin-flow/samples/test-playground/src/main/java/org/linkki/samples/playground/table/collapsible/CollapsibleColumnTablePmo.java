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

package org.linkki.samples.playground.table.collapsible;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

@UISection(caption = "Table with collapsible columns")
public class CollapsibleColumnTablePmo implements ContainerPmo<CollapsibleColumnTablePmo.CollapsibleColumnRowPmo> {

    @Override
    public List<CollapsibleColumnRowPmo> getItems() {
        return List.of(new CollapsibleColumnRowPmo(), new CollapsibleColumnRowPmo());
    }

    @Override
    public int getPageLength() {
        return 0;
    }

    public static class CollapsibleColumnRowPmo {

        @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE, flexGrow = 1)
        @UILabel(position = 10, label = "Collapsible")
        public String getCollapsible() {
            return "cell";
        }

        @UITableColumn(collapsible = CollapseMode.INITIALLY_COLLAPSED, flexGrow = 1)
        @UILabel(position = 20, label = "Initially collapsed")
        public String getInitiallyCollapsed() {
            return "cell";
        }

        @UITableColumn(collapsible = CollapseMode.NOT_COLLAPSIBLE, flexGrow = 1)
        @UILabel(position = 30, label = "Not collapsible")
        public String getNotCollapsible() {
            return "cell";
        }

        @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE, flexGrow = 1)
        @UILabel(position = 40, label = "Programatically collapsed")
        public String getProgramaticallyCollapsed() {
            return "Programatically collapsed";
        }
    }
}
