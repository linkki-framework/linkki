package org.linkki.test.cdi;
/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.enterprise.inject.spi.ObserverMethod;

import org.apache.commons.lang3.NotImplementedException;

/**
 * A {@link BeanManager} implementation for tests that uses a set of objects it "manages" beans for.
 */
public class TestBeanManager implements BeanManager {

    private final Set<Object> managedInstances = new HashSet<>();

    public TestBeanManager() {
        super();
    }

    public TestBeanManager(Collection<Object> instances) {
        super();
        managedInstances.addAll(instances);
    }

    public TestBeanManager(Object... instances) {
        super();
        managedInstances.addAll(Arrays.asList(instances));
    }

    public void addInstance(Object o) {
        this.managedInstances.add(o);
    }

    @Override
    public <T> CreationalContext<T> createCreationalContext(Contextual<T> contextual) {
        return new TestCreationalContext<T>();
    }

    @Override
    public Set<Bean<?>> getBeans(Type beanType, Annotation... qualifiers) {
        Class<?> beanClass = (Class<?>)beanType;
        // @formatter:off
        return managedInstances.stream()
                .filter(o -> beanClass.isInstance(o))
                .map(o -> new TestBean<>(o))
                .collect(Collectors.toSet());
        // @formatter:on
    }

    @Override
    public Object getReference(Bean<?> bean, Type beanType, CreationalContext<?> ctx) {
        throw new NotImplementedException("");
    }

    @Override
    public Object getInjectableReference(InjectionPoint ij, CreationalContext<?> ctx) {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Bean<?>> getBeans(String name) {
        throw new NotImplementedException("");
    }

    @Override
    public Bean<?> getPassivationCapableBean(String id) {
        throw new NotImplementedException("");
    }

    @Override
    public <X> Bean<? extends X> resolve(Set<Bean<? extends X>> beans) {
        throw new NotImplementedException("");
    }

    @Override
    public void validate(InjectionPoint injectionPoint) {
        throw new NotImplementedException("");

    }

    @Override
    public void fireEvent(Object event, Annotation... qualifiers) {
        throw new NotImplementedException("");

    }

    @Override
    public <T> Set<ObserverMethod<? super T>> resolveObserverMethods(T event, Annotation... qualifiers) {
        throw new NotImplementedException("");
    }

    @Override
    public List<Decorator<?>> resolveDecorators(Set<Type> types, Annotation... qualifiers) {
        throw new NotImplementedException("");
    }

    @Override
    public List<Interceptor<?>> resolveInterceptors(InterceptionType type, Annotation... interceptorBindings) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isScope(Class<? extends Annotation> annotationType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isNormalScope(Class<? extends Annotation> annotationType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isPassivatingScope(Class<? extends Annotation> annotationType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isQualifier(Class<? extends Annotation> annotationType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isInterceptorBinding(Class<? extends Annotation> annotationType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isStereotype(Class<? extends Annotation> annotationType) {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Annotation> getInterceptorBindingDefinition(Class<? extends Annotation> bindingType) {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Annotation> getStereotypeDefinition(Class<? extends Annotation> stereotype) {
        throw new NotImplementedException("");
    }

    @Override
    public Context getContext(Class<? extends Annotation> scopeType) {
        throw new NotImplementedException("");
    }

    @Override
    public ELResolver getELResolver() {
        throw new NotImplementedException("");
    }

    @Override
    public ExpressionFactory wrapExpressionFactory(ExpressionFactory expressionFactory) {
        throw new NotImplementedException("");
    }

    @Override
    public <T> AnnotatedType<T> createAnnotatedType(Class<T> type) {
        throw new NotImplementedException("");
    }

    @Override
    public <T> InjectionTarget<T> createInjectionTarget(AnnotatedType<T> type) {
        throw new NotImplementedException("");
    }

}
