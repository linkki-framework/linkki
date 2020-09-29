package org.linkki.samples.playground.bugs.lin1917;

import java.util.Arrays;
import java.util.Optional;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.pmo.ButtonPmoBuilder;
import org.linkki.core.ui.table.pmo.SelectableTablePmo;
import org.linkki.util.handler.Handler;

@UISection(caption = "LIN-1917 :: Triangle symbol should appear when adding an item")
public class TriangleTablePmo
        extends SimpleTablePmo<TreeNode, TriangleRowPmo>
        implements SelectableTablePmo<TriangleRowPmo> {

    private static final String ROOT_TEXT = "Select this row and press the add button to create a new child";
    private static final int PAGE_LENGTH = 15;
    private Optional<TriangleRowPmo> selectedRow = Optional.empty();

    private Handler addChildHandler;

    public TriangleTablePmo() {
        super(Arrays.asList(new TreeNode(ROOT_TEXT)));
        addChildHandler = () -> {
            selectedRow.ifPresent(selection -> selection.addNewChild());
        };
    }

    @Override
    public int getPageLength() {
        return PAGE_LENGTH;
    }

    @Override
    protected TriangleRowPmo createRow(TreeNode modelObject) {
        return new TriangleRowPmo(modelObject);
    }

    @Override
    public TriangleRowPmo getSelection() {
        return selectedRow.orElse(null);
    }

    @Override
    public void setSelection(TriangleRowPmo selectedRow) {
        this.selectedRow = Optional.of(selectedRow);

    }

    @Override
    public void onDoubleClick() {
        // do nothing
    }

    @Override
    public Optional<ButtonPmo> getAddItemButtonPmo() {
        return Optional.of(ButtonPmoBuilder.newAddButton(addChildHandler));
    }


}
