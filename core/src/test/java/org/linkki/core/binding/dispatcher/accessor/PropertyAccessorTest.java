/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.binding.dispatcher.accessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.dispatcher.accessor.other.OtherPackageTestObject;
import org.linkki.core.binding.dispatcher.accessor.other.TestPublicSubclass;

public class PropertyAccessorTest {

    private static final String STRING_PROPERTY_INITIAL_VALUE = "initialValue";

    @SuppressWarnings("null")
    private TestObject testObject;
    @SuppressWarnings("null")
    private PropertyAccessor<TestObject, String> stringAccessor;

    @Before
    public void setUp() {
        testObject = new TestObject();
        testObject.setStringProperty(STRING_PROPERTY_INITIAL_VALUE);
        stringAccessor = new PropertyAccessor<>(TestObject.class, TestObject.STRING_PROPERTY);
    }

    @Test
    public void testRead() {
        String propertyValue = stringAccessor.getPropertyValue(testObject);
        assertEquals(STRING_PROPERTY_INITIAL_VALUE, propertyValue);
    }

    @Test
    public void testWrite() {
        assertEquals(STRING_PROPERTY_INITIAL_VALUE, testObject.getStringProperty());
        stringAccessor.setPropertyValue(testObject, "anotherValue");
        assertEquals("anotherValue", testObject.getStringProperty());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test(expected = RuntimeException.class)
    public void testWriteWrongType() {
        ((PropertyAccessor)stringAccessor).setPropertyValue(testObject, 5);
    }

    @Test
    public void testConstructor() {
        String boundProperty = stringAccessor.getPropertyName();
        assertNotNull(boundProperty);
    }

    @Test
    public void testConstructorWrongProperty() {
        PropertyAccessor<Object, ?> propertyAccessor = new PropertyAccessor<>(Object.class, "doesNotExist");
        assertFalse(propertyAccessor.canRead());
        assertFalse(propertyAccessor.canWrite());
    }

    @SuppressWarnings({ "null", "unused" })
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullObject() {
        PropertyAccessor<?, ?> propertyAccessor = new PropertyAccessor<>(null, "anyProperty");
    }

    @SuppressWarnings({ "null", "unused" })
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullPropertyName() {
        PropertyAccessor<TestObject, ?> propertyAccessor = new PropertyAccessor<>(TestObject.class, null);
    }

    @SuppressWarnings({ "null", "unused" })
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullArguments() {
        PropertyAccessor<?, ?> propertyAccessor = new PropertyAccessor<>(null, null);
    }

    @Test
    public void testBooleanProperty() {
        Object testObject2 = new TestObject();
        PropertyAccessor<Object, Object> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.BOOLEAN_PROPERTY);
        assertTrue((Boolean)accessor.getPropertyValue(testObject2));
        accessor.setPropertyValue(testObject2, true);
    }

    @Test
    public void testIntProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Integer> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.INT_PROPERTY);
        assertEquals(42, accessor.getPropertyValue(testObject2).intValue());
        accessor.setPropertyValue(testObject2, 23);
    }

    @Test
    public void testCanRead() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);
        assertFalse(propertyAccessor.canWrite());
        assertTrue(propertyAccessor.canRead());
        Long propertyValue = propertyAccessor.getPropertyValue(testObject2);
        assertEquals(42, propertyValue.longValue());
    }

    @Test(expected = LinkkiBindingException.class)
    public void testSetPropertyValue_readOnlyProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);
        accessor.setPropertyValue(testObject2, 5L);
    }

    @Test
    public void testGetValueClass() {
        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);
        assertEquals(long.class, accessor.getValueClass());
    }

    @Test
    public void testGetValueClass2() {
        PropertyAccessor<TestObject, Boolean> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.BOOLEAN_PROPERTY);
        assertEquals(boolean.class, accessor.getValueClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueClassIllegalProperty() {
        PropertyAccessor<TestObject, ?> accessor = new PropertyAccessor<>(TestObject.class, "illegalProperty");
        accessor.getValueClass();
    }

    @Test
    public void testLongProperty() {
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);
        Long propertyValue = propertyAccessor.getPropertyValue(testObject);
        assertEquals(42, propertyValue.longValue());
    }

    /**
     * It should be possible to call default methods from implemented interface.
     */
    @Test
    public void testDefaultMethod() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(TestInterfaceImpl.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new TestInterfaceImpl();
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("Hello", propertyValue);
    }

    /**
     * It should be possible to call default methods from implemented interface of the super class.
     */
    @Test
    public void testDefaultMethod_InSubclass() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                TestInterfaceImplSub.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new TestInterfaceImplSub();
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("Hello", propertyValue);
    }

    /**
     * If a default method from an interface is overriden, the overriding method should be called.
     */
    @Test
    public void testDefaultMethod_OverwrittenInSubclass() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                TestInterfaceOverwriting.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new TestInterfaceOverwriting();
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("Hi", propertyValue);
    }

    @Test
    public void testDefaultMethod_OtherPackage() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                OtherPackageTestObject.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new OtherPackageTestObject();
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("other", propertyValue);
    }

    @Test
    public void testDefaultMethod_OtherPackagePrivate() {
        TestInterface testInterfaceImpl = OtherPackageTestObject.getPackagePrivateInstance();
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<TestInterface, String>(
                testInterfaceImpl.getClass(),
                TestInterface.RO_DEFAULT_METHOD);
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("otherPackage", propertyValue);
    }

    @Test
    public void testDefaultMethod_OtherPackageProtected() {
        PropertyAccessor<TestPublicSubclass, Integer> propertyAccessor = new PropertyAccessor<>(
                TestPublicSubclass.class,
                TestPublicSubclass.PROPERTY_ANSWER);

        int propertyValue = propertyAccessor.getPropertyValue(new TestPublicSubclass());
        assertEquals(42, propertyValue);
    }

    @Test
    public void testGenericInterface() {
        PropertyAccessor<TestGenericInterfaceImpl, String> propertyAccessor = new PropertyAccessor<>(
                TestGenericInterfaceImpl.class,
                "foo");
        TestGenericInterfaceImpl testImpl = new TestGenericInterfaceImpl();
        String propertyValue = propertyAccessor.getPropertyValue(testImpl);
        assertEquals("bar", propertyValue);

        propertyAccessor.setPropertyValue(testImpl, "baz");
        propertyValue = propertyAccessor.getPropertyValue(testImpl);
        assertEquals("baz", propertyValue);
    }

    public static class TestInterfaceImpl implements TestInterface {

        @Override
        public void doSomething() {
            // nope
        }

    }

    public static class TestInterfaceImplSub extends TestInterfaceImpl {
        // same
    }

    public static class TestInterfaceOverwriting extends TestInterfaceImpl {

        @Override
        public String getRoDefaultMethod() {
            return "Hi";
        }
    }

    public static interface TestGenericInterface<T> {
        public T getFoo();

        public void setFoo(T t);
    }

    public static class TestGenericInterfaceImpl implements TestGenericInterface<String> {

        private String foo = "bar";

        @Override
        public String getFoo() {
            return foo;
        }

        @Override
        public void setFoo(String foo) {
            this.foo = foo;
        }

    }

}
