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

package org.linkki.core.binding.descriptor.modelobject;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.linkki.core.pmo.ModelObject;
import org.linkki.util.BeanUtils;
import org.linkki.util.reflection.accessor.MemberAccessors;

/**
 * Utility class to access the model object of a PMO.
 * <p>
 * A model object in a PMO is defined by a field or method that is annotated with the annotation
 * {@link ModelObject}. The value may change during the lifetime of a PMO - that's why only a supplier
 * for the model object is returned. The supplier should be called whenever a model object is required.
 */
public class ModelObjects {

    private static final Map<Key, Optional<Member>> CACHE = new ConcurrentHashMap<>();

    private final Class<?> pmoClass;
    private final String modelObjectName;

    private ModelObjects(Class<?> pmoClass, String modelObjectName) {
        this.pmoClass = requireNonNull(pmoClass, "pmoClass must not be null");
        this.modelObjectName = requireNonNull(modelObjectName, "modelObjectName must not be null");
    }

    private Optional<Member> getAccessMember() {
        Optional<Member> annotatedMethod = getModelObjectMethod();
        Optional<Member> annotatedField = getModelObjectField();
        if (annotatedMethod.isPresent() && annotatedField.isPresent()) {
            throw ModelObjectAnnotationException.multipleMembersAnnotated(pmoClass, modelObjectName,
                                                                          annotatedMethod.get(),
                                                                          annotatedField.get());
        }
        return annotatedMethod.or(() -> annotatedField);
    }

    private Optional<Member> getModelObjectMethod() {
        Optional<Method> method = BeanUtils.getMethods(pmoClass,
                                                       (m) -> m.isAnnotationPresent(ModelObject.class)
                                                               && requireNonNull(m.getAnnotation(ModelObject.class))
                                                                       .name()
                                                                       .equals(modelObjectName))
                .reduce((f1, f2) -> {
                    throw ModelObjectAnnotationException.multipleMembersAnnotated(pmoClass, modelObjectName, f1, f2);
                });
        method.ifPresent(m -> {
            if (Void.TYPE.equals(m.getReturnType())) {
                throw ModelObjectAnnotationException.voidMethod(pmoClass, m);
            }
        });

        return method.map(Member.class::cast);
    }

    private Optional<Member> getModelObjectField() {
        Optional<Field> field = FieldUtils
                .getFieldsListWithAnnotation(pmoClass, ModelObject.class)
                .stream()
                .filter(f -> requireNonNull(f.getAnnotation(ModelObject.class)).name().equals(modelObjectName))
                .reduce((f1, f2) -> {
                    throw ModelObjectAnnotationException.multipleMembersAnnotated(pmoClass, modelObjectName, f1, f2);
                });
        field.ifPresent(f -> {
            if (Void.TYPE.equals(f.getType())) {
                throw ModelObjectAnnotationException.voidField(pmoClass, f);
            }
        });
        return field.map(Member.class::cast);
    }

    /**
     * Reads the given presentation model object's class to find a method or field annotated with
     * {@link ModelObject @ModelObject} and the annotation's {@link ModelObject#name()} matching the
     * given model object name. Returns a supplier that supplies a model object by invoking that method
     * or retrieving the field value.
     *
     * @param pmo a presentation model object
     * @param modelObjectName the name of the model object as provided by a method/field annotated with
     *            {@link ModelObject @ModelObject}
     *
     * @return a supplier that supplies a model object by invoking the annotated method or retrieving
     *         the field value
     *
     * @throws ModelObjectAnnotationException if no matching method or field is found, the method has no
     *             return value, the field has the type {@link Void} or multiple annotations for the
     *             same model object name are present.
     */
    public static Supplier<Object> supplierFor(Object pmo, String modelObjectName) {
        Member accessMember = getModelObjectAccessMember(pmo, modelObjectName)
                .orElseThrow(() -> ModelObjectAnnotationException.noAnnotatedMember(pmo.getClass(), modelObjectName));
        return () -> MemberAccessors.getValue(pmo, accessMember);
    }

