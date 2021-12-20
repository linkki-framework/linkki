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

package org.linkki.doc;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.doc.ExampleTablePmo.ExampleRowPmo;

@UISection
public class ExampleTablePmo implements ContainerPmo<ExampleRowPmo> {

    private final SimpleItemSupplier<ExampleRowPmo, Partner> items;

    public ExampleTablePmo(List<Partner> partners) {
        items = new SimpleItemSupplier<>(() -> partners,
                ExampleRowPmo::new);
    }

    @Override
    public List<ExampleRowPmo> getItems() {
        return items.get();
    }

    public static class ExampleRowPmo extends PartnerSectionPmo {

        public ExampleRowPmo(Partner partner) {
            super(partner);
        }

    }
}
