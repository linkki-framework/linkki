/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.faktorips.runtime.IModelObject;
import org.faktorips.runtime.IValidationContext;
import org.faktorips.runtime.MessageList;
import org.junit.Before;
import org.junit.Test;

import de.faktorzehn.ipm.web.PresentationModelObject;

/**
 * @author widmaier
 */
public class ReflectionPropertyDispatcherTest {
    private ReflectionPropertyDispatcher dispatcher;
    private TestModelObject testModelObject;
    private TestPMO testPMO;

    @Before
    public void setUp() {
        testModelObject = new TestModelObject();
        testPMO = new TestPMO(testModelObject);
        dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, new ReflectionPropertyDispatcher(
                this::getTestModelObject, new ExceptionPropertyDispatcher(testModelObject, testPMO)));
    }

    private TestPMO getTestPmo() {
        return testPMO;
    }

    private Object getTestModelObject() {
        return testPMO.getModelObject();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor() {
        dispatcher = new ReflectionPropertyDispatcher(null, new ExceptionPropertyDispatcher(testModelObject, testPMO));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor2() {
        dispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, null);
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, dispatcher.getValueClass("abc"));
        assertEquals(String.class, dispatcher.getValueClass("xyz"));
        assertEquals(Boolean.class, dispatcher.getValueClass("objectBoolean"));

        // Test FIPM-326 workaround: wrapper class is returned for primitive type
        assertEquals(Boolean.class, dispatcher.getValueClass("primitiveBoolean"));
    }

    @Test
    public void testGetValue() {
        assertEquals("890", dispatcher.getValue("xyz"));
    }

    public void testGetValue_fromPmo() {
        testPMO.setPmoProp("abc123");

        assertEquals("abc123", dispatcher.getValue(TestPMO.PROPERTY_PMO_PROP));
    }

    @Test
    public void testGetValue_fromModelObject() {
        assertEquals("567", dispatcher.getValue("abc"));
        testModelObject.setAbc("anotherValue");
        assertEquals("anotherValue", dispatcher.getValue("abc"));
    }

    @Test
    public void testGetValue_changedModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPMO.setModelObject(newTestModelObject);
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newAbcValue", dispatcher.getValue("abc"));
    }

    @Test
    public void testGetValue_changedPmo() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPMO = new TestPMO(newTestModelObject);
        testPMO.setPmoProp("newValue");

        assertEquals("newValue", dispatcher.getValue(TestPMO.PROPERTY_PMO_PROP));
    }

    @Test
    public void testGetValue_changedPmoAndModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPMO = new TestPMO(newTestModelObject);
        testPMO.setPmoProp("newValue");
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newValue", dispatcher.getValue(TestPMO.PROPERTY_PMO_PROP));
        assertEquals("newAbcValue", dispatcher.getValue("abc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_illegalProperty() {
        dispatcher.getValue("doesNotExist");
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_nullProperty() {
        dispatcher.getValue(null);
    }

    @Test
    public void testSetValue() {
        dispatcher.setValue("abc", "test");
        assertEquals("test", dispatcher.getValue("abc"));
    }

    @Test
    public void testSetValue_toModelObject() {
        assertEquals("567", testModelObject.getAbc());
        dispatcher.setValue("abc", "yetAnotherValue");
        assertEquals("yetAnotherValue", testModelObject.getAbc());
    }

    @Test
    public void testSetValue_dispatchToModelObject() {
        assertEquals("567", dispatcher.getValue("abc"));
    }

    @Test
    public void testSetValue_illegalProperty() {
        dispatcher.setValue("doesNotExist", null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValue_nullProperty() {
        dispatcher.setValue(null, null);
    }

    @Test
    public void testSetValue_readonlyProperty() {
        // no exception
        dispatcher.setValue("fooBar", "value");
    }

    @Test
    public void testIsReadonly() {
        assertFalse(dispatcher.isReadonly("xyz"));
        assertTrue(dispatcher.isReadonly("fooBar"));
    }

    /**
     * No messages in any case.
     */
    @Test
    public void testGetMessages() {
        assertEquals(0, dispatcher.getMessages("xyz").size());
        assertEquals(0, dispatcher.getMessages(null).size());
        assertEquals(0, dispatcher.getMessages("invalidProperty").size());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(dispatcher.isEnabled("xyz"));
        testModelObject.setAbc("123");
        assertTrue(dispatcher.isEnabled("xyz"));
    }

    /**
     * Tests that the model object is never accessed for enabled state even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsEnabled_doNotAccessModelObjet() {
        dispatcher.isEnabled("abc");
    }

    @Test(expected = NullPointerException.class)
    public void testIsEnabled_nullProperty() {
        dispatcher.isEnabled(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEnabled_illegalProperty() {
        dispatcher.isEnabled("doesNotExist");
    }

    @Test(expected = NullPointerException.class)
    public void testIsReadonly_nullProperty() {
        dispatcher.isReadonly(null);
    }

    @Test
    public void testIsReadonly_illegalProperty() {
        /*
         * Expect true if no setter exists. Dispatcher cannot yet differentiate between missing
         * setter and missing property.
         */
        assertTrue(dispatcher.isReadonly("doesNotExist"));
    }

    @Test
    public void testIsVisible() {
        assertFalse(dispatcher.isVisible("xyz"));
        testModelObject.setAbc("bla");
        assertTrue(dispatcher.isVisible("xyz"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsVisible_nullProperty() {
        dispatcher.isVisible(null);
    }

    /**
     * Tests that the model object is never accessed for visibility even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsVisible_doNotAccessModelObjet() {
        dispatcher.isVisible("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsVisible_illegalProperty() {
        dispatcher.isVisible("doesNotExist");
    }

    @Test
    public void testIsRequired() {
        assertFalse(dispatcher.isRequired("xyz"));
        testModelObject.setAbc("zzz");
        assertTrue(dispatcher.isRequired("xyz"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsRequired_nullProperty() {
        dispatcher.isRequired(null);
    }

    /**
     * Tests that the model object is never accessed for mandatory state even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_doNotAccessModelObjet() {
        dispatcher.isRequired("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_illegalProperty() {
        dispatcher.isRequired("doesNotExist");
    }

    @Test
    public void testGetAvailableValues() {
        assertEquals(0, dispatcher.getAvailableValues("xyz").size());
        testModelObject.setAbc("lov");
        assertEquals(3, dispatcher.getAvailableValues("xyz").size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAvailableValues_nullProperty() {
        dispatcher.isEnabled(null);
    }

    /**
     * Tests that the model object is never accessed for available values even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableValues_doNotAccessModelObjet() {
        dispatcher.getAvailableValues("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableValues_illegalProperty() {
        dispatcher.isEnabled("doesNotExist");
    }

    public static class TestPMO implements PresentationModelObject {

        public static final String PROPERTY_PMO_PROP = "pmoProp";

        private TestModelObject modelObject;

        private String pmoProp;

        public TestPMO(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        public String getXyz() {
            return "890";
        }

        public boolean isXyzEnabled() {
            return modelObject.getAbc().equals("123");
        }

        public boolean isXyzVisible() {
            return modelObject.getAbc().equals("bla");
        }

        public boolean isXyzRequired() {
            return modelObject.getAbc().equals("zzz");
        }

        public List<String> getXyzAvailableValues() {
            if (modelObject.getAbc().equals("lov")) {
                return Arrays.asList(new String[] { "1", "2", "3" });
            } else {
                return Collections.emptyList();
            }
        }

        public String getFooBar() {
            return "";
        }

        @Override
        public Object getModelObject() {
            return modelObject;
        }

        public void setModelObject(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        public String getPmoProp() {
            return pmoProp;
        }

        public void setPmoProp(String pmoProp) {
            this.pmoProp = pmoProp;
        }

    }

    public static class TestModelObject implements IModelObject {

        private String xyz = "123";
        private String abc = "567";

        private boolean primitiveBoolean = false;
        private Boolean objectBoolean = null;

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean b) {
            this.primitiveBoolean = b;
        }

        public Boolean getObjectBoolean() {
            return objectBoolean;
        }

        public void setObjectBoolean(Boolean objectBoolean) {
            this.objectBoolean = objectBoolean;
        }

        public String getXyz() {
            return xyz;
        }

        public void setXyz(String xyz) {
            this.xyz = xyz;
        }

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public boolean isAbcEnabled() {
            return true;
        }

        public boolean isAbcVisible() {
            return true;
        }

        public boolean isAbcRequired() {
            return true;
        }

        public List<?> getAbcAvailableValues() {
            return Collections.emptyList();
        }

        @Override
        public MessageList validate(IValidationContext context) {
            return null;
        }
    }
}
