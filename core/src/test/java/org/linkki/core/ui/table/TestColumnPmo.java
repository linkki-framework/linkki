package org.linkki.core.ui.table;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UITextField;

public class TestColumnPmo implements PresentationModelObject {

    private final Object modelObject = new Object();

    private final TestContainerPmo parent;

    public TestColumnPmo(TestContainerPmo parent) {
        super();
        this.parent = parent;
    }

    @UITableColumn(width = 100)
    @UITextField(position = 0, label = "1")
    public String getValue1() {
        return "1";
    }

    @UITableColumn(expandRation = 2.0f)
    @UITextField(position = 1, label = "2")
    public String getValue2() {
        return "2";
    }

    @UITextField(position = 2, label = "3")
    public String getValue3() {
        return "3";
    }

    @UIButton(position = 3)
    public void delete() {
        parent.deleteItem(this);
    }

    @Override
    public Object getModelObject() {
        return modelObject;
    }

}