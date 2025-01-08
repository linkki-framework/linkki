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

package de.faktorzehn.commons.linkki.search.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.faktorzehn.commons.linkki.search.model.SearchParameterMapper;

/**
 * A collection of methods to use in {@link SearchParameterMapper}.
 * 
 * @apiNote Since {@code parse...} methods are null-safe, they can be called without checking
 *          whether the map contains the key:<br>
 *          <code>ParamsUtil.parseIsoDate(queryParams.get(PROPERTY_VALID_AT)).ifPresent(searchParams::setValidAt);</code>
 *          <p>
 *          {@code format...} methods return null if the parameter is null. Since null values are
 *          removed after {@link SearchParameterMapper#toQueryParameters(Object)}, a null-check does
 *          not have be performed on search parameter properties:<br>
 *          <code>queryParams.put(PROPERTY_VALID_AT, ParamsUtil.formatIsoDate(searchParams.getValidAt()));</code>
 */
public class ParamsUtil {

    private ParamsUtil() {
        // prevent instantiation
    }

    /**
     * Returns a copy of the given map that maps keys to a list consisting of only the value of the
     * original map.
     */
    public static Map<String, List<String>> expand(Map<String, String> params) {
        Map<String, List<String>> expandedMap = new LinkedHashMap<>();
        params.forEach((key, value) -> expandedMap.put(key, Arrays.asList(value)));
        return expandedMap;
    }

    /**
     * Returns a copy of the given map that maps keys to the first element in the list of the
     * original map. Additional entries in the list are ignored.
     * 
     * @throws NullPointerException if a key maps to {@code null}
     * @throws IndexOutOfBoundsException if a key maps to an empty map
     */
    public static Map<String, String> flatten(Map<String, List<String>> params) {
        Map<String, String> flattenedMap = new LinkedHashMap<>();
        params.forEach((key, values) -> flattenedMap.put(key, values.get(0)));
        return flattenedMap;
    }

    /**
     * Returns a copy of the given map for which all empty entries ({@code null} and {@code ""})
     * have been removed from the value lists. Keys that would map to an empty value ({@code null}
     * or an empty map) are removed.
     * <p>
     * This means the following guarantees apply to the returned map:
     * <ul>
     * <li>No key maps to {@code null}
     * <li>No key maps to an empty list
     * <li>No key maps to a value containing {@code null} or {@code ""}, i.e. no value contains
     * {@code null} or {@code ""}
     * </ul>
     */
    public static Map<String, List<String>> removeEmptyValues(Map<String, List<String>> params) {
        Map<String, List<String>> sanitizedMap = new LinkedHashMap<>();

        params.forEach((key, values) -> {
            if (values != null) {
                List<String> sanitizedValues = new ArrayList<>(values);
                sanitizedValues.removeAll(Arrays.asList(null, ""));
                if (!sanitizedValues.isEmpty()) {
                    sanitizedMap.put(key, sanitizedValues);
                }
            }
        });

        return sanitizedMap;
    }

    /**
     * Returns an optional containing the parsed date for the given value. If {@code value} is
     * {@code null} or can not be parsed by {@link DateTimeFormatter#ISO_DATE},
     * {@link Optional#empty()} is returned.
     */
    public static Optional<LocalDate> parseIsoDate(String value) {
        if (value != null) {
            try {
                return Optional.of(LocalDate.parse(value, DateTimeFormatter.ISO_DATE));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Formats the given date using {@link DateTimeFormatter#ISO_DATE}. If {@code value} is
     * {@code null} or can not be formatted, {@code null} is returned.
     */
    public static String formatIsoDate(LocalDate value) {
        if (value != null) {
            try {
                return value.format(DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns an optional containing the parsed boolean for the given value. If {@code value} is
     * {@code null}, {@link Optional#empty()} is returned
     */
    public static Optional<Boolean> parseBoolean(String value) {
        return Optional.ofNullable(value).map(Boolean::valueOf);
    }

    /**
     * Formats the given boolean. If {@code value} is {@code null}, {@code null} is returned.
     */
    public static String formatBoolean(Boolean value) {
        return value != null ? String.valueOf(value) : null;
    }

}
