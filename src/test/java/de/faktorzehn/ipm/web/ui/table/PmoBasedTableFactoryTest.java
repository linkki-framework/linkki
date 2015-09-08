/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.table;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.ui.section.annotations.UITable;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextField;

@RunWith(MockitoJUnitRunner.class)
public class PmoBasedTableFactoryTest {

    static class TestPmo implements PresentationModelObject {

        private final Object modelObject = new Object();

        private String value;

        public TestPmo(String s) {
            this.value = s;
        }

        @UITextField(position = 0, label = "Some Value")
        public String getValue() {
            return value;
        }

        public void setValue(String s) {
            this.value = s;
        }

        @Override
        public Object getModelObject() {
            return modelObject;
        }

    }

    static class TestContainerPmo implements ContainerPmo<TestPmo> {

        public static TestPmo PMO_0 = new TestPmo("foo");
        public static TestPmo PMO_1 = new TestPmo("bar");

        private Optional<DeleteItemAction<TestPmo>> deleteAction = Optional.empty();

        @Override
        public Class<TestPmo> getItemPmoClass() {
            return TestPmo.class;
        }

        @Override
        public boolean isEditable() {
            return false;
        }

        @Override
        public List<TestPmo> getItems() {
            return Lists.newArrayList(PMO_0, PMO_1);
        }

        @Override
        public Optional<DeleteItemAction<TestPmo>> deleteItemAction() {
            return deleteAction;
        }

        void setDeleteAction(DeleteItemAction<TestPmo> deleteAction) {
            this.deleteAction = Optional.of(deleteAction);
        }
    }

    @UITable(deleteItemColumnHeader = "Test Delete Column Header")
    static class TestContainerPmoWithAnnotation extends TestContainerPmo {
        // Noting to implement, class is only needed for the annotation
    }

    @Mock
    private BindingContext bindingContext;

    @Mock
    private PropertyBehaviorProvider propertyBehaviorProvider;

    @Test
    public void testCreateTable() {
        PmoBasedTableFactory<TestPmo> factory = new PmoBasedTableFactory<>(new TestContainerPmo(), bindingContext,
                propertyBehaviorProvider);
        PmoBasedTable<TestPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(), is(arrayContaining("Some Value:")));
    }

    @Test
    public void testCreateTable_DeleteColumnHeaderIsReadFromAnnotation() {
        TestContainerPmoWithAnnotation containerPmo = new TestContainerPmoWithAnnotation();
        containerPmo.setDeleteAction(System.out::println);
        PmoBasedTableFactory<TestPmo> factory = new PmoBasedTableFactory<>(containerPmo, bindingContext,
                propertyBehaviorProvider);
        PmoBasedTable<TestPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(), is(arrayContaining("Some Value:", "Test Delete Column Header")));
    }

    @Test
    public void testCreateTable_DefaultDeleteColumnHeaderIsUsedIfAnnotationIsMissing() {
        TestContainerPmo containerPmo = new TestContainerPmo();
        containerPmo.setDeleteAction(System.out::println);
        PmoBasedTableFactory<TestPmo> factory = new PmoBasedTableFactory<>(containerPmo, bindingContext,
                propertyBehaviorProvider);
        PmoBasedTable<TestPmo> table = factory.createTable();
        assertThat(table, is(notNullValue()));
        assertThat(table.getColumnHeaders(), is(arrayContaining("Some Value:", "Entfernen")));
    }
}
