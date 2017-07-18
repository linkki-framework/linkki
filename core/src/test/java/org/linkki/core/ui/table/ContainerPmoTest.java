/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.table;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ContainerPmoTest {

    @Test
    public void testGetItemPmoClass() {
        TestTablePmo testTablePmo = new TestTablePmo();

        Class<?> itemPmoClass = testTablePmo.getItemPmoClass();

        assertThat(itemPmoClass, is(TestRowPmo.class));
    }

    @Test
    public void testGetItemPmoClass_Indirect() {
        IndirectContainerPmo indirectContainerPmo = new IndirectContainerPmo();

        Class<?> itemPmoClass = indirectContainerPmo.getItemPmoClass();

        assertThat(itemPmoClass, is(SubTestRow.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetItemPmoClass_exception() {
        RawContainerPmo rawContainerPmo = new RawContainerPmo();

        rawContainerPmo.getItemPmoClass();
    }

    @SuppressWarnings("rawtypes")
    private static class RawContainerPmo implements ContainerPmo {

        @Override
        public List getItems() {
            return new ArrayList<>();
        }

    }


    private static class SubTestRow extends TestRowPmo {

        public SubTestRow(TestTablePmo parent) {
            super(parent);
        }

    }

    private abstract static class AnotherTestTablePmo<T extends TestRowPmo> implements ContainerPmo<T> {
        // nothing
    }

    @SuppressWarnings("unused")
    private static class IndirectContainerPmo extends AnotherTestTablePmo<SubTestRow> {

        @Override
        public List<SubTestRow> getItems() {
            return Arrays.asList(new SubTestRow(new TestTablePmo()));
        }

    }

}
