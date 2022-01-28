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

package org.linkki.samples.playground.products;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.products.ProductsSamplePmo.TableSamplePmo;

@UISection(caption = "Sample Table Section")
public class ProductsSampleTablePmo extends SimpleTablePmo<ProductsSampleModelObject, TableSamplePmo> {

    private final int pageLength;

    protected ProductsSampleTablePmo(List<? extends ProductsSampleModelObject> modelObjects, int pageLength) {
        super(modelObjects);
        this.pageLength = pageLength;
    }

    @Override
    protected TableSamplePmo createRow(ProductsSampleModelObject modelObject) {
        return new TableSamplePmo(modelObject);
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }
}
