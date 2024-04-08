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
package org.linkki.samples.appsample.pmo.table;

import java.util.List;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.appsample.model.BusinessPartner;

// tag::searchTable[]
@UISection(caption = "Search Results")
public class SearchResultTablePmo extends SimpleTablePmo<BusinessPartner, SearchResultRowPmo> {

    public SearchResultTablePmo(Supplier<List<? extends BusinessPartner>> modelObjectsSupplier) {
        super(modelObjectsSupplier);
    }

    @Override
    protected SearchResultRowPmo createRow(BusinessPartner partner) {
        return new SearchResultRowPmo(partner);
    }
}
// end::searchTable[]