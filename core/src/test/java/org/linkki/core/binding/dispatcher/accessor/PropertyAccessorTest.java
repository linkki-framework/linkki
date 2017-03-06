package org.linkki.core.binding.dispatcher.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class PropertyAccessorTest {
    @SuppressWarnings("null")
    private TestObject testObject;
    @SuppressWarnings("null")
    private PropertyAccessor stringAccessor;

    @Before
    public void setUp() {
        testObject = new TestObject();
        testObject.setStringProperty("initialValue");
        stringAccessor = new PropertyAccessor(testObject.getClass(), TestObject.STRING_PROPERTY);
    }

    @Test
    public void testRead() {
        String propertyValue = (String)stringAccessor.getPropertyValue(testObject);
        assertNotNull(propertyValue);
    }

    @Test
    public void testWrite() {
        assertEquals("initialValue", testObject.getStringProperty());
        stringAccessor.setPropertyValue(testObject, "anotherValue");
        assertEquals("anotherValue", testObject.getStringProperty());
    }

    @Test(expected = RuntimeException.class)
    public void testWriteWrongType() {
        PropertyAccessor accessor = new PropertyAccessor(testObject.getClass(), TestObject.STRING_PROPERTY);
        accessor.setPropertyValue(testObject, 5);
    }

    @Test
    public void testConstructor() {
        String boundProperty = stringAccessor.getPropertyName();
        assertNotNull(boundProperty);
    }

    @Test
    public void testConstructorWrongProperty() {
        PropertyAccessor propertyAccessor = new PropertyAccessor(stringAccessor.getClass(), "doesNotExist");
        assertFalse(propertyAccessor.canRead());
        assertFalse(propertyAccessor.canWrite());
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullObject() {
        stringAccessor = new PropertyAccessor(null, "anyProperty");
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullPropertyName() {
        stringAccessor = new PropertyAccessor(testObject.getClass(), null);
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullArguments() {
        stringAccessor = new PropertyAccessor(null, null);
    }

    @Test
    public void testBooleanProperty() {
        Object testObject2 = new TestObject();
        PropertyAccessor accessor = new PropertyAccessor(testObject2.getClass(), TestObject.BOOLEAN_PROPERTY);
        assertTrue((Boolean)accessor.getPropertyValue(testObject2));
        accessor.setPropertyValue(testObject2, true);
    }

    @Test
    public void testIntProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor accessor = new PropertyAccessor(testObject2.getClass(), TestObject.INT_PROPERTY);
        assertEquals(42, ((Integer)accessor.getPropertyValue(testObject2)).intValue());
        accessor.setPropertyValue(testObject2, 23);
    }

    @Test
    public void testIntProperty_stopIfValuesEqual() {
        TestObject testSample = spy(new TestObject());
        PropertyAccessor accessor = new PropertyAccessor(testSample.getClass(), TestObject.INT_PROPERTY);
        assertEquals(42, ((Integer)accessor.getPropertyValue(testSample)).intValue());

        accessor.setPropertyValue(testSample, 42);
        verify(testSample, never()).setIntProperty(42);

        accessor.setPropertyValue(testSample, 23);
        verify(testSample).setIntProperty(23);
    }

    @Test
    public void testCanRead() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor propertyAccessor = new PropertyAccessor(testObject2.getClass(),
                TestObject.READ_ONLY_LONG_PROPERTY);
        assertFalse(propertyAccessor.canWrite());
        assertTrue(propertyAccessor.canRead());
        Long propertyValue = (Long)propertyAccessor.getPropertyValue(testObject2);
        assertEquals(42, propertyValue.longValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyValue_readOnlyProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor accessor = new PropertyAccessor(testObject2.getClass(), TestObject.READ_ONLY_LONG_PROPERTY);
        accessor.setPropertyValue(testObject2, 5L);
    }

    @Test
    public void testGetValueClass() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor accessor = new PropertyAccessor(testObject2.getClass(), TestObject.READ_ONLY_LONG_PROPERTY);
        assertEquals(long.class, accessor.getValueClass());
    }

    @Test
    public void testGetValueClass2() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor accessor = new PropertyAccessor(testObject2.getClass(), TestObject.BOOLEAN_PROPERTY);
        assertEquals(boolean.class, accessor.getValueClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueClassIllegalProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor accessor = new PropertyAccessor(testObject2.getClass(), "illegalProperty");
        accessor.getValueClass();
    }

    @Test
    public void testLongProperty() {
        PropertyAccessor propertyAccessor = new PropertyAccessor(testObject.getClass(),
                TestObject.READ_ONLY_LONG_PROPERTY);
        Long propertyValue = (Long)propertyAccessor.getPropertyValue(testObject);
        assertEquals(42, propertyValue.longValue());
    }

}
