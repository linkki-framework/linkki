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

package org.linkki.util;

import java.util.function.BooleanSupplier;

public class BooleanSuppliers {

    private BooleanSuppliers() {
        // prevent instantiation
    }

    /**
     * Returns a boolean supplier that represents the logical negation of the given boolean supplier.
     *
     * @param original the boolean supplier to be negated
     * @return a boolean supplier that represents the logical negation of the given boolean supplier
     */
    public static BooleanSupplier negate(BooleanSupplier original) {
        return () -> !original.getAsBoolean();
    }

}
