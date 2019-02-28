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
package org.linkki.core.nls.pmo;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.nls.pmo.sample.NlsTablePmo;
import org.linkki.core.nls.pmo.sample.NlsTableRowPmo;
import org.linkki.core.nls.pmo.sample.NoNlsTablePmo;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.PmoBasedTableFactory;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PmoNlsServiceTableSectionTest {

    private BindingContext bindingContext = new BindingContext();

    @SuppressWarnings("null")
    private String translatedLabel;
    @SuppressWarnings("null")
    private String translatedCaption;

    @Before
    public void setUp() {
        // test nls setup
        translatedCaption = PmoNlsService.get().getSectionCaption(NlsTablePmo.class, NlsTablePmo.TABLE_CAPTION);
        assertThat(translatedCaption, is(not(NlsTablePmo.TABLE_CAPTION)));
        translatedLabel = getTranslatedLabel(NlsTableRowPmo.PROPERTY_VALUE1);
        assertThat(translatedLabel, is(not(NlsTableRowPmo.PMO_LABEL)));
        assertThat(getTranslatedLabel(NlsTableRowPmo.PROPERTY_VALUE2), is(translatedLabel));
        assertThat(getTranslatedLabel(NlsTableRowPmo.PROPERTY_VALUE3), is(NlsTableRowPmo.PMO_LABEL));
        assertThat(getTranslatedLabel(NlsTableRowPmo.PROPERTY_DELETE), is(NlsTableRowPmo.PMO_LABEL));
    }

    private String getTranslatedLabel(String property) {
        return PmoNlsService.get().getLabel(NlsTableRowPmo.class, property, "label", NlsTableRowPmo.PMO_LABEL);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testTableRowLabels() {
        PmoBasedTableFactory factory = new PmoBasedTableFactory(new NlsTablePmo(), bindingContext);
        com.vaadin.v7.ui.Table table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(),
                   is(arrayContaining(translatedLabel, translatedLabel, NlsTableRowPmo.PMO_LABEL, "")));
    }

    @Test
    public void testTableSectionCaption() {
        assertThat(createAndGetTableSectionCaption(new NlsTablePmo()), is(translatedCaption));
    }

    @Test
    public void testTableSectionCaptionNoOverriding() {
        assertThat(createAndGetTableSectionCaption(new NoNlsTablePmo()), is(NoNlsTablePmo.CAPTION));
    }


    private String createAndGetTableSectionCaption(ContainerPmo<NlsTableRowPmo> containerPmo) {
        PmoBasedSectionFactory factory = new PmoBasedSectionFactory();

        AbstractSection tableSection = factory.createSection(containerPmo, bindingContext);
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponent(0);
        Label l = (Label)header.getComponent(0);
        return l.getValue();
    }


}
