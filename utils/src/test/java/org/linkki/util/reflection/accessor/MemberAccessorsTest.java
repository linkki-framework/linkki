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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.linkki.util.reflection.LookupProvider;

class MemberAccessorsTest {

    private static final String VALUE = "testValue";

    @Test
    void testGetValue() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getValue");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.getValue());
    }

    @Test
    void testGetValue_Field() throws NoSuchFieldException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredField("field");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.field);
    }

    @Test
    void testGetValue_Field_NativeType() throws NoSuchFieldException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredField("intField");

        int result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.intField);
    }

    @Test
    void testGetValue_Void() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getVoid");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MemberAccessors.getValue(testObject, fieldOrMethod));
    }

    @Test
    void testGetValue_NativeReturnType() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getInt");

        int result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo(testObject.getInt());
    }

    @Test
    void testGetValue_MethodThrowsException() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getThrowException");

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> MemberAccessors.getValue(testObject, fieldOrMethod))
                .withStackTraceContaining("any exception thrown");
    }

    @Test
    void testGetValue_PrivateMethod() throws NoSuchMethodException, SecurityException {
        var testObject = new TestObject();
        var fieldOrMethod = TestObject.class.getDeclaredMethod("getPrivateValue");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result).isEqualTo("privateValue");
    }

    @Test
    void testGetValue_UnsupportedMemberType() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Constructor<? extends TestObject> constructor = testObject.getClass().getDeclaredConstructor();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MemberAccessors.getValue(testObject, constructor));
    }

    @Test
    void testGetType_Field() throws NoSuchFieldException {
        Member field = TestObject.class.getDeclaredField("field");
        assertThat(MemberAccessors.getType(field, TestObject.class)).isEqualTo(String.class);
    }

    @Test
    void testGetType_Method() throws NoSuchMethodException {
        Member method = TestObject.class.getDeclaredMethod("getValue");
        assertThat(MemberAccessors.getType(method, TestObject.class)).isEqualTo(String.class);
    }

    @Test
    void testGetType_NotMethodOrField() throws NoSuchMethodException {
        TestObject testObject = new TestObject();
        Constructor<? extends TestObject> constructor = testObject.getClass().getDeclaredConstructor();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MemberAccessors.getType(constructor, TestObject.class))
                .withMessageContaining("Only field or method is supported")
                .withMessageContaining("java.lang.reflect.Constructor")
                .withMessageContaining(TestObject.class.getName());
    }

    @Test
    @SuppressWarnings("deprecation")
    void testGetType_GenericSuperClass_Deprecated() throws NoSuchMethodException {
        var method = GenericSuperClass.class.getMethod("getTestObject");
        // only the superclass of the return type is found
        assertThat(MemberAccessors.getType(method)).isEqualTo(TestObject.class);
    }

    @Test
    void testGetType_Method_GenericSuperClassImpl() throws NoSuchMethodException {
        var method = GenericSuperClass.class.getMethod("getTestObject");
        assertThat(MemberAccessors.getType(method, GenericSuperClassImpl.class)).isEqualTo(InheritedTestObject.class);
    }

    @Test
    void testGetType_Field_GenericSuperClassImpl() throws NoSuchFieldException {
        var field = GenericSuperClass.class.getField("testField");
        assertThat(MemberAccessors.getType(field, GenericSuperClassImpl.class)).isEqualTo(InheritedTestObject.class);
    }

    @Test
    void testGetType_Field_GenericSuperClass() throws NoSuchFieldException {
        var field = GenericSuperClass.class.getField("testField");
        assertThat(MemberAccessors.getType(field, GenericSuperClass.class)).isEqualTo(TestObject.class);
    }

    @Test
    void testGetType_Method_GenericSuperClass() throws NoSuchMethodException {
        var method = GenericSuperClass.class.getMethod("getTestObject");
        assertThat(MemberAccessors.getType(method, GenericSuperClass.class)).isEqualTo(TestObject.class);
    }

    @Test
    void testGetType_Field_FullyGenericSuperClass() throws NoSuchFieldException {
        var field = FullyGenericSuperClass.class.getField("testField");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MemberAccessors.getType(field, FullyGenericSuperClass.class))
                .withMessageContaining(FullyGenericSuperClass.class.getSimpleName())
                .withMessageContaining("testField");
    }

    @Test
    void testGetType_Method_FullyGenericSuperClass() throws NoSuchMethodException {
        var method = FullyGenericSuperClass.class.getMethod("getTestObject");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MemberAccessors.getType(method, FullyGenericSuperClass.class))
                .withMessageContaining(FullyGenericSuperClass.class.getSimpleName())
                .withMessageContaining("getTestObject");
    }

    @Test
    void testGetType_Field_MultipleGenericSuperClass() throws NoSuchFieldException {
        var field = MultipleGenericSuperClass.class.getField("testField");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MemberAccessors.getType(field, MultipleGenericSuperClass.class))
                .withMessageContaining(MultipleGenericSuperClass.class.getSimpleName())
                .withMessageContaining("testField")
                .withMessageContaining(TestObject.class.getSimpleName())
                .withMessageContaining(TestInterface.class.getSimpleName());
    }

    @Test
    void testGetType_Method_MultipleGenericSuperClass() throws NoSuchMethodException {
        var method = MultipleGenericSuperClass.class.getMethod("getTestObject");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MemberAccessors.getType(method, MultipleGenericSuperClass.class))
                .withMessageContaining(MultipleGenericSuperClass.class.getSimpleName())
                .withMessageContaining("getTestObject")
                .withMessageContaining(TestObject.class.getSimpleName())
                .withMessageContaining(TestInterface.class.getSimpleName());
    }

    @Test
    void testGetValue_DifferentClassloader() throws NoSuchMethodException,
            ClassNotFoundException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = classInCustomClassLoader.getDeclaredMethod("getValue");
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();

            assertThatNoException().isThrownBy(() -> MemberAccessors.getValue(instance, method));

            String result = MemberAccessors.getValue(instance, method);
            assertThat(result).isEqualTo(VALUE);
        }
    }

    @Test
    void testGetValue_DifferentClassloader_Exception() throws NoSuchMethodException,
            ClassNotFoundException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException {
        try (var simulatedRestartClassLoader = new SimulatedRestartClassLoader(TestObject.class)) {
            var classInCustomClassLoader = simulatedRestartClassLoader.loadClass(TestObject.class.getName());
            var method = classInCustomClassLoader.getDeclaredMethod("getThrowException");
            assertThat(method.getDeclaringClass().getClassLoader()).isNotEqualTo(LookupProvider.class.getClassLoader());
            var instance = classInCustomClassLoader.getDeclaredConstructor().newInstance();

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> MemberAccessors.getValue(instance, method))
                    .withStackTraceContaining("any exception thrown");
        }
    }

    private static class SimulatedRestartClassLoader extends URLClassLoader {

        private final ClassLoader delegate;
        private final Class<?>[] classes;

        public SimulatedRestartClassLoader(Class<?>... classes) {
            super(Stream.of(classes).map(c -> c.getProtectionDomain().getCodeSource().getLocation())
                    .toArray(URL[]::new), null);
            this.classes = classes;
            this.delegate = Thread.currentThread().getContextClassLoader();
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (Arrays.stream(classes).map(Class::getName).anyMatch(name::equals)) {
                return findClass(name);
            }
            return delegate.loadClass(name);
        }
    }

    public static class TestObject {

        private final String field = VALUE;

        private final int intField = ThreadLocalRandom.current().nextInt();

        public TestObject() {
            // nothing to do
        }

        public String getValue() {
            return field;
        }

        @SuppressWarnings("unused")
        public void getVoid() {
            return;
        }

        public int getInt() {
            return intField;
        }

        @SuppressWarnings("unused")
        public int getThrowException() {
            throw new RuntimeException("any exception thrown");
        }

        private String getPrivateValue() {
            return "privateValue";
        }
    }

    private static abstract class FullyGenericSuperClass<T> {

        public T testField;
        private T testObject;

        public T getTestObject() {
            return testObject;
        }

        public void setTestObject(T testObject) {
            this.testObject = testObject;
        }
    }

    private static abstract class MultipleGenericSuperClass<T extends TestObject & TestInterface> {

        public T testField;
        private T testObject;

        public T getTestObject() {
            return testObject;
        }

        public void setTestObject(T testObject) {
            this.testObject = testObject;
        }
    }

    private static abstract class GenericSuperClass<T extends TestObject> {
        public T testField;
        private T testObject;

        public T getTestObject() {
            return testObject;
        }

        public void setTestObject(T testObject) {
            this.testObject = testObject;
        }
    }

    private static class GenericSuperClassImpl extends GenericSuperClass<InheritedTestObject> {
        // nothing to do here
    }

    private static class InheritedTestObject extends TestObject {
        // nothing to do here
    }

    private static interface TestInterface {
        // nothing to do here
    }

}
