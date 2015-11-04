/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {

    /** Utility class that should not be instantiated. */
    private StreamUtil() {
        super();
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
