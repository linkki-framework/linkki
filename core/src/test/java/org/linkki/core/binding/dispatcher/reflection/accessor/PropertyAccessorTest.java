/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding.dispatcher.reflection.accessor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.linkki.test.matcher.Matchers.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.dispatcher.accessor.other.OtherPackageTestObject;
import org.linkki.core.binding.dispatcher.accessor.other.TestPublicSubclass;

public class PropertyAccessorTest {

    private static final String STRING_PROPERTY_INITIAL_VALUE = "initialValue";

    private TestObject testObject;
    private PropertyAccessor<TestObject, String> stringAccessor;

    @BeforeEach
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
    @Test
    public void testWriteWrongType() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            ((PropertyAccessor)stringAccessor).setPropertyValue(testObject, 5);
        });
    }

    @Test
    public void testConstructor() {
        String boundProperty = stringAccessor.getPropertyName();
        assertNotNull(boundProperty);
    }

    @Test
    public void testConstructorWrongProperty() {
        PropertyAccessor<Object, ?> propertyAccessor = new PropertyAccessor<>(Object.class, "doesNotExist");
        assertThat((propertyAccessor.canRead()), is(false));
        assertThat((propertyAccessor.canWrite()), is(false));
    }

    @SuppressWarnings({ "unused" })
    @Test
    public void testConstructor_nullObject() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            PropertyAccessor<?, ?> propertyAccessor = new PropertyAccessor<>(null, "anyProperty");
        });

    }

    @SuppressWarnings({ "unused" })
    @Test
    public void testConstructor_nullPropertyName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            PropertyAccessor<TestObject, ?> propertyAccessor = new PropertyAccessor<>(TestObject.class, null);
        });
    }

    @SuppressWarnings({ "unused" })
    @Test
    public void testConstructor_nullArguments() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            PropertyAccessor<?, ?> propertyAccessor = new PropertyAccessor<>(null, null);
        });
    }

    @Test
    public void testBooleanProperty() {
        Object testObject2 = new TestObject();
        PropertyAccessor<Object, Object> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.BOOLEAN_PROPERTY);
        assertThat(accessor.getPropertyValue(testObject2), is(false));

        accessor.setPropertyValue(testObject2, true);

        assertThat(accessor.getPropertyValue(testObject2), is(true));
    }

    @Test
    public void testIntProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Integer> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.INT_PROPERTY);
        assertEquals(42, accessor.getPropertyValue(testObject2).intValue());

        accessor.setPropertyValue(testObject2, 23);

        assertEquals(23, accessor.getPropertyValue(testObject2).intValue());
    }

    @Test
    public void testCanRead() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);
        assertThat((propertyAccessor.canWrite()), is(false));
        assertThat(propertyAccessor.canRead());
        Long propertyValue = propertyAccessor.getPropertyValue(testObject2);
        assertEquals(42, propertyValue.longValue());
    }

    @Test
    public void testSetPropertyValue_readOnlyProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.READ_ONLY_LONG_PROPERTY);

        Assertions.assertThrows(LinkkiBindingException.class, () -> {
            accessor.setPropertyValue(testObject2, 5L);
        });
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

    @Test
    public void testGetValueClassIllegalProperty() {
        PropertyAccessor<TestObject, ?> accessor = new PropertyAccessor<>(TestObject.class, "illegalProperty");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accessor.getValueClass();
        });
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
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
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

    /**
     * On my machine with LambdaMetafactory:
     * <ul>
     * <li>1000 set/get-calls took 9ms</li>
     * <li>1000000 set/get-calls took 47ms</li>
     * <li>1000000000 set/get-calls took 13637ms</li>
     * </ul>
     * With old reflection:
     * <ul>
     * <li>1000 set/get-calls took 6ms</li>
     * <li>1000000 set/get-calls took 57ms</li>
     * <li>1000000000 set/get-calls took 21279ms</li>
     * </ul>
     */
    @Disabled
    @Test
    public void testPerformance() {
        PropertyAccessor<TestObject, Integer> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.INT_PROPERTY);
        for (long l = 1000L; l <= 1_000_000_000L; l *= 1000) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < l; i++) {
                accessor.setPropertyValue(testObject, i);
                assertThat(accessor.getPropertyValue(testObject), is(i));
            }
            System.out.println(l + " set/get-calls took " + (System.currentTimeMillis() - start) + "ms");
        }
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
