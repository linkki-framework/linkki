/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.test.cdi;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;

public class TestInstance<T> implements Instance<T> {

    private Set<T> instances;

    public TestInstance(Collection<T> instances) {
        this.instances = new HashSet<>(instances);
    }

    @SafeVarargs
    public TestInstance(T... instances) {
        this.instances = Arrays.stream(instances).collect(Collectors.toSet());
    }

    @Override
    public Iterator<T> iterator() {
        return instances.iterator();
    }

    @Override
    public T get() {
        return instances.stream().findAny().get();
    }

    @Override
    public Instance<T> select(Annotation... qualifiers) {
        return this;
    }

    @Override
    public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
        return null;
    }

    @Override
    public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
        return null;
    }

    @Override
    public boolean isUnsatisfied() {
        return false;
    }

    @Override
    public boolean isAmbiguous() {
        return false;
    }

    @SafeVarargs
    public static <T> Instance<T> of(T... instances) {
        return new TestInstance<T>(instances);
    }

    public static <T> Instance<T> of(Collection<T> instances) {
        return new TestInstance<T>(instances);
    }

    public static <T> Instance<T> none() {
        return new TestInstance<T>(Collections.emptySet());
    }

}
