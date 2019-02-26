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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.linkki.test.matcher.Matchers.present;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;

public class BeanUtilsTest {

    @Test
    public void testgetMethod_publicMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("foo"));
        assertThat(method, is(present()));
    }

    @Test
    public void testgetMethod_interfaceMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("iFoo"));
        assertThat(method, is(present()));
    }

    @Test
    public void testgetMethod_superTypeMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("superFoo"));
        assertThat(method, is(present()));
    }

    @Test
    public void testgetMethod_privateMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("bar"));
        assertThat(method, is(present()));
    }

    @Test(expected = IllegalStateException.class)
    public void testgetMethod_oneMatchingMethodIsFound() {
        BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("baz"));
    }

    @Test
    public void testgetMethods_allMatchingMethodsAreFound() {
        Stream<Method> declaredMethods = BeanUtils.getMethods(Foo.class, (m) -> m.getName().equals("baz"));
        assertThat(declaredMethods.count(), is(2L));
    }

    @Test
    public void testgetMethod_NoMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("hammaned"));
        assertThat(method, is(not(present())));
    }

    private static interface IFoo {
        default void iFoo() {
            // do nothing
        }
    }

    public static class SuperFoo implements IFoo {
        public void superFoo() {
            // do nothing
        }
    }

    public static class Foo extends SuperFoo {
        public void foo() {
            // do nothing
        }

        public void bar() {
            // do nothing
        }

        public int baz() {
            return 0;
        }

        public int baz(int foo) {
            return foo + 1;
        }
    }

}
