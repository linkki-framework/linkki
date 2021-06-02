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

package org.linkki.samples.playground.bugs.lin1917;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
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

    @UITableColumn(flexGrow = 1)
    @UITextField(label = "Value", position = 10)
    @BindTooltip
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
