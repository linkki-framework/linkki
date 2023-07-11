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

package org.linkki.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberAccessorsTest {

    private static final String VALUE = "testValue";

    @Test
    void testFieldAccess() throws NoSuchFieldException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredField("field");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result, is(testObject.field));
    }

    @Test
    void testFieldAccess_NativeType() throws NoSuchFieldException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredField("intField");

        int result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result, is(testObject.intField));
    }

    @Test
    void testGetValue() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getValue");

        String result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result, is(testObject.getValue()));
    }

    @Test
    void testGetValue_Void() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getVoid");

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> MemberAccessors.getValue(testObject, fieldOrMethod));
    }

    @Test
    void testGetValue_NativeReturnType() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getInt");

        int result = MemberAccessors.getValue(testObject, fieldOrMethod);

        assertThat(result, is(testObject.getInt()));
    }

    @Test
    void testGetValue_MethodThrowsException() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Member fieldOrMethod = TestObject.class.getDeclaredMethod("getThrowException");

        assertThrows(RuntimeException.class, () -> MemberAccessors.getValue(testObject, fieldOrMethod));
    }

    @Test
    void testGetValue_UnsupportedMemberType() throws NoSuchMethodException, SecurityException {
        TestObject testObject = new TestObject();
        Constructor<? extends TestObject> constructor = testObject.getClass().getDeclaredConstructor();

        assertThrows(IllegalArgumentException.class, () -> MemberAccessors.getValue(testObject, constructor));
    }

    @Test
    void testGetType_Field() throws NoSuchFieldException{
        Member field = TestObject.class.getDeclaredField("field");
        assertThat(MemberAccessors.getType(field), is(String.class));
    }

    @Test
    void testGetType_Method() throws NoSuchMethodException {
        Member method = TestObject.class.getDeclaredMethod("getValue");
        assertThat(MemberAccessors.getType(method), is(String.class));
    }

    @Test
    void testGetType_NotMethodOrField() throws NoSuchMethodException {
        TestObject testObject = new TestObject();
        Constructor<? extends TestObject> constructor = testObject.getClass().getDeclaredConstructor();
        var illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> MemberAccessors.getType(constructor));
        assertThat(illegalArgumentException.getMessage(),
                is("Only field or method is supported, found java.lang.reflect.Constructor as type of org.linkki.util.MemberAccessorsTest.TestObject#org.linkki.util.MemberAccessorsTest$TestObject"));
    }

    private static class TestObject {

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

    }

}
