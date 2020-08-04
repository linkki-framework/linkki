package org.linkki.samples.playground.bugs.lin1917;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class TriangleRowPmo implements HierarchicalRowPmo<TriangleRowPmo> {

    private TreeNode node;
    private SimpleItemSupplier<TriangleRowPmo, TreeNode> rowSupplier;

    public TriangleRowPmo(TreeNode node) {
        this.node = node;
        rowSupplier = new SimpleItemSupplier<>(node::getChildren, TriangleRowPmo::new);
    }

    @UITableColumn(expandRatio = 1)
    @UITextField(label = "Value", position = 10)
    @BindTooltip(tooltipType = TooltipType.DYNAMIC)
    public String getValue() {
        return node.getValue();
    }

    public String getValueTooltip() {
        return node.getValue();
    }

    @ModelObject
    public TreeNode getModelObject() {
        return node;
    }

    @Override
    public List<? extends TriangleRowPmo> getChildRows() {
        return rowSupplier.get();
    }

    public void addNewChild() {
        String text = String.valueOf(node.getChildren().size());
        node.addChild(new TreeNode(text));
    }

}
