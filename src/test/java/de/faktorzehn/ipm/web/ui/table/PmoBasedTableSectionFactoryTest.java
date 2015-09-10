/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableSectionFactoryTest {

    @Mock
    private BindingContext ctx;

    @Mock
    private PropertyBehaviorProvider pbp;

    @Test
    public void testCreateEmptySection_IconIsReadFromAnnotation() {
        TestContainerPmoWithAnnotation containerPmo = new TestContainerPmoWithAnnotation();
        containerPmo.setAddAction(TestColumnPmo::new);
        PmoBasedTableSectionFactory<TestColumnPmo> factory = new PmoBasedTableSectionFactory<>(containerPmo, ctx, pbp);

        TableSection<TestColumnPmo> section = factory.createEmptySection();
        HorizontalLayout header = (HorizontalLayout)section.getComponent(0);
        Button addButton = (Button)header.getComponent(1);

        assertThat(addButton.getIcon(), is(TestContainerPmoWithAnnotation.ADD_ITEM_ICON));
    }

    @Test
    public void testCreateEmptySection_DefaultIconIsUsedIfAnnotationIsMissing() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        containerPmo.setAddAction(TestColumnPmo::new);
        PmoBasedTableSectionFactory<TestColumnPmo> factory = new PmoBasedTableSectionFactory<>(containerPmo, ctx, pbp);

        TableSection<TestColumnPmo> section = factory.createEmptySection();
        HorizontalLayout header = (HorizontalLayout)section.getComponent(0);
        Button addButton = (Button)header.getComponent(1);

        assertThat(addButton.getIcon(), is(UITable.DEFAULT_ADD_ITEM_ICON));
    }
}
