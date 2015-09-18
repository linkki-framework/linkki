package de.faktorzehn.ipm.web.ui.table;


import com.vaadin.server.FontAwesome;

import de.faktorzehn.ipm.web.ui.section.annotations.UISection;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;

@UISection(caption = TestContainerPmo.CAPTION)
@UITable(deleteItemColumnHeader = TestContainerPmoWithAnnotation.DELETE_ITEM_COLUMN_HEADER, addItemIcon = FontAwesome.AMBULANCE)
public class TestContainerPmoWithAnnotation extends TestContainerPmo {

    public static final String DELETE_ITEM_COLUMN_HEADER = "delete-item-column-header";
    public static final FontAwesome ADD_ITEM_ICON = FontAwesome.AMBULANCE;

}