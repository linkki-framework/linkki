/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.util.reflection.accessor;

import static java.util.Objects.requireNonNull;
import static org.linkki.util.BeanUtils.GET_PREFIX;
import static org.linkki.util.BeanUtils.IS_PREFIX;
import static org.linkki.util.BeanUtils.SET_PREFIX;
import static org.linkki.util.LazyReference.lazy;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * This class wraps a {@link PropertyDescriptor} for a bound class and a specified property
 * identified by its name. It could create a {@link WriteMethod} or {@link ReadMethod} object for
 * the bound class and property using the methods {@link #createWriteMethod()} and
 * {@link #createReadMethod()}.
 * 
 * @param <T> the type containing the property
 * @param <V> the property's type
 */
class PropertyAccessDescriptor<T, V> {

    private final Class<? extends T> boundClass;
    private final String propertyName;
    private final Supplier<Optional<Method>> getter = lazy(this::findGetter);
    private final Supplier<Optional<Method>> setter = lazy(this::findSetter);
    private final Supplier<Optional<Method>> invoker = lazy(this::findInvoker);

    private final String capitalizedPropertyName;

    PropertyAccessDescriptor(Class<? extends T> boundClass, String propertyName) {
        this.boundClass = requireNonNull(boundClass, "clazz must not be null");
        this.propertyName = requireNonNull(propertyName, "propertyName must not be null");
        capitalizedPropertyName = StringUtils.capitalize(propertyName);
    }

    private Optional<Method> findGetter() {
        Method foundMethod = MethodUtils.getMatchingAccessibleMethod(boundClass, GET_PREFIX + capitalizedPropertyName);
        if (foundMethod == null) {
            foundMethod = MethodUtils.getMatchingAccessibleMethod(boundClass, IS_PREFIX + capitalizedPropertyName);
        }
        if (foundMethod != null && Void.TYPE.equals(foundMethod.getReturnType())) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(foundMethod);
        }
    }

    private Optional<Method> findSetter() {
        return getter.get()
                .map(Method::getReturnType)
                .map(returnTyp -> new Class<?>[] { returnTyp })
                .map(params -> MethodUtils.getMatchingAccessibleMethod(boundClass, SET_PREFIX + capitalizedPropertyName,
                                                                       params));
    }

    private Optional<Method> findInvoker() {
        return Optional.ofNullable(MethodUtils.getAccessibleMethod(boundClass, propertyName));
    }

    public WriteMethod<T, V> createWriteMethod() {
        return new WriteMethod<>(getBoundClass(), getPropertyName(), getReflectionWriteMethod());
    }

    public ReadMethod<T, V> createReadMethod() {
        return new ReadMethod<>(getBoundClass(), getPropertyName(), getReflectionReadMethod());
    }

    public InvokeMethod<T> createInvokeMethod() {
        return new InvokeMethod<>(getBoundClass(), getPropertyName(), getReflectionInvokeMethod());
    }

    Supplier<Optional<Method>> getReflectionWriteMethod() {
        return setter;
    }

    Supplier<Optional<Method>> getReflectionReadMethod() {
        return getter;
    }

    Supplier<Optional<Method>> getReflectionInvokeMethod() {
        return invoker;
    }

    public Class<? extends T> getBoundClass() {
        return boundClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String toString() {
        return PropertyAccessDescriptor.class.getSimpleName() + '[' + boundClass + '#' + propertyName + ']';
    }
}
