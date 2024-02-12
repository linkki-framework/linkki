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
package org.linkki.util.reflection.accessor;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.linkki.test.matcher.Matchers.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.linkki.util.reflection.TestInterface;
import org.linkki.util.reflection.TestObject;
import org.linkki.util.reflection.other.OtherPackageTestObject;
import org.linkki.util.reflection.other.TestPublicSubclass;

class PropertyAccessorTest {

    private static final String STRING_PROPERTY_INITIAL_VALUE = "initialValue";

    private TestObject testObject;
    private PropertyAccessor<TestObject, String> stringAccessor;

    @BeforeEach
    void setUp() {
        testObject = new TestObject();
        testObject.setStringProperty(STRING_PROPERTY_INITIAL_VALUE);
        stringAccessor = new PropertyAccessor<>(TestObject.class, TestObject.PROPERTY_STRING);
    }

    @Test
    void testGetPropertyValue() {
        String propertyValue = stringAccessor.getPropertyValue(testObject);

        assertEquals(STRING_PROPERTY_INITIAL_VALUE, propertyValue);
    }

    @Test
    void testSetPropertyValue() {
        assertEquals(STRING_PROPERTY_INITIAL_VALUE, testObject.getStringProperty());

        stringAccessor.setPropertyValue(testObject, "anotherValue");

        assertEquals("anotherValue", testObject.getStringProperty());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void testSetPropertyValue_ExceptionDuringInvocation_WrongType() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> ((PropertyAccessor)stringAccessor).setPropertyValue(testObject, 5))
                .withStackTraceContaining(TestObject.PROPERTY_STRING)
                .withStackTraceContaining(TestObject.class.getName());
    }

    @Test
    void testInvoke() {
        assertThat(testObject.isBooleanProperty(), is(false));
        var accessor = new PropertyAccessor<>(TestObject.class, TestObject.PROPERTY_DO_SOMETHING);

        accessor.invoke(testObject);

        assertThat(testObject.isBooleanProperty(), is(true));
    }

    @Test
    void testConstructor() {
        var instance = new TestObject();
        instance.setBooleanProperty(true);

        var accessor = new PropertyAccessor<>(TestObject.class, TestObject.PROPERTY_BOOLEAN);

        assertThat(accessor.getPropertyValue(instance), is(true));
    }

    @Test
    void testConstructor_WrongProperty() {
        var propertyAccessor = new PropertyAccessor<>(Object.class, "doesNotExist");

        assertThat(propertyAccessor.canRead(), is(false));
        assertThat(propertyAccessor.canWrite(), is(false));
        assertThat(propertyAccessor.canInvoke(), is(false));
    }

    @Test
    void testConstructor_nullObject() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new PropertyAccessor<>(null, "anyProperty"));
    }

    @Test
    void testConstructor_nullPropertyName() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new PropertyAccessor<>(TestObject.class, null));
    }

    @Test
    void testConstructor_nullArguments() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new PropertyAccessor<>(null, null));
    }

    @Test
    void testBooleanProperty() {
        Object testObject2 = new TestObject();
        PropertyAccessor<Object, Object> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_BOOLEAN);
        assertThat(accessor.getPropertyValue(testObject2), is(false));

        accessor.setPropertyValue(testObject2, true);

        assertThat(accessor.getPropertyValue(testObject2), is(true));
    }

    @Test
    void testIntProperty() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Integer> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_INT);
        assertEquals(42, accessor.getPropertyValue(testObject2).intValue());

        accessor.setPropertyValue(testObject2, 23);

        assertEquals(23, accessor.getPropertyValue(testObject2).intValue());
    }

    @Test
    void testCanReadWriteInvoke() {
        TestObject testObject2 = new TestObject();
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        assertThat((propertyAccessor.canWrite()), is(false));
        assertThat(propertyAccessor.canRead());
        assertThat(propertyAccessor.canInvoke(), is(false));

        Long propertyValue = propertyAccessor.getPropertyValue(testObject2);
        assertEquals(42, propertyValue.longValue());
    }

    @Test
    void testSetPropertyValue_readOnlyProperty() {
        TestObject testObject2 = new TestObject();

        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> accessor.setPropertyValue(testObject2, 5L));
    }

    @Test
    void testGetValueClass() {
        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        assertEquals(long.class, accessor.getValueClass());
    }

    @Test
    void testGetValueClass2() {
        PropertyAccessor<TestObject, Boolean> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_BOOLEAN);

        assertEquals(boolean.class, accessor.getValueClass());
    }

    @Test
    void testGetValueClassIllegalProperty() {
        PropertyAccessor<TestObject, ?> accessor = new PropertyAccessor<>(TestObject.class, "illegalProperty");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(accessor::getValueClass)
                .withStackTraceContaining(TestObject.class.getName())
                .withStackTraceContaining("illegalProperty");
    }

    @Test
    void testGetPropertyValue_ExceptionDuringInvocation() {
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        Long propertyValue = propertyAccessor.getPropertyValue(testObject);

        assertEquals(42, propertyValue.longValue());
    }

    /**
     * It should be possible to call default methods from implemented interface.
     */
    @Test
    void testDefaultMethod() {
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
    void testDefaultMethod_InSubclass() {
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
    void testDefaultMethod_OverwrittenInSubclass() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                TestInterfaceOverwriting.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new TestInterfaceOverwriting();
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("Hi", propertyValue);
    }

    @Test
    void testDefaultMethod_OtherPackage() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                OtherPackageTestObject.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new OtherPackageTestObject();
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("other", propertyValue);
    }

    @Test
    void testDefaultMethod_OtherPackagePrivate() {
        TestInterface testInterfaceImpl = OtherPackageTestObject.getPackagePrivateInstance();
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                testInterfaceImpl.getClass(),
                TestInterface.RO_DEFAULT_METHOD);
        String propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertEquals("otherPackage", propertyValue);
    }

    @Test
    void testDefaultMethod_OtherPackageProtected() {
        PropertyAccessor<TestPublicSubclass, Integer> propertyAccessor = new PropertyAccessor<>(
                TestPublicSubclass.class,
                TestPublicSubclass.PROPERTY_ANSWER);

        int propertyValue = propertyAccessor.getPropertyValue(new TestPublicSubclass());
        assertEquals(42, propertyValue);
    }

    @Test
    void testGenericInterface() {
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
     * On my machine with LambdaMetaFactory:
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
    @Disabled("Enable and change implementation to compare reflection with method handle")
    @Test
    void testPerformance() {
        PropertyAccessor<TestObject, Integer> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_INT);
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

    public interface TestGenericInterface<T> {
        T getFoo();

        void setFoo(T t);
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
