/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.util.cdi;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

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
        this.beanManager = requireNonNull(beanManager, "beanManager must not be null");
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
     * for all instances of the given class.
     * <ul>
     * <li>If CDI is not running: returns the fall back implementation</li>
     * <li>If no instance can be found: returns the fall back implementation</li>
     * <li>If multiple instances can be found: an {@link IllegalStateException} is thrown.</li>
     * <li>If exactly one instance is found, it is returned.</li>
     * </ul>
     * 
     * @param beanClass the class of the bean to instantiate
     * @param fallbackSupplier a fall back {@link Supplier} for the given bean class
     */
    public static <T> T getCDIInstance(Class<T> beanClass, Supplier<T> fallbackSupplier) {
        try {
            JndiBeanManagerProvider beanManagerProvider = new JndiBeanManagerProvider();
            BeanManager beanManager = beanManagerProvider.get();
            BeanInstantiator beanInstantiator = new BeanInstantiator(beanManager);
            Set<T> instances = beanInstantiator.getInstances(beanClass);
            beanInstantiator.checkAtMostOneInstance(instances);
            if (instances.isEmpty()) {
                return fallbackSupplier.get();
            } else {
                return instances.iterator().next();
            }
        } catch (IllegalStateException ise) {
            return fallbackSupplier.get();
        }
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
        checkAtLeastOneInstance(beanClass, instances);
        checkAtMostOneInstance(instances);
    }

    private <T> void checkAtMostOneInstance(Set<T> instances) {
        if (instances.size() > 1) {
            throw new IllegalStateException("Multiple instances found. Don't know which one to use " + instances);
        }
    }

    private <T> void checkAtLeastOneInstance(Class<T> beanClass, Set<T> instances) {
        if (instances.isEmpty()) {
            throw new IllegalStateException("Cannot find any instance of class " + beanClass);
        }
    }
}
