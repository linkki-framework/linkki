package de.faktorzehn.ipm.web.ui.table;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.ui.section.annotations.UITableColumn;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextField;

public class TestColumnPmo implements PresentationModelObject {

    private final Object modelObject = new Object();

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

    @Override
    public Object getModelObject() {
        return modelObject;
    }

}