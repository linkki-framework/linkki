/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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

    @Test
    public void testgetMethod_oneMatchingMethodIsFound() {
        Optional<Method> method = BeanUtils.getMethod(Foo.class, (m) -> m.getName().equals("baz"));
        assertThat(method, is(present()));
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
        void superFoo() {
            // do nothing
        }
    }

    public static class Foo extends SuperFoo {
        public void foo() {
            // do nothing
        }

        @SuppressWarnings("unused")
        private void bar() {
            // do nothing
        }

        protected int baz() {
            return 0;
        }

        protected int baz(int foo) {
            return foo + 1;
        }
    }

}
