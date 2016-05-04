/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.linkki.test.matcher.Matchers.emptyMessageList;
import static org.linkki.test.matcher.Matchers.hasSize;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.faktorips.runtime.IModelObject;
import org.faktorips.runtime.IValidationContext;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.Severity;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.ui.section.annotations.ModelObject;

/**
 * @author widmaier
 */
public class ReflectionPropertyDispatcherTest {
    private static final String PRIMITIVE_BOOLEAN = "primitiveBoolean";
    private static final String OBJECT_BOOLEAN = "objectBoolean";
    private static final String XYZ = "xyz";
    private static final String ABC = "abc";
    private TestModelObject testModelObject;
    private TestPMO testPmo;

    @Before
    public void setUp() {
        testModelObject = new TestModelObject();
        testPmo = new TestPMO(testModelObject);
    }

    private TestPMO getTestPmo() {
        return testPmo;
    }

    private Object getTestModelObject() {
        return testPmo.getModelObject();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_noSupplier() {
        @SuppressWarnings("unused")
        ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(null, "null",
                new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_noFallback() {
        @SuppressWarnings("unused")
        ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                "foo", null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_noProperty() {
        @SuppressWarnings("unused")
        ReflectionPropertyDispatcher reflectionPropertyDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo,
                null, new ExceptionPropertyDispatcher("null", testModelObject, testPmo));
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, pmoDispatcher(ABC).getValueClass());
        assertEquals(String.class, pmoDispatcher(XYZ).getValueClass());
        assertEquals(Boolean.class, pmoDispatcher(OBJECT_BOOLEAN).getValueClass());

        // Test FIPM-326 workaround: wrapper class is returned for primitive type
        assertEquals(Boolean.class, pmoDispatcher(PRIMITIVE_BOOLEAN).getValueClass());
    }

    @Test
    public void testGetValue() {
        assertEquals("890", pmoDispatcher(XYZ).getValue());
    }

    public void testGetValue_fromPmo() {
        testPmo.setPmoProp("abc123");

        assertEquals("abc123", pmoDispatcher(TestPMO.PROPERTY_PMO_PROP).getValue());
    }

    @Test
    public void testGetValue_fromModelObject() {
        assertEquals("567", pmoDispatcher(ABC).getValue());
        testModelObject.setAbc("anotherValue");
        assertEquals("anotherValue", pmoDispatcher(ABC).getValue());
    }

    @Test
    public void testGetValue_changedModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo.setModelObject(newTestModelObject);
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newAbcValue", pmoDispatcher(ABC).getValue());
    }

    @Test
    public void testGetValue_changedPmo() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");

        assertEquals("newValue", pmoDispatcher(TestPMO.PROPERTY_PMO_PROP).getValue());
    }

