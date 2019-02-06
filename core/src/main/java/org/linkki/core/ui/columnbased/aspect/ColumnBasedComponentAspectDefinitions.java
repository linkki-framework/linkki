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

package org.linkki.core.ui.columnbased.aspect;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.ui.columnbased.ColumnBasedComponentWrapper;

/**
 * Utility class to create {@link LinkkiAspectDefinition LinkkiAspectDefinitions} for use with
 * {@link ColumnBasedComponentWrapper ColumnBasedComponentWrappers}.
 */
public class ColumnBasedComponentAspectDefinitions {

    private ColumnBasedComponentAspectDefinitions() {
        // do not instantiate
    }

    /**
     * Creates a list of {@link LinkkiAspectDefinition LinkkiAspectDefinitions} for use with
     * {@link ColumnBasedComponentWrapper ColumnBasedComponentWrappers} that update the items, footer,
     * and page length.
     */
    public static List<LinkkiAspectDefinition> createAll() {
        return Arrays.asList(new ColumnBasedComponentItemsAspectDefinition<>(),
                             new ColumnBasedComponentFooterAspectDefinition<>(),
                             new ColumnBasedComponentPageLengthAspectDefinition<>());
    }

}
