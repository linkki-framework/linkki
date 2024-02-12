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

package org.linkki.samples.playground.ts.aspects;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo;
import org.linkki.samples.playground.table.TableModelObject;

public class BindVariantNamesTables {

    private BindVariantNamesTables() {
        // should not be instantiated
    }

    // tag::bindVariantNames[]
    @UISection(caption = "Table with 'no row border' variant")
    @BindVariantNames(value = { "no-row-borders" })
    public static class BindVariantNamesTablePmoNoBorder extends SimplePlaygroundTablePmo {
        public BindVariantNamesTablePmoNoBorder() {
            super(IntStream.range(1, 2)
                    .mapToObj(TableModelObject::new)
                    .collect(Collectors.toList()));
        }
    }
    // end::bindVariantNames[]

    @UISection(caption = "Table without any variant")
    public static class BindVariantNamesTablePmoWithoutVariant extends SimplePlaygroundTablePmo {
        public BindVariantNamesTablePmoWithoutVariant() {
            super(IntStream.range(1, 2)
                    .mapToObj(TableModelObject::new)
                    .collect(Collectors.toList()));
        }
    }
}
