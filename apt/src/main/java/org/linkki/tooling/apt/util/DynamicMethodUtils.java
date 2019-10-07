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


package org.linkki.tooling.apt.util;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;

/**
 * Utilities for dealing with dynamic fields.
 */
public final class DynamicMethodUtils {

    private DynamicMethodUtils() {
        // util
    }

    /**
     *
     * @param methodName name of the annotated method
     * @param annotation the annotation that has the aspects
     * @return a set of all dynamic methods that are required.
     */
    public static Set<DynamicAspectMethodName> getExpectedMethods(
            String methodName,
            Annotation annotation) {
        List<LinkkiAspectDefinition> aspectDefinitions = AspectAnnotationReader
                .createAspectDefinitionsFrom(annotation);
        return getExpectedMethods(methodName, aspectDefinitions);
    }

    /**
     *
     * @param methodName name of the annotated method
     * @param aspectDefinitions the aspect definitions of the annotation
     * @return a set of all dynamic methods that are required.
     */
    public static Set<DynamicAspectMethodName> getExpectedMethods(
            String methodName,
            List<LinkkiAspectDefinition> aspectDefinitions) {

        Set<DynamicAspectMethodName> expectedMethodsFromAvailableValuesAspectDefinitions = getExpectedMethodsFromAvailableValuesAspectDefinition(methodName,
                                                                                                                                                 aspectDefinitions);

        Set<DynamicAspectMethodName> expectedMethodsFromModelToUiAspectDefinition = getExpectedMethodsFromModelToUiAspectDefinition(methodName,
                                                                                                                                    aspectDefinitions);

        Set<DynamicAspectMethodName> expectedMethodsFromCompositeAspectDefinition = getExpectedMethodsFromCompositeAspectDefinition(methodName,
                                                                                                                                    aspectDefinitions);

        return Stream.concat(
                             Stream.concat(
                                           expectedMethodsFromAvailableValuesAspectDefinitions.stream(),
                                           expectedMethodsFromModelToUiAspectDefinition.stream()),
                             expectedMethodsFromCompositeAspectDefinition.stream())
                .collect(toSet());
    }

    private static Set<DynamicAspectMethodName> getExpectedMethodsFromCompositeAspectDefinition(
            String methodName,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        return aspectDefinitions.stream()
                .filter(it -> it instanceof CompositeAspectDefinition)
                .map(it -> (CompositeAspectDefinition)it)
                .flatMap(it -> {
                    Field field;
                    try {
                        field = CompositeAspectDefinition.class.getDeclaredField("aspectDefinitions");
                        field.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        List<LinkkiAspectDefinition> innerAspectDefinitions = (List<LinkkiAspectDefinition>)field
                                .get(it);
                        return getExpectedMethods(methodName, innerAspectDefinitions).stream();
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                            | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toSet());
    }

    private static Set<DynamicAspectMethodName> getExpectedMethodsFromModelToUiAspectDefinition(
            String methodName,
            List<LinkkiAspectDefinition> aspectDefinitions) {

        class AspectInfo {
            private final Aspect<?> aspect;
            private final boolean isBooleanModelToUiAspectDefinition;

            public AspectInfo(Aspect<?> aspect, boolean isBooleanModelToUiAspectDefinition) {
                this.aspect = aspect;
                this.isBooleanModelToUiAspectDefinition = isBooleanModelToUiAspectDefinition;
            }

            public Aspect<?> getAspect() {
                return aspect;
            }

            public boolean isBooleanModelToUiAspectDefinition() {
                return isBooleanModelToUiAspectDefinition;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + aspect.hashCode();
                result = prime * result + (isBooleanModelToUiAspectDefinition ? 1231 : 1237);
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                AspectInfo other = (AspectInfo)obj;
                if (!aspect.equals(other.aspect)) {
                    return false;
                }
                if (isBooleanModelToUiAspectDefinition != other.isBooleanModelToUiAspectDefinition) {
                    return false;
                }
                return true;
            }

        }

        return aspectDefinitions.stream()
                .filter(it -> it instanceof ModelToUiAspectDefinition<?>)
                .map(it -> (ModelToUiAspectDefinition<?>)it)
                .map(it -> new AspectInfo(it.createAspect(), isBooleanModelToUiAspectDefinition(it)))
                .filter(it -> !it.getAspect().isValuePresent())
                .filter(it -> !it.getAspect().getName().isEmpty())
                .map(it -> new DynamicAspectMethodName(
                        methodName,
                        it.getAspect().getName(),
                        it.isBooleanModelToUiAspectDefinition()))
                .collect(toSet());
    }

    private static Set<DynamicAspectMethodName> getExpectedMethodsFromAvailableValuesAspectDefinition(
            String methodName,
            List<LinkkiAspectDefinition> aspectDefinitions) {
        Class<? extends Enum<?>> valueClass = VisibleType.class;
        return aspectDefinitions.stream()
                .filter(it -> it instanceof AvailableValuesAspectDefinition<?>)
                .map(it -> (AvailableValuesAspectDefinition<?>)it)
                .map(it -> it.createAspect(MethodNameUtils.toPropertyName(methodName), valueClass))
                .filter(it -> !it.isValuePresent())
                .filter(it -> !it.getName().isEmpty())
                .map(it -> new DynamicAspectMethodName(methodName, it.getName(), false))
                .collect(toSet());
    }

    private static boolean isBooleanModelToUiAspectDefinition(ModelToUiAspectDefinition<?> aspectDefinition) {
        Class<?> currentClass = aspectDefinition.getClass();
        Class<?> superClass = currentClass.getSuperclass();

        while (!superClass.equals(ModelToUiAspectDefinition.class)) {
            if (superClass.equals(LinkkiAspectDefinition.class)) {
                return false;
            }

            currentClass = superClass;
            superClass = superClass.getSuperclass();
        }

        Type genericSuperclass = currentClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return asList(((ParameterizedType)genericSuperclass).getActualTypeArguments())
                    .stream()
                    .anyMatch(it -> it.getTypeName().equals(Boolean.TYPE.getTypeName()) ||
                            it.getTypeName().equals(Boolean.class.getTypeName()));
        }

        return false;
    }

    public static List<Aspect<?>> getAspects(
            String methodName,
            LinkkiAspectDefinition aspectDefinition) {
        if (aspectDefinition instanceof ModelToUiAspectDefinition<?>) {
            return asList(((ModelToUiAspectDefinition<?>)aspectDefinition).createAspect());
        } else if (aspectDefinition instanceof AvailableValuesAspectDefinition<?>) {
            String propertyName = MethodNameUtils.toPropertyName(methodName);
            return asList(((AvailableValuesAspectDefinition<?>)aspectDefinition).createAspect(propertyName,
                                                                                              VisibleType.class));
        } else if (aspectDefinition instanceof CompositeAspectDefinition) {
            return AccessController.doPrivileged(new PrivilegedAction<List<Aspect<?>>>() {
                @Override
                public List<Aspect<?>> run() {
                    Field field;
                    try {
                        field = CompositeAspectDefinition.class.getDeclaredField("aspectDefinitions");
                        field.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        List<LinkkiAspectDefinition> innerAspectDefinitions = (List<LinkkiAspectDefinition>)field
                                .get(aspectDefinition);
                        return innerAspectDefinitions.stream()
                                .flatMap(it -> getAspects(methodName, it).stream())
                                .collect(toList());
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                            | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        } else {
            return emptyList();
        }
    }


}