    /**
     * Tests whether the presentation model object has an accessible method or field annotated with
     * {@link ModelObject @ModelObject} using the given name.
     *
     * @param pmo an object used for a presentation model
     * @param modelObjectName the name of the model object
     *
     * @return whether the object has a method annotated with {@link ModelObject @ModelObject} using the
     *         given name
     * @throws ModelObjectAnnotationException if multiple annotations for the model object name are
     *             present
     */
    public static boolean isAccessible(Object pmo, String modelObjectName) {
        return getModelObjectAccessMember(pmo, modelObjectName).isPresent();
    }

    /**
     * Returns the accessible method or field that is annotated with {@link ModelObject @ModelObject} using the given name
     * if there is any.
     *
     * @param pmo an object used for a presentation model
     * @param modelObjectName the name of the model object
     *
     * @return method or field to access the model object with the given name
     * @throws ModelObjectAnnotationException if multiple annotations for the model object name are
     *             present
     */
    public static Optional<Member> getModelObjectAccessMember(Object pmo, String modelObjectName) {
        return CACHE.computeIfAbsent(new Key(pmo.getClass(), modelObjectName),
                                     key -> new ModelObjects(key.pmoClass, key.modelObjectName).getAccessMember());
    }

    /**
     * Thrown when trying to get a method annotated with {@link ModelObject @ModelObject} via
     * {@link ModelObjects#supplierFor(Object, String)} fails.
     */
    public static final class ModelObjectAnnotationException extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;

        public ModelObjectAnnotationException(String description) {
            super(description);
        }

        private static ModelObjectAnnotationException noAnnotatedMember(Class<?> pmoClass, String modelObjectName) {
            return new ModelObjectAnnotationException("Presentation model object " + pmoClass.getName()
                    + " has no method or field annotated with " + getDescriptionForAnnotation(modelObjectName));
        }

        private static ModelObjectAnnotationException voidField(Class<?> pmoClass, Field field) {
            return new ModelObjectAnnotationException(
                    "Presentation model object " + pmoClass.getName() + "'s field " + field.getName()
                            + " is annotated with @"
                            + ModelObject.class.getSimpleName() + " but is of type Void");
        }

        private static ModelObjectAnnotationException voidMethod(Class<?> pmoClass, Method method) {
            return new ModelObjectAnnotationException(
                    "Presentation model object " + pmoClass.getName() + "'s method " + method.getName()
                            + " is annotated with @"
                            + ModelObject.class.getSimpleName() + " but returns void");
        }

        private static ModelObjectAnnotationException multipleMembersAnnotated(Class<?> pmoClass,
                String modelObjectName,
                Member... annotatedMembers) {
            return new ModelObjectAnnotationException(String.format(
                                                                    "Presentation model object %s has multiple members (%s) that are annotated with %s",
                                                                    pmoClass.getName(),
                                                                    Arrays.stream(annotatedMembers).map(Member::getName)
                                                                            .collect(Collectors.joining(", ")),
                                                                    getDescriptionForAnnotation(modelObjectName)));
        }

        private static String getDescriptionForAnnotation(String modelObjectName) {
            String annotation = "@" + ModelObject.class.getSimpleName();
            return ModelObject.DEFAULT_NAME.equals(modelObjectName)
                    ? annotation
                    : annotation + " for the model object named \"" + modelObjectName + "\"";
        }
    }

    private static class Key {

        private final Class<?> pmoClass;
        private final String modelObjectName;

        public Key(Class<?> pmoClass, String modelObjectName) {
            this.pmoClass = pmoClass;
            this.modelObjectName = modelObjectName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((modelObjectName == null) ? 0 : modelObjectName.hashCode());
            result = prime * result + ((pmoClass == null) ? 0 : pmoClass.hashCode());
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
            Key other = (Key)obj;
            if (modelObjectName == null) {
                if (other.modelObjectName != null) {
                    return false;
                }
            } else if (!modelObjectName.equals(other.modelObjectName)) {
                return false;
            }
            if (pmoClass == null) {
                if (other.pmoClass != null) {
                    return false;
                }
            } else if (!pmoClass.equals(other.pmoClass)) {
                return false;
            }
            return true;
        }

    }

}