    @Test
    public void testGetValue_changedPmoAndModelObject() {
        TestModelObject newTestModelObject = new TestModelObject();
        testPmo = new TestPMO(newTestModelObject);
        testPmo.setPmoProp("newValue");
        newTestModelObject.setAbc("newAbcValue");

        assertEquals("newValue", pmoDispatcher(TestPMO.PROPERTY_PMO_PROP).getValue());
        assertEquals("newAbcValue", pmoDispatcher(ABC).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_illegalProperty() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher("doesNotExist",
                testModelObject, testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                "doesNotExist", exceptionDispatcher);
        ReflectionPropertyDispatcher pmoDispatcher = new ReflectionPropertyDispatcher(this::getTestPmo, "doesNotExist",
                modelObjectDispatcher);
        pmoDispatcher.getValue();
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_nullProperty() {
        pmoDispatcher(null).getValue();
    }

    @Test
    public void testSetValue() {
        pmoDispatcher(ABC).setValue("test");
        assertEquals("test", pmoDispatcher(ABC).getValue());
    }

    @Test
    public void testSetValue_toModelObject() {
        assertEquals("567", testModelObject.getAbc());
        pmoDispatcher(ABC).setValue("yetAnotherValue");
        assertEquals("yetAnotherValue", testModelObject.getAbc());
    }

    @Test
    public void testSetValue_dispatchToModelObject() {
        assertEquals("567", pmoDispatcher(ABC).getValue());
    }

    @Test
    public void testSetValue_illegalProperty() {
        pmoDispatcher("doesNotExist").setValue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValue_nullProperty() {
        pmoDispatcher(null).setValue(null);
    }

    @Test
    public void testSetValue_readonlyProperty() {
        // no exception
        pmoDispatcher("fooBar").setValue("value");
    }

    @Test
    public void testIsReadOnly() {
        assertFalse(pmoDispatcher(XYZ).isReadOnly());
        assertTrue(pmoDispatcher("fooBar").isReadOnly());
    }

    @Test
    public void testGetMessages_empty() {
        MessageList messageList = new MessageList();
        assertThat(pmoDispatcher(XYZ).getMessages(messageList), emptyMessageList());
        assertThat(pmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_ShouldReturnMessagesFromModelObject() {
        MessageList messageList = new MessageList();
        Message msg1 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(testModelObject, XYZ)
                .create();
        Message msg2 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(testModelObject, ABC)
                .create();
        messageList.add(msg1);
        messageList.add(msg2);

        assertThat(pmoDispatcher(XYZ).getMessages(messageList), hasSize(1));
        assertThat(pmoDispatcher(ABC).getMessages(messageList), hasSize(1));
        assertThat(pmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_ShouldReturnMessagesFromPmo() {
        MessageList messageList = new MessageList();
        Message msg1 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(testPmo, XYZ).create();
        Message msg2 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(testPmo, ABC).create();
        Message msg3 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(testModelObject, XYZ)
                .create();
        messageList.add(msg1);
        messageList.add(msg2);
        messageList.add(msg3);

        assertThat(pmoDispatcher(XYZ).getMessages(messageList), hasSize(2));
        assertThat(pmoDispatcher(ABC).getMessages(messageList), hasSize(1));
        assertThat(pmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testGetMessages_IgnoreIrrelevantMessages() {
        MessageList messageList = new MessageList();
        Message msg1 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(new Object(), XYZ).create();
        Message msg2 = new Message.Builder(ABC, Severity.ERROR).invalidObjectWithProperties(new Object(), ABC).create();
        messageList.add(msg1);
        messageList.add(msg2);

        assertThat(pmoDispatcher(XYZ).getMessages(messageList), emptyMessageList());
        assertThat(pmoDispatcher(ABC).getMessages(messageList), emptyMessageList());
        assertThat(pmoDispatcher("invalidProperty").getMessages(messageList), emptyMessageList());
    }

    @Test
    public void testIsEnabled() {
        assertFalse(pmoDispatcher(XYZ).isEnabled());
        testModelObject.setAbc("123");
        assertTrue(pmoDispatcher(XYZ).isEnabled());
    }

    /**
     * Tests that the model object is never accessed for enabled state even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsEnabled_doNotAccessModelObjet() {
        pmoDispatcher(ABC).isEnabled();
    }

    @Test(expected = NullPointerException.class)
    public void testIsEnabled_nullProperty() {
        pmoDispatcher(null).isEnabled();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEnabled_illegalProperty() {
        pmoDispatcher("doesNotExist").isEnabled();
    }

    @Test(expected = NullPointerException.class)
    public void testIsReadOnly_nullProperty() {
        pmoDispatcher(null).isReadOnly();
    }

    @Test
    public void testIsReadOnly_illegalProperty() {
        /*
         * Expect true if no setter exists. Dispatcher cannot yet differentiate between missing
         * setter and missing property.
         */
        assertTrue(pmoDispatcher("doesNotExist").isReadOnly());
    }

    @Test
    public void testIsVisible() {
        assertFalse(pmoDispatcher(XYZ).isVisible());
        testModelObject.setAbc("bla");
        assertTrue(pmoDispatcher(XYZ).isVisible());
    }

    @Test(expected = NullPointerException.class)
    public void testIsVisible_nullProperty() {
        pmoDispatcher(null).isVisible();
    }

    /**
     * Tests that the model object is never accessed for visibility even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsVisible_doNotAccessModelObjet() {
        pmoDispatcher(ABC).isVisible();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsVisible_illegalProperty() {
        pmoDispatcher("doesNotExist").isVisible();
    }

    @Test
    public void testIsRequired() {
        assertFalse(pmoDispatcher(XYZ).isRequired());
        testModelObject.setAbc("zzz");
        assertTrue(pmoDispatcher(XYZ).isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testIsRequired_nullProperty() {
        pmoDispatcher(null).isRequired();
    }

    /**
     * Tests that the model object is never accessed for mandatory state even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_doNotAccessModelObjet() {
        pmoDispatcher(ABC).isRequired();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired_illegalProperty() {
        pmoDispatcher("doesNotExist").isRequired();
    }

    @Test
    public void testGetAvailableValues() {
        assertEquals(0, pmoDispatcher(XYZ).getAvailableValues().size());
        testModelObject.setAbc("lov");
        assertEquals(3, pmoDispatcher(XYZ).getAvailableValues().size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAvailableValues_nullProperty() {
        pmoDispatcher(null).isEnabled();
    }

    /**
     * Tests that the model object is never accessed for available values even if it has a matching
     * method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableValues_doNotAccessModelObjet() {
        pmoDispatcher(ABC).getAvailableValues();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailableValues_illegalProperty() {
        pmoDispatcher("doesNotExist").isEnabled();
    }

    @Test
    public void testInvoke() {
        testPmo = spy(new TestPMO(testModelObject));
        pmoDispatcher("buttonClick").invoke();
        verify(testPmo).buttonClick();
    }

    @Test(expected = RuntimeException.class)
    public void testInvoke_illegalMethodName() {
        pmoDispatcher("noSuchMethod").invoke();
    }

    private ReflectionPropertyDispatcher pmoDispatcher(String property) {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher(property, testModelObject,
                testPmo);
        ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(this::getTestModelObject,
                property, exceptionDispatcher);
        return new ReflectionPropertyDispatcher(this::getTestPmo, property, modelObjectDispatcher);
    }

    public static class TestPMO {

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

        @ModelObject
        public TestModelObject getModelObject() {
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
