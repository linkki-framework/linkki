/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls.pmo.sample;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;

@UISection(caption = "Some Caption")
public class NlsOtherTablePmo implements ContainerPmo<NlsTableRowPmo> {

    private final List<NlsTableRowPmo> rows = new ArrayList<>();

    @Override
    public List<NlsTableRowPmo> getItems() {
        return rows;
    }

}
