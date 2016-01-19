package org.linkki.test.cdi;
/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.lang3.NotImplementedException;

/**
 * A {@link Bean} implementation for tests that uses a fixed instance.
 */
public class TestBean<T> implements Bean<T> {

    private final T instance;

    public TestBean(T instance) {
        this.instance = instance;
    }

    @Override
    public T create(CreationalContext<T> creationalContext) {
        return instance;
    }

    @Override
    public void destroy(T someInstance, CreationalContext<T> creationalContext) {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Type> getTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Annotation> getQualifiers() {
        throw new NotImplementedException("");
    }

    @Override
    public Class<? extends Annotation> getScope() {
        throw new NotImplementedException("");
    }

    @Override
    public String getName() {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isAlternative() {
        throw new NotImplementedException("");
    }

    @Override
    public Class<?> getBeanClass() {
        throw new NotImplementedException("");
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isNullable() {
        throw new NotImplementedException("");
    }

}
