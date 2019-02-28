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
package org.linkki.core.ui.section.annotations.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Provides default methods to convert a value class to a list of available values for different data
 * types.
 */
public class AvailableValuesProvider {

    private AvailableValuesProvider() {
        // prevents calls
    }

    public static <T extends Enum<T>> List<@Nullable T> enumToValues(Class<T> valueClass, boolean inclNull) {
        @SuppressWarnings("null")
        List<@Nullable T> values = Arrays.asList(valueClass.getEnumConstants());
        if (inclNull) {
            ArrayList<@Nullable T> result = new ArrayList<>();
            result.add(null);
            result.addAll(values);
            return Collections.unmodifiableList(result);
        } else {
            return values;
        }
    }

    public static List<@Nullable Boolean> booleanWrapperToValues() {
        return Arrays.asList(null, Boolean.TRUE, Boolean.FALSE);
    }

    public static List<Object> booleanPrimitiveToValues() {
        return Arrays.asList(true, false);
    }
}