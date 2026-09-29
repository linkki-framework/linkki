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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        var propertyValue = stringAccessor.getPropertyValue(testObject);

        assertThat(propertyValue).isEqualTo(STRING_PROPERTY_INITIAL_VALUE);
    }

    @Test
    void testSetPropertyValue() {
        assertThat(testObject.getStringProperty()).isEqualTo(STRING_PROPERTY_INITIAL_VALUE);

        stringAccessor.setPropertyValue(testObject, "anotherValue");

        assertThat(testObject.getStringProperty()).isEqualTo("anotherValue");
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
        assertThat(testObject.isBooleanProperty()).isFalse();
        var accessor = new PropertyAccessor<>(TestObject.class, TestObject.PROPERTY_DO_SOMETHING);

        accessor.invoke(testObject);

        assertThat(testObject.isBooleanProperty()).isTrue();
    }

    @Test
    void testConstructor() {
        var instance = new TestObject();
        instance.setBooleanProperty(true);

        var accessor = new PropertyAccessor<>(TestObject.class, TestObject.PROPERTY_BOOLEAN);

        assertThat(accessor.getPropertyValue(instance)).isEqualTo(true);
    }

    @Test
    void testConstructor_WrongProperty() {
        var propertyAccessor = new PropertyAccessor<>(Object.class, "doesNotExist");

        assertThat(propertyAccessor.canRead()).isFalse();
        assertThat(propertyAccessor.canWrite()).isFalse();
        assertThat(propertyAccessor.canInvoke()).isFalse();
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
        assertThat(accessor.getPropertyValue(testObject2)).isEqualTo(false);

        accessor.setPropertyValue(testObject2, true);

        assertThat(accessor.getPropertyValue(testObject2)).isEqualTo(true);
    }

    @Test
    void testIntProperty() {
        var testObject2 = new TestObject();
        PropertyAccessor<TestObject, Integer> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_INT);
        assertThat(accessor.getPropertyValue(testObject2)).isEqualTo(42);

        accessor.setPropertyValue(testObject2, 23);

        assertThat(accessor.getPropertyValue(testObject2)).isEqualTo(23);
    }

    @Test
    void testCanReadWriteInvoke() {
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        assertThat(propertyAccessor.canWrite()).isFalse();
        assertThat(propertyAccessor.canRead()).isTrue();
        assertThat(propertyAccessor.canInvoke()).isFalse();

        var testObject2 = new TestObject();
        var propertyValue = propertyAccessor.getPropertyValue(testObject2);
        assertThat(propertyValue).isEqualTo(42L);
    }

    @Test
    void testSetPropertyValue_readOnlyProperty() {
        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);
        var testObject2 = new TestObject();

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> accessor.setPropertyValue(testObject2, 5L));
    }

    @Test
    void testGetValueClass() {
        PropertyAccessor<TestObject, Long> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        assertThat(accessor.getValueClass()).isEqualTo(long.class);
    }

    @Test
    void testGetValueClass2() {
        PropertyAccessor<TestObject, Boolean> accessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_BOOLEAN);

        assertThat(accessor.getValueClass()).isEqualTo(boolean.class);
    }

    @Test
    void testGetValueClassIllegalProperty() {
        var accessor = new PropertyAccessor<TestObject, Object>(TestObject.class, "illegalProperty");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(accessor::getValueClass)
                .withStackTraceContaining(TestObject.class.getName())
                .withStackTraceContaining("illegalProperty");
    }

    @Test
    void testGetPropertyValue_ExceptionDuringInvocation() {
        PropertyAccessor<TestObject, Long> propertyAccessor = new PropertyAccessor<>(TestObject.class,
                TestObject.PROPERTY_READ_ONLY_LONG);

        var propertyValue = propertyAccessor.getPropertyValue(new TestObject());

        assertThat(propertyValue).isEqualTo(42L);
    }

    /**
     * It should be possible to call default methods from implemented interface.
     */
    @Test
    void testDefaultMethod() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(TestInterfaceImpl.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new TestInterfaceImpl();
        var propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertThat(propertyValue).isEqualTo("Hello");
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
        var propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertThat(propertyValue).isEqualTo("Hello");
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
        var propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertThat(propertyValue).isEqualTo("Hi");
    }

    @Test
    void testDefaultMethod_OtherPackage() {
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                OtherPackageTestObject.class,
                TestInterface.RO_DEFAULT_METHOD);
        TestInterface testInterfaceImpl = new OtherPackageTestObject();
        var propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertThat(propertyValue).isEqualTo("other");
    }

    @Test
    void testDefaultMethod_OtherPackagePrivate() {
        var testInterfaceImpl = OtherPackageTestObject.getPackagePrivateInstance();
        PropertyAccessor<TestInterface, String> propertyAccessor = new PropertyAccessor<>(
                testInterfaceImpl.getClass(),
                TestInterface.RO_DEFAULT_METHOD);
        var propertyValue = propertyAccessor.getPropertyValue(testInterfaceImpl);
        assertThat(propertyValue).isEqualTo("otherPackage");
    }

    @Test
    void testDefaultMethod_OtherPackageProtected() {
        PropertyAccessor<TestPublicSubclass, Integer> propertyAccessor = new PropertyAccessor<>(
                TestPublicSubclass.class,
                TestPublicSubclass.PROPERTY_ANSWER);

        int propertyValue = propertyAccessor.getPropertyValue(new TestPublicSubclass());
        assertThat(propertyValue).isEqualTo(42);
    }

    @Test
    void testGenericInterface() {
        PropertyAccessor<TestGenericInterfaceImpl, String> propertyAccessor = new PropertyAccessor<>(
                TestGenericInterfaceImpl.class,
                "foo");
        var testImpl = new TestGenericInterfaceImpl();
        var propertyValue = propertyAccessor.getPropertyValue(testImpl);
        assertThat(propertyValue).isEqualTo("bar");

        propertyAccessor.setPropertyValue(testImpl, "baz");
        propertyValue = propertyAccessor.getPropertyValue(testImpl);
        assertThat(propertyValue).isEqualTo("baz");
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
            var start = System.currentTimeMillis();
            for (int i = 0; i < l; i++) {
                accessor.setPropertyValue(testObject, i);
                assertThat(accessor.getPropertyValue(testObject)).isEqualTo(i);
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
