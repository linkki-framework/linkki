/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations.adapters;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import de.faktorzehn.ipm.web.ui.components.DecimalField;
import de.faktorzehn.ipm.web.ui.components.DoubleField;
import de.faktorzehn.ipm.web.ui.components.IntegerField;
import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.ComponentTypes;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIFieldDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;
import de.faktorzehn.ipm.web.ui.section.annotations.UITableDefinition;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;

public class UITableAdapter implements UITableDefinition, UIFieldDefinition {

    private final UITable uiTable;

    public UITableAdapter(UITable uiTable) {
        this.uiTable = uiTable;
    }

    @Override
    public Component newComponent() {
        Table table = new Table();

        ComponentTypes[] componentTypes = uiTable.containerProperties();
        for (ComponentTypes componentType : componentTypes) {
            if (componentType == ComponentTypes.CHECKBOX) {
                table.addContainerProperty("", CheckBox.class, null);
            } else if (componentType == ComponentTypes.COMBOBOX) {
                table.addContainerProperty("", ComboBox.class, null);
            } else if (componentType == ComponentTypes.DATEFIELD) {
                table.addContainerProperty("", DateField.class, null);
            } else if (componentType == ComponentTypes.DECIMALFIELD) {
                table.addContainerProperty("", DecimalField.class, null);
            } else if (componentType == ComponentTypes.DOUBLEFIELD) {
                table.addContainerProperty("", DoubleField.class, null);
            } else if (componentType == ComponentTypes.INTEGERFIELD) {
                table.addContainerProperty("", IntegerField.class, null);
            } else if (componentType == ComponentTypes.TEXTAREA) {
                table.addContainerProperty("", TextArea.class, null);
            } else if (componentType == ComponentTypes.TEXTFIELD) {
                table.addContainerProperty("", TextField.class, null);
            }
        }
        return table;
    }

    @Override
    public int position() {
        return uiTable.position();
    }

    @Override
    public String label() {
        return uiTable.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTable.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTable.required();
    }

    @Override
    public VisibleType visible() {
        return uiTable.visible();
    }

    @Override
    public AvailableValuesType availableValues() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return uiTable.modelAttribute();
    }

    @Override
    public boolean noLabel() {
        return uiTable.noLabel();
    }

    @Override
    public int columns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int rows() {
        // TODO Auto-generated method stub
        return 0;
    }

}
