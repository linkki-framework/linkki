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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic.Kind;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.tooling.apt.validator.Messages;

/**
 * Utilities for dealing with dynamic fields.
 */
public final class DynamicMethodUtils {

    public static final String ASPECT_CREATION_FAILED = "ASPECT_CREATION_FAILED";

    private DynamicMethodUtils() {
        // util
    }

    /**
     *
     * @param method The model element that represents the annotated method
     * @param annotation the annotation that has the aspects
     * @return a set of all dynamic methods that are required.
     */
    public static Set<DynamicAspectMethodName> getExpectedMethods(
            ExecutableElement method,
            Annotation annotation,
            Messager messager) {
        List<LinkkiAspectDefinition> aspectDefinitions = AspectAnnotationReader
                .createAspectDefinitionsFrom(annotation);
        return getExpectedMethods(method, aspectDefinitions, messager);
    }

    /**
     *
     * @param method the model element that represents the annotated method
     * @param aspectDefinitions the aspect definitions of the annotation
     * @return a set of all dynamic methods that are required.
     */
    public static Set<DynamicAspectMethodName> getExpectedMethods(
            ExecutableElement method,
            List<LinkkiAspectDefinition> aspectDefinitions,
            Messager messager) {

        Set<DynamicAspectMethodName> expectedMethodsFromAvailableValuesAspectDefinitions = getExpectedMethodsFromAvailableValuesAspectDefinition(method,
                                                                                                                                                 aspectDefinitions,
                                                                                                                                                 messager);

        Set<DynamicAspectMethodName> expectedMethodsFromModelToUiAspectDefinition = getExpectedMethodsFromModelToUiAspectDefinition(method,
                                                                                                                                    aspectDefinitions,
                                                                                                                                    messager);

        Set<DynamicAspectMethodName> expectedMethodsFromCompositeAspectDefinition = getExpectedMethodsFromCompositeAspectDefinition(method,
                                                                                                                                    aspectDefinitions,
                                                                                                                                    messager);

        return Stream.of(expectedMethodsFromAvailableValuesAspectDefinitions,
                         expectedMethodsFromModelToUiAspectDefinition,
                         expectedMethodsFromCompositeAspectDefinition)
                .flatMap(Set::stream)
                .collect(toSet());
    }

    private static Set<DynamicAspectMethodName> getExpectedMethodsFromCompositeAspectDefinition(
            ExecutableElement method,
            List<LinkkiAspectDefinition> aspectDefinitions,
            Messager messager) {
        return aspectDefinitions.stream()
                .filter(CompositeAspectDefinition.class::isInstance)
                .map(CompositeAspectDefinition.class::cast)
                .flatMap(it -> {
                    Field field;
                    try {
                        field = CompositeAspectDefinition.class.getDeclaredField("aspectDefinitions");
                        field.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        List<LinkkiAspectDefinition> innerAspectDefinitions = (List<LinkkiAspectDefinition>)field
                                .get(it);
                        return getExpectedMethods(method, innerAspectDefinitions, messager).stream();
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                            | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toSet());
    }

    private static Set<DynamicAspectMethodName> getExpectedMethodsFromModelToUiAspectDefinition(
            ExecutableElement method,
            List<LinkkiAspectDefinition> aspectDefinitions,
            Messager messager) {

        return aspectDefinitions.stream()
                .filter(ModelToUiAspectDefinition.class::isInstance)
                .map(ModelToUiAspectDefinition.class::cast)
                .map(it -> createAspect(it, method, messager)
                        .map(aspect -> new AspectInfo(aspect, isBooleanModelToUiAspectDefinition(it))))
                .flatMap(Optional::stream)
                .filter(it -> !it.getAspect().isValuePresent())
                .filter(it -> !it.getAspect().getName().isEmpty())
                .map(it -> new DynamicAspectMethodName(
                        method,
                        it.getAspect().getName(),
                        it.isBooleanModelToUiAspectDefinition()))
                .collect(toSet());
    }

    private static Set<DynamicAspectMethodName> getExpectedMethodsFromAvailableValuesAspectDefinition(
            ExecutableElement method,
            List<LinkkiAspectDefinition> aspectDefinitions,
            Messager messager) {
        return aspectDefinitions.stream()
                .filter(AvailableValuesAspectDefinition.class::isInstance)
                .map(AvailableValuesAspectDefinition.class::cast)
                .map(it -> createAspect(it, method, messager))
                .flatMap(Optional::stream)
                .filter(it -> !it.isValuePresent())
                .filter(it -> !it.getName().isEmpty())
                .map(it -> new DynamicAspectMethodName(method, it.getName(), false))
                .collect(toSet());
    }

    private static Optional<Aspect<?>> createAspect(ModelToUiAspectDefinition<?> aspectDefinition,
            Element method,
            Messager messager) {
        try {
            return Optional.of(aspectDefinition.createAspect());
            // CSOFF: IllegalCatch
        } catch (RuntimeException e) {
            // CSON: IllegalCatch
            String msg = String.format(Messages.getString(ASPECT_CREATION_FAILED),
                                       aspectDefinition.getClass().getSimpleName(), e.getMessage());
            messager.printMessage(Kind.WARNING, msg, method);
            return Optional.empty();
        }
    }

    private static Optional<Aspect<?>> createAspect(AvailableValuesAspectDefinition<?> aspectDefinition,
            Element method,
            Messager messager) {
        try {
            return Optional.of(aspectDefinition.createAspect(VisibleType.class));
            // CSOFF: IllegalCatch
        } catch (RuntimeException e) {
            // CSON: IllegalCatch
            String msg = String.format(Messages.getString(ASPECT_CREATION_FAILED),
                                       aspectDefinition.getClass().getSimpleName(), e.getMessage());
            messager.printMessage(Kind.WARNING, msg, method);
            return Optional.empty();
        }
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
            ExecutableElement method,
            LinkkiAspectDefinition aspectDefinition) {
        if (aspectDefinition instanceof ModelToUiAspectDefinition<?>) {
            return asList(((ModelToUiAspectDefinition<?>)aspectDefinition).createAspect());
        } else if (aspectDefinition instanceof AvailableValuesAspectDefinition<?>) {
            return asList(((AvailableValuesAspectDefinition<?>)aspectDefinition).createAspect(
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
                                .flatMap(it -> getAspects(method, it).stream())
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

    private static class AspectInfo {
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

}
