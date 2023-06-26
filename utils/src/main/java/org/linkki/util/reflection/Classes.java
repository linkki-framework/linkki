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

package org.linkki.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Utility for handling {@link Class Classes}.
 */
public class Classes {

    private Classes() {
        // prevent instantiation
    }

    /**
     * Wraps the call to the given type supplier with a try-catch-block to catch
     * {@link MirroredTypeException}. Returns the fallback type in case of a
     * {@link MirroredTypeException}.
     * <p>
     * This method must be used for all type access in annotations and aspects which may be loaded by
     * annotation processor. While processing the annotation the referenced type may not be compiled
     * yet. In this case a {@link MirroredTypeException} is thrown. The annotation processor should
     * proceed with the fallback type.
     * 
     * @param typeSupplier the supplier that accesses the type
     * @param aptFallback the fallback which is returned in case of a {@link MirroredTypeException}
     * @return The type returned by the supplier or the fallback in case of a
     *         {@link MirroredTypeException}
     */
    public static <T> Class<? extends T> getType(Supplier<Class<? extends T>> typeSupplier,
            Class<? extends T> aptFallback) {
        try {
            return typeSupplier.get();
        } catch (MirroredTypeException e) {
            return aptFallback;
        }
    }

    /**
     * Returns the name of the class which is provided by the given supplier. This method can be safely
     * used in an annotation processors.
     * 
     * @param classSupplier A supplier that gives the class for which the type name is requested
     * @return The qualified name of the class which is returned by the given supplier
     */
    public static String getTypeName(Supplier<? extends Class<?>> classSupplier) {
        try {
            return classSupplier.get().getName();
        } catch (MirroredTypeException mte) {
            DeclaredType typeMirror = (DeclaredType)mte.getTypeMirror();
            TypeElement typeElement = (TypeElement)typeMirror.asElement();
            return typeElement.getQualifiedName().toString();
        }
    }

    /**
     * Instantiates the class which is returned by the given supplier by calling the no-arguments
     * constructor. This method can be safely used in an annotation processors.
     * 
     * @param <T> the class
     * @param classSupplier The supplier which gives access to the class
     * @return an instance of the class
     * @throws IllegalArgumentException if the class has no accessible no-arguments constructor or if
     *             calling it fails for any reason
     */
    public static <T> T instantiate(Supplier<Class<? extends T>> classSupplier, Class<? extends T> aptFallback) {
        return instantiate(getType(classSupplier, aptFallback));
    }

    /**
     * Instantiates the given class with its no-arguments constructor.
     * 
     * @param <T> the class
     * @param clazz the class
     * @return an instance of the class
     * @throws IllegalArgumentException if the class has no accessible no-arguments constructor or if
     *             calling it fails for any reason
     */
    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz
                    .getConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    String.format("Cannot instantiate %s", clazz.getName()),
                    e);
        }
    }
}