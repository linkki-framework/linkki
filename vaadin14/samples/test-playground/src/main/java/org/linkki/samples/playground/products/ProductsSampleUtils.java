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

import org.apache.commons.compress.utils.Lists;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.AbstractSection;

public class ProductsSampleUtils {

    public static ProductsSampleTablePmo createSampleTable(int elements) {
        List<ProductsSampleModelObject> modelObjects = Lists.newArrayList();
        for (int i = 0; i < elements; i++) {
            modelObjects
                    .add(new ProductsSampleModelObject("Value of Name " + (i + 1), "Value of Property 1 " + (i + 1)));
        }
        return new ProductsSampleTablePmo(modelObjects);
    }

    public static AbstractSection createSampleTableSection(int elements) {
        return PmoBasedSectionFactory.createAndBindSection(createSampleTable(elements), new BindingContext());
    }

    public static ProductsSampleModelObject createSampleModelObject() {
        return new ProductsSampleModelObject("Value of Name", "Value of Property 1");
    }

    public static AbstractSection createSampleSectionHorizontal() {
        return PmoBasedSectionFactory
                .createAndBindSection(new ProductsSampleSectionHorizontal(createSampleModelObject()),
                                      new BindingContext());
    }

    public static AbstractSection createSampleSectionVertical() {
        return PmoBasedSectionFactory
                .createAndBindSection(new ProductsSampleSectionVertical(createSampleModelObject()),
                                      new BindingContext());
    }

}
