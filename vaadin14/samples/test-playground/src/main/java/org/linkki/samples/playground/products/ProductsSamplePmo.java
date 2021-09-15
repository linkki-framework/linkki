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

import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;

public class ProductsSamplePmo {

    private ProductsSampleModelObject sampleModelObject;

    public ProductsSamplePmo() {
        this(new ProductsSampleModelObject());
    }

    public ProductsSamplePmo(ProductsSampleModelObject sampleModelObject) {
        this.sampleModelObject = sampleModelObject;
    }

    @ModelObject
    public ProductsSampleModelObject getSampleModelObject() {
        return sampleModelObject;
    }

    @UITextField(position = 10, label = "Property 1", modelAttribute = "property")
    public void property1() {
        /* model binding only */
    }

    @UITextField(position = 20, label = "Property 2", required = RequiredType.REQUIRED, modelAttribute = "property2")
    public void property2() {
        /* model binding only */
    }

    @UISection(caption = "Sample Section Horizontal", layout = SectionLayout.HORIZONTAL, columns = 2)
    public static class Horizontal extends ProductsSamplePmo {
        // nothing needed
    }

    @UISection(caption = "Sample Section Vertical", layout = SectionLayout.COLUMN)
    public static class Vertical extends ProductsSamplePmo {
        // nothing needed
    }
}