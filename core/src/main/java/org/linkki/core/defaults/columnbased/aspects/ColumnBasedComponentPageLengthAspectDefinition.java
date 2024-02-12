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

package org.linkki.core.defaults.columnbased.aspects;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentWrapper;

/**
 * Binds the number of simultaneously displayed items in a column based UI component to the number
 * obtained with a {@value #NAME} {@link Aspect}.
 */
public class ColumnBasedComponentPageLengthAspectDefinition<ROW, WRAPPER extends ColumnBasedComponentWrapper<ROW>>
        extends ColumnBasedComponentAspectDefinition<ROW, Integer, WRAPPER> {

    public static final String NAME = "pageLength";

    public ColumnBasedComponentPageLengthAspectDefinition() {
        super(NAME, WRAPPER::setPageLength);
    }

}
