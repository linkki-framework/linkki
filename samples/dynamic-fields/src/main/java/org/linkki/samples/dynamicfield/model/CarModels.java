/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.samples.dynamicfield.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CarModels {

    private static final Map<String, List<String>> MAKE_MODEL_MAP;

    static {
        MAKE_MODEL_MAP = new HashMap<>();
        MAKE_MODEL_MAP.put("Audi", Arrays.asList("A4", "A5", "A6", "A8", "Q7"));
        MAKE_MODEL_MAP.put("BMW", Arrays.asList("Z4", "M5", "i3"));
        MAKE_MODEL_MAP.put("VW", Arrays.asList("Passat", "Golf", "Polo"));
        MAKE_MODEL_MAP.put("Ferrari", Arrays.asList("812 superfast", "Enzo Ferrari"));
        MAKE_MODEL_MAP.put("Mercedes-Benz", Arrays.asList("A-Klasse", "B-Klasse", "GLA"));
        MAKE_MODEL_MAP.put("Porsche", Arrays.asList("911", "919", "718"));
    }

    public static Optional<List<String>> getModels(String make) {
        return Optional.ofNullable(MAKE_MODEL_MAP.get(make));
    }
}
