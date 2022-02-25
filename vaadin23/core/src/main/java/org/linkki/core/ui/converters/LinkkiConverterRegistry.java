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
package org.linkki.core.ui.converters;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.linkki.util.Sequence;

import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.DateToLongConverter;
import com.vaadin.flow.data.converter.DateToSqlDateConverter;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToBigIntegerConverter;
import com.vaadin.flow.data.converter.StringToBooleanConverter;
import com.vaadin.flow.data.converter.StringToDateConverter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.server.VaadinSession;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A converter registry that holds a set of standard converters. The registry could be instantiated with
 * additional converters which would be set before the default converters. That means if the registry is
 * initiated with a custom converter which has the same types as an existing one, the custom converter
 * is used with higher priority.
 */
public class LinkkiConverterRegistry implements Serializable {

    /**
     * Using the default implementation the converter message could not be translated in the locale of
     * the client because it is set within the constructor.
     */
    private static final String ERROR_MESSAGE = "Error converting Value";

    private static final long serialVersionUID = 1L;

    private static final List<Converter<?, ?>> DEFAULT_CONVERTERS = Arrays.asList(
                                                                                  new DateToLongConverter(),
                                                                                  new DateToSqlDateConverter(),
                                                                                  new LocalDateTimeToDateConverter(
                                                                                          ZoneId.systemDefault()),
                                                                                  new LocalDateToDateConverter(),
                                                                                  new LocalDateToStringConverter(),
                                                                                  new LocalDateTimeToStringConverter(),
                                                                                  new StringToBigDecimalConverter(
                                                                                          ERROR_MESSAGE),
                                                                                  new StringToBigIntegerConverter(
                                                                                          ERROR_MESSAGE),
                                                                                  new StringToBooleanConverter(
                                                                                          ERROR_MESSAGE),
                                                                                  new StringToDateConverter(),
                                                                                  new StringToDoubleConverter(
                                                                                          ERROR_MESSAGE),
                                                                                  new StringToFloatConverter(
                                                                                          ERROR_MESSAGE),
                                                                                  new StringToIntegerConverter(
                                                                                          ERROR_MESSAGE),
                                                                                  new StringToLongConverter(
                                                                                          ERROR_MESSAGE));

    // CSOFF: Declaration: must be located after DEFAULT_CONVERTERS because it uses them
    public static final LinkkiConverterRegistry DEFAULT = new LinkkiConverterRegistry();
    // CSOON: Declaration

    private final HashMap<Class<?>, Map<Class<?>, Converter<?, ?>>> converters = new HashMap<>();

    /**
     * Creates a new {@link LinkkiConverterRegistry} with all default converters.
     * 
     * @param customConverters custom converters that are registered within this
     *            {@link LinkkiConverterRegistry}
     */
    @SafeVarargs
    public LinkkiConverterRegistry(Converter<?, ?>... customConverters) {
        this(Arrays.asList(customConverters));
    }

    /**
     * Creates a new {@link LinkkiConverterRegistry} with additional converters which are registered
     * with higher priority before the default converters.
     * 
     * @param customConverters custom converters that are registered within this
     *            {@link LinkkiConverterRegistry}
     */
    public LinkkiConverterRegistry(Collection<Converter<?, ?>> customConverters) {
        requireNonNull(customConverters, "customConverters must not be null");
        DEFAULT_CONVERTERS.stream().forEach(this::storeConverter);
        customConverters.stream().forEach(this::storeConverter);
    }

    public LinkkiConverterRegistry(Sequence<Converter<?, ?>> customConverters) {
        this(customConverters.list());
    }

    private void storeConverter(Converter<?, ?> converter) {
        converters.computeIfAbsent(getPresentationType(converter),
                                   p -> new LinkedHashMap<>())
                .put(getModelType(converter), converter);
    }

