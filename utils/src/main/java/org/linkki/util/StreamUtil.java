/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

public class StreamUtil {

    /** Utility class that should not be instantiated. */
    private StreamUtil() {
        super();
    }

    public static <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
        if (iterable == null) {
            return Stream.empty();
        }
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
