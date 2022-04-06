package org.linkki.samples.playground.table.collapsible;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

import java.util.List;

@UISection
public class CollapsibleColumnTablePmo implements ContainerPmo<CollapsibleColumnTablePmo.CollapsibleColumnRowPmo> {

    @Override
    public List<CollapsibleColumnRowPmo> getItems() {
        return List.of(new CollapsibleColumnRowPmo(), new CollapsibleColumnRowPmo());
    }

    public static class CollapsibleColumnRowPmo {

        @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE)
        @UILabel(position = 10, label = "Collapsible")
        public String getCollapsible() {
            return "cell";
        }

        @UITableColumn(collapsible = CollapseMode.INITIALLY_COLLAPSED)
        @UILabel(position = 20, label = "Initially collapsed")
        public String getInitiallyCollapsed() {
            return "cell";
        }

        @UITableColumn(collapsible = CollapseMode.NOT_COLLAPSIBLE)
        @UILabel(position = 30, label = "Not collapsible")
        public String getNotCollapsible() {
            return "cell";
        }
    }
}