    /**
     * Finds a converter that does match the two types for the presentation an the model type.
     * <p>
     * If the types are {@link Class classes} they have to match exactly the type of a converter. We do
     * not check assignable types because the covariance and contravariance depends on the direction of
     * conversion.
     * <p>
     * If both types are equal or we cannot determine the correct type for any reason, the
     * {@link Converter#identity() identity converter} is returned.
     * <p>
     * If the correct type could be determined but there is no matching converter, an
     * {@link IllegalArgumentException} is thrown.
     * 
     * @param presentationType The type of the presentation, that means the value type of the UI
     *            component
     * @param modelType The type of the model, that means the value type of the model property
     * @return the converter that best match the two types
     * 
     * @throws IllegalArgumentException if the types could be considered to be correct but no matching
     *             converter could be found.
     */
    @SuppressWarnings("unchecked")
    public <P, M> Converter<P, M> findConverter(Type presentationType, Type modelType) {
        Class<?> rawPresentationType = getRawType(presentationType);
        Class<?> rawModelType = getRawType(modelType);
        // rawPresentationType may be null if presentationType is a type variable
        if (isIdentityNecessary(rawPresentationType, rawModelType)) {
            return (Converter<P, M>)Converter.identity();
        } else {
            var byPresentationType = converters.get(rawPresentationType);

            if (byPresentationType != null) {
                return (Converter<P, M>)byPresentationType
                        .computeIfAbsent(rawModelType,
                                         mt -> findNextByPresentationType(byPresentationType.values(),
                                                                          rawPresentationType, mt));
            } else {
                throw new IllegalArgumentException(
                        "Cannot convert presentation type " + presentationType + " to model type "
                                + modelType);
            }
        }

    }

    private Converter<?, ?> findNextByPresentationType(
            Collection<Converter<?, ?>> convertersByPresentationType,
            Type presentationType,
            Type modelType) {
        return convertersByPresentationType.stream()
                .filter(c -> TypeUtils.isAssignable(modelType, getModelType(c)))
                .max((c0, c1) -> {
                    boolean c0AssignableToC1 = TypeUtils.isAssignable(getModelType(c0), getModelType(c1));
                    boolean c1AssignableToC0 = TypeUtils.isAssignable(getModelType(c1), getModelType(c0));
                    return Boolean.compare(c0AssignableToC1, c1AssignableToC0);
                })
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot convert presentation type " + presentationType + " to model type "
                                + modelType));
    }


    /**
     * This method determines whether we should use an identity converter. This might be because of:
     * <ul>
     * <li>Any of the types are null: this might be if the type was a {@link TypeVariable}</li>
     * <li>Both types are the same: this case is easy</li>
     * <li>One type is Object: this looks like we cannot get the correct type by any reason, better use
     * identity than nothing</li>
     * </ul>
     */
    private boolean isIdentityNecessary(@CheckForNull Class<?> rawPresentationType,
            @CheckForNull Class<?> rawModelType) {
        // CSOFF: Complexity
        return rawPresentationType == null
                || rawModelType == null
                || rawPresentationType.equals(rawModelType)
                || Object.class.equals(rawModelType) || Object.class.equals(rawPresentationType);
        // CSON: Complexity
    }

    @CheckForNull
    private Class<?> getRawType(@CheckForNull Type type) {
        return ClassUtils.primitiveToWrapper(TypeUtils.getRawType(type, null));
    }

    @CheckForNull
    private Class<?> getPresentationType(Converter<?, ?> converter) {
        return getTypeOf(converter, 0);
    }

    @CheckForNull
    private Class<?> getModelType(Converter<?, ?> converter) {
        return getTypeOf(converter, 1);
    }

    @CheckForNull
    private Class<?> getTypeOf(Converter<?, ?> converter, int index) {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(converter.getClass(),
                                                                              Converter.class);
        @SuppressWarnings("rawtypes")
        TypeVariable<Class<Converter>>[] typeVariables = Converter.class.getTypeParameters();
        return getRawType(typeArguments.get(typeVariables[index]));
    }

    protected Sequence<Converter<?, ?>> getAllConverters() {
        return converters.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Sequence.collect());
    }

    public LinkkiConverterRegistry with(Converter<?, ?> converter) {
        return new LinkkiConverterRegistry(getAllConverters().with(converter));
    }

    /**
     * Returns the {@link LinkkiConverterRegistry} that is configured in the current
     * {@link VaadinSession}. Another instance of {@link LinkkiConverterRegistry} could be configured
     * using
     * 
     * <pre>
     * VaadinSession.getCurrent().setAttribute(LinkkiConverterRegistry.class, converterRegistry);
     * </pre>
     * 
     * If there is either no current {@link VaadinSession} or no {@link LinkkiConverterRegistry} is
     * configured this method returns {@link LinkkiConverterRegistry#DEFAULT}.
     * 
     * @return the current configured {@link LinkkiConverterRegistry}
     */
    public static LinkkiConverterRegistry getCurrent() {
        return Optional.ofNullable(VaadinSession.getCurrent())
                .map(s -> s.getAttribute(LinkkiConverterRegistry.class))
                .orElse(LinkkiConverterRegistry.DEFAULT);
    }

}
