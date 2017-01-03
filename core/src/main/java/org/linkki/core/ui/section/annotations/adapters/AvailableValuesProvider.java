/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides default methods to convert a value class to a list of available values for different
 * data types.
 */
public class AvailableValuesProvider {

    private AvailableValuesProvider() {
        // prevents calls
    }

    public static <T extends Enum<T>> List<T> enumToValues(Class<T> valueClass, boolean inclNull) {
        List<T> values = Arrays.asList(valueClass.getEnumConstants());
        if (inclNull) {
            ArrayList<T> result = new ArrayList<>();
            result.add(null);
            result.addAll(values);
            return Collections.unmodifiableList(result);
        } else {
            return values;
        }
    }

    public static List<Boolean> booleanWrapperToValues() {
        return Arrays.asList(null, Boolean.TRUE, Boolean.FALSE);
    }

    public static List<Object> booleanPrimitiveToValues() {
        return Arrays.asList(true, false);
    }
}