package org.linkki.samples.playground.bugs.lin1890;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class Lin1890HierarchicalRowPmo implements HierarchicalRowPmo<Lin1890HierarchicalRowPmo> {

    private StringTreeNode node;
    private SimpleItemSupplier<Lin1890HierarchicalRowPmo, StringTreeNode> rowSupplier;
    private long objectCallCount;
    private static long classCallCount;

    public Lin1890HierarchicalRowPmo(StringTreeNode node) {
        this.node = node;
        rowSupplier = new SimpleItemSupplier<Lin1890HierarchicalRowPmo, StringTreeNode>(
                () -> node.getChildren(), Lin1890HierarchicalRowPmo::new);
    }

    @UITableColumn(expandRatio = 1)
    @UITextField(label = "Value", position = 10)
    @BindTooltip(tooltipType = TooltipType.DYNAMIC)
    public String getValue() {
        return node.getValue();
    }

    public String getValueTooltip() {
        objectCallCount++;
        classCallCount++;
        String tooltip = "getValueTooltip() called " + classCallCount
                + " times (" + objectCallCount + " times for object " + node + "#" + node.hashCode() + ")";
        return tooltip;
    }

    @ModelObject
    public StringTreeNode getModelObject() {
        return node;
    }

    @Override
    public List<? extends Lin1890HierarchicalRowPmo> getChildRows() {
        return rowSupplier.get();
    }

}
