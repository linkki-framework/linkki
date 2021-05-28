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

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class ProductsSampleTableRowPmo {

    private ProductsSampleModelObject modelObject;

    public ProductsSampleTableRowPmo(ProductsSampleModelObject modelObject) {
        this.modelObject = modelObject;
    }

    @ModelObject
    public ProductsSampleModelObject getModelObject() {
        return modelObject;
    }

    @UITableColumn
    @UILabel(position = 10, label = "Name", modelAttribute = "name")
    public void getName() {
        // bound to model
    }

    @UITableColumn
    @UILabel(position = 20, label = "Property 1", modelAttribute = "property1")
    public void getProperty1() {
        // bound to model
    }

    @UITableColumn
    @UILabel(position = 30, label = "Property 2", modelAttribute = "property2")
    public void getProperty2() {
        // bound to model
    }

}
