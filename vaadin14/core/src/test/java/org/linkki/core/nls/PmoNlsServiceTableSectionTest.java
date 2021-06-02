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
package org.linkki.core.nls;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.nls.sample.NlsTablePmo;
import org.linkki.core.nls.sample.NlsTableRowPmo;
import org.linkki.core.nls.sample.NoNlsTablePmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.element.annotation.TestUiUtil;
import org.linkki.core.vaadin.component.section.AbstractSection;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PmoNlsServiceTableSectionTest {

    private final BindingContext bindingContext = new BindingContext();


    private String translatedLabel;

    private String translatedCaption;

    @BeforeEach
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

    @Test
    public void testTableRowLabels() {
        Grid<?> table = GridComponentCreator.createGrid(new NlsTablePmo(), bindingContext);
        assertThat(table, is(notNullValue()));

        assertThat(TestUiUtil.getColumnHeaders(table),
                   contains(translatedLabel, translatedLabel, NlsTableRowPmo.PMO_LABEL, ""));
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
        HorizontalLayout header = (HorizontalLayout)tableSection.getComponentAt(0).getElement().getComponent().get();
        return header.getComponentAt(0).getElement().getText();
    }


}
