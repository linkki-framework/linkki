/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
    private ReflectionPropertyDispatcher pmoDispatcher;
    private ReflectionPropertyDispatcher modelObjectDispatcher;
    private ExceptionPropertyDispatcher exceptionDispatcher;
    private TestModelObject testModelObject;
    private TestPMO testPmo;

    @Before
    public void setUp() {
        testModelObject = new TestModelObject();
        testPmo = new TestPMO(testModelObject);
        exceptionDispatcher = new ExceptionPropertyDispatcher(testModelObject, testPmo);
        modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject, exceptionDispatcher);
        pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, modelObjectDispatcher);
    }

    private TestPMO getTestPmo() {
        return testPmo;
    }

    private Object getTestModelObject() {
        return testPmo.getModelObject();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor() {
        pmoDispatcher = new ReflectionPropertyDispatcher(null, exceptionDispatcher);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor2() {
        pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, null);
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, pmoDispatcher.getValueClass("abc"));
        assertEquals(String.class, pmoDispatcher.getValueClass("xyz"));
        assertEquals(Boolean.class, pmoDispatcher.getValueClass("objectBoolean"));

        // Test FIPM-326 workaround: wrapper class is returned for primitive type
        assertEquals(Boolean.class, pmoDispatcher.getValueClass("primitiveBoolean"));
    }

    @Test
    public void testGetValue() {
        assertEquals("890", pmoDispatcher.getValue("xyz"));
    }

    public void testGetValue_fromPmo() {
        testPmo.setPmoProp("abc123");

        assertEquals("abc123", pmoDispatcher.getValue(TestPMO.PROPERTY_PMO_PROP));
    }

    @Test
    public void testGetValue_fromModelObject() {
        assertEquals("567", pmoDispatcher.getValue("abc"));
        testModelObject.setAbc("anotherValue");
        assertEquals("anotherValue", pmoDispatcher.getValue("abc"));
    }

    @Test
    public void testGetValue_changedModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo.setModelObject(newTestModelObject);
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newAbcValue", pmoDispatcher.getValue("abc"));
    }

    @Test
    public void testGetValue_changedPmo() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");

        assertEquals("newValue", pmoDispatcher.getValue(TestPMO.PROPERTY_PMO_PROP));
    }

    @Test
    public void testGetValue_changedPmoAndModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newValue", pmoDispatcher.getValue(TestPMO.PROPERTY_PMO_PROP));
        assertEquals("newAbcValue", pmoDispatcher.getValue("abc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_illegalProperty() {
        pmoDispatcher.getValue("doesNotExist");
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_nullProperty() {
        pmoDispatcher.getValue(null);
    }

    @Test
    public void testSetValue() {
        pmoDispatcher.setValue("abc", "test");
        assertEquals("test", pmoDispatcher.getValue("abc"));
    }

    @Test
    public void testSetValue_toModelObject() {
        assertEquals("567", testModelObject.getAbc());
        pmoDispatcher.setValue("abc", "yetAnotherValue");
        assertEquals("yetAnotherValue", testModelObject.getAbc());
    }

    @Test
    public void testSetValue_dispatchToModelObject() {
        assertEquals("567", pmoDispatcher.getValue("abc"));
    }

    @Test
    public void testSetValue_illegalProperty() {
        pmoDispatcher.setValue("doesNotExist", null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValue_nullProperty() {
        pmoDispatcher.setValue(null, null);
    }

    @Test
    public void testSetValue_readonlyProperty() {
        // no exception
        pmoDispatcher.setValue("fooBar", "value");
    }

    @Test
    public void testIsReadonly() {
        assertFalse(pmoDispatcher.isReadonly("xyz"));
        assertTrue(pmoDispatcher.isReadonly("fooBar"));
    }

    /**
     * No messages in any case.
     */
    @Test
    public void testGetMessages() {
        assertEquals(0, pmoDispatcher.getMessages("xyz").size());
        assertEquals(0, pmoDispatcher.getMessages(null).size());
        assertEquals(0, pmoDispatcher.getMessages("invalidProperty").size());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(pmoDispatcher.isEnabled("xyz"));
        testModelObject.setAbc("123");
        assertTrue(pmoDispatcher.isEnabled("xyz"));
    }

    /**
     * Tests that the model object is never accessed for enabled state even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsEnabled_doNotAccessModelObjet() {
        pmoDispatcher.isEnabled("abc");
    }

    @Test(expected = NullPointerException.class)
    public void testIsEnabled_nullProperty() {
        pmoDispatcher.isEnabled(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEnabled_illegalProperty() {
        pmoDispatcher.isEnabled("doesNotExist");
    }

    @Test(expected = NullPointerException.class)
    public void testIsReadonly_nullProperty() {
        pmoDispatcher.isReadonly(null);
    }

    @Test
    public void testIsReadonly_illegalProperty() {
        /*
         * Expect true if no setter exists. Dispatcher cannot yet differentiate between missing
         * setter and missing property.
         */
        assertTrue(pmoDispatcher.isReadonly("doesNotExist"));
    }

    @Test
    public void testIsVisible() {
        assertFalse(pmoDispatcher.isVisible("xyz"));
        testModelObject.setAbc("bla");
        assertTrue(pmoDispatcher.isVisible("xyz"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsVisible_nullProperty() {
        pmoDispatcher.isVisible(null);
    }

    /**
     * Tests that the model object is never accessed for visibility even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsVisible_doNotAccessModelObjet() {
        pmoDispatcher.isVisible("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsVisible_illegalProperty() {
        pmoDispatcher.isVisible("doesNotExist");
    }

    @Test
    public void testIsRequired() {
        assertFalse(pmoDispatcher.isRequired("xyz"));
        testModelObject.setAbc("zzz");
        assertTrue(pmoDispatcher.isRequired("xyz"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsRequired_nullProperty() {
        pmoDispatcher.isRequired(null);
    }

    /**
     * Tests that the model object is never accessed for mandatory state even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_doNotAccessModelObjet() {
        pmoDispatcher.isRequired("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_illegalProperty() {
        pmoDispatcher.isRequired("doesNotExist");
    }

    @Test
    public void testGetAvailableValues() {
        assertEquals(0, pmoDispatcher.getAvailableValues("xyz").size());
        testModelObject.setAbc("lov");
        assertEquals(3, pmoDispatcher.getAvailableValues("xyz").size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAvailableValues_nullProperty() {
        pmoDispatcher.isEnabled(null);
    }

    /**
     * Tests that the model object is never accessed for available values even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableValues_doNotAccessModelObjet() {
        pmoDispatcher.getAvailableValues("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableValues_illegalProperty() {
        pmoDispatcher.isEnabled("doesNotExist");
    }

    @Test
    public void testInvoke() {
        testPmo = spy(new TestPMO(testModelObject));
        pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, new ReflectionPropertyDispatcher(
                this::getTestModelObject, new ExceptionPropertyDispatcher(testModelObject, testPmo)));
        pmoDispatcher.invoke("buttonClick");
        verify(testPmo).buttonClick();
    }

    @Test(expected = RuntimeException.class)
    public void testInvoke_illegalMethodName() {
        pmoDispatcher.invoke("noSuchMethod");
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

        public void buttonClick() {
            // Nothing to do
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
