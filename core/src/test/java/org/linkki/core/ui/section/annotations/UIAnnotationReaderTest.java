/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UITextField;

public class UIAnnotationReaderTest {

    private UIAnnotationReader annotationReader = new UIAnnotationReader(new TestObject().getClass());

    @Test
    public void testGetFieldDefinitions() {
        assertThat(annotationReader.getUiElements(), hasSize(3));
    }

    @Test
    public void testGetFieldDefinition() {
        assertThat(annotationReader.get("test"), is(notNullValue()));
        assertThat(annotationReader.get("test2"), is(notNullValue()));
        assertThat(annotationReader.get("test3"), is(notNullValue()));
        assertThat(annotationReader.get("illegal"), is(nullValue()));
    }

    @Test
    public void testHasFieldDefinition() {
        assertThat(annotationReader.hasAnnotation("test"), is(true));
        assertThat(annotationReader.hasAnnotation("test2"), is(true));
        assertThat(annotationReader.hasAnnotation("test3"), is(true));
        assertThat(annotationReader.hasAnnotation("illegalProperty"), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testHasFieldDefinition_NullProperty() {
        assertThat(annotationReader.hasAnnotation(null), is(false));
    }

    @Test
    public void testGetTableColumnDescriptorString() {
        assertThat(annotationReader.getTableColumnDescriptor("test"), is(nullValue()));
        assertThat(annotationReader.getTableColumnDescriptor("test2"), is(nullValue()));
        assertThat(annotationReader.getTableColumnDescriptor("test3"), is(notNullValue()));
    }

    @Test
    public void testGetTableColumnDescriptorFieldDescriptor() {
        ElementDescriptor test = annotationReader.get("test");
        ElementDescriptor test3 = annotationReader.get("test3");

        assertThat(test, is(notNullValue()));
        assertThat(test3, is(notNullValue()));

        assertThat(annotationReader.getTableColumnDescriptor(test), is(nullValue()));
        assertThat(annotationReader.getTableColumnDescriptor(test3), is(notNullValue()));
    }

    @Test
    public void testHasTableColumnAnnotation() {
        ElementDescriptor test = annotationReader.get("test");
        ElementDescriptor test3 = annotationReader.get("test3");

        assertThat(test, is(notNullValue()));
        assertThat(test3, is(notNullValue()));

        assertThat(annotationReader.hasTableColumnAnnotation(test), is(false));
        assertThat(annotationReader.hasTableColumnAnnotation(test3), is(true));
    }

    public static class TestObject {
        @UITextField(position = 1, modelAttribute = "test")
        public void test() {
            //
        }

        @UIComboBox(position = 2, modelAttribute = "test2")
        public void abc() {
            //
        }

        @UITableColumn
        @UIDateField(position = 3, modelAttribute = "test3")
        public void isTest3() {
            //
        }
    }

}
