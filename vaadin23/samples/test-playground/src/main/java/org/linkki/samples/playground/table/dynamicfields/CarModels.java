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
package org.linkki.samples.playground.table.dynamicfields;

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

    private CarModels() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<List<String>> getModels(String make) {
        return Optional.ofNullable(MAKE_MODEL_MAP.get(make));
    }
}
