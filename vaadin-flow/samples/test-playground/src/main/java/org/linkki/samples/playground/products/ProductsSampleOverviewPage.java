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

package org.linkki.samples.playground.products;

import java.io.Serial;

import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.vaadin.component.page.AbstractPage;

public class ProductsSampleOverviewPage extends AbstractPage {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ProductsSampleModelObject modelObject;
    private final ProductSampleBindingManager bindingManager;

    public ProductsSampleOverviewPage() {
        this.modelObject = new ProductsSampleModelObject();
        this.bindingManager = new ProductSampleBindingManager(modelObject::validate);
    }

    @Override
    public void createContent() {
        addSection(new ProductsSamplePmo.VerticalSamplePmo(modelObject));
        addSection(new ProductsSamplePmo.HorizontalSamplePmo(modelObject));

        // Two column layout
        addSections(new ProductsSamplePmo.VerticalSamplePmo(modelObject),
                    new ProductsSamplePmo.VerticalSamplePmo(modelObject));

        // Scroll and grow table section
        var tableSection = addSection(new ProductsSampleTablePmo(100, 0));
        ComponentStyles.setOverflowAuto(tableSection);
        addAndExpand(tableSection);
    }

    @Override
    protected ProductSampleBindingManager getBindingManager() {
        return bindingManager;
    }
}
