/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.cdi;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 * Helper class to access objects managed by the CDI {@link BeanManager}.
 */
public class BeanInstantiator {

    private final BeanManager beanManager;

    public BeanInstantiator(BeanManager beanManager) {
        super();
        this.beanManager = beanManager;
    }

    /**
     * Returns the instances of the given class with the given qualifier annotations using the
     * BeanManager.
     * 
     * @param beanClass the class of the beans to instantiate
     * @param qualifiers the qualifier annotations of the beans
     * @return the instances
     */
    public <T> Set<T> getInstances(Class<T> beanClass, Annotation... qualifiers) {
        Set<Bean<?>> beans = beanManager.getBeans(beanClass, qualifiers);
        Set<T> instances = new HashSet<>();
        // @formatter:off
        for (@SuppressWarnings("rawtypes") Bean bean : beans) {
            @SuppressWarnings("unchecked") Object o = beanManager.getReference(bean, beanClass, beanManager.createCreationalContext(bean));
            if (beanClass.isInstance(o)) {
                instances.add(beanClass.cast(o));
            }
        }
        // @formatter:on
        return instances;
    }

    /**
     * Uses a {@link JndiBeanManagerProvider} to get a CDI bean manager. Searches that bean manager
     * for all instances of the given class.
     * <ul>
     * <li>If no instance can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If multiple instances can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If exactly one instance is found, it is returned.</li>
     * </ul>
     * 
     * @param beanClass the class of the bean to instantiate
     */
    public static <T> T getCDIInstance(Class<T> beanClass) {
        JndiBeanManagerProvider beanManagerProvider = new JndiBeanManagerProvider();
        return new BeanInstantiator(beanManagerProvider.get()).getInstance(beanClass);
    }

    /**
     * Uses a {@link JndiBeanManagerProvider} to get a CDI bean manager. Searches that bean manager
     * for all instances of the given class with the given qualifier annotations.
     * <ul>
     * <li>If no instance can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If multiple instances can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If exactly one instance is found, it is returned.</li>
     * </ul>
     * 
     * @param beanClass the class of the bean to instantiate
     * @param qualifiers the qualifier annotations of the bean to instantiate
     */
    public static <T> T getCDIInstance(Class<T> beanClass, Annotation... qualifiers) {
        JndiBeanManagerProvider beanManagerProvider = new JndiBeanManagerProvider();
        return new BeanInstantiator(beanManagerProvider.get()).getInstance(beanClass, qualifiers);
    }

    /**
     * Searches the bean manager for all instances of the given class with the given qualifiers.
     * <ul>
     * <li>If no instance can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If multiple instances can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If exactly one instance is found, it is returned.</li>
     * </ul>
     * 
     * @param beanClass the class of the bean to instantiate
     * @param qualifiers the qualifier annotations of the bean to instantiate
     */
    public <T> T getInstance(Class<T> beanClass, Annotation... qualifiers) {
        Set<T> instances = getInstances(beanClass, qualifiers);
        checkExactlyOneInstance(beanClass, instances);
        @SuppressWarnings("null")
        @Nonnull
        T next = instances.iterator().next();
        return next;
    }

    private <T> void checkExactlyOneInstance(Class<T> beanClass, Set<T> instances) {
        if (instances.isEmpty()) {
            throw new IllegalStateException("Cannot find any instance of class " + beanClass);
        }
        if (instances.size() > 1) {
            throw new IllegalStateException("Multiple instances found. Don't know which one to use " + instances);
        }
    }
}
