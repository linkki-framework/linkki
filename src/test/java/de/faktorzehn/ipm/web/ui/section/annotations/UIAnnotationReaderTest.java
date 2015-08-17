/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UIAnnotationReaderTest {

    private UIAnnotationReader annotationReader = new UIAnnotationReader(new TestObject().getClass());

    @Test
    public void testGetFieldDefinitions() {
        assertEquals(3, annotationReader.getFields().size());
    }

    @Test
    public void testGetFieldDefinition() {
        assertNotNull(annotationReader.get("test"));
        assertNotNull(annotationReader.get("test2"));
        assertNotNull(annotationReader.get("test3"));
        assertNull(annotationReader.get("illegal"));
    }

    @Test
    public void testHasFieldDefinition() {
        assertTrue(annotationReader.hasAnnotation("test"));
        assertTrue(annotationReader.hasAnnotation("test2"));
        assertTrue(annotationReader.hasAnnotation("test3"));
        assertFalse(annotationReader.hasAnnotation("illegalProperty"));
    }

    @Test(expected = NullPointerException.class)
    public void testHasFieldDefinition_NullProperty() {
        assertFalse(annotationReader.hasAnnotation(null));
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

        @UIDateField(position = 3, modelAttribute = "test3")
        public void isTest3() {
            //
        }
    }
}
