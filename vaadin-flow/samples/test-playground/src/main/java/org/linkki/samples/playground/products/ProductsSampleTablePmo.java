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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.products.ProductsSamplePmo.RowSamplePmo;

import com.vaadin.flow.component.icon.VaadinIcon;

@BindCaption
@UISection
public class ProductsSampleTablePmo extends SimpleTablePmo<ProductsSampleModelObject, RowSamplePmo> {

    private final int pageLength;
    private String caption;

    public ProductsSampleTablePmo(int elements, int pageLength) {
        this("Sample Table Section", elements, pageLength);
    }

    public ProductsSampleTablePmo(String caption, int elements, int pageLength) {
        this(caption,
                IntStream.range(0, elements).mapToObj(i -> new ProductsSampleModelObject("Value of Name " + (i + 1),
                        "Value of Property 1 " + (i + 1))).collect(Collectors.toList()),
                pageLength);
    }

    private ProductsSampleTablePmo(String caption, List<? extends ProductsSampleModelObject> modelObjects,
            int pageLength) {
        super(modelObjects);
        this.pageLength = pageLength;
        this.caption = caption;
    }

    @Override
    protected RowSamplePmo createRow(ProductsSampleModelObject modelObject) {
        return new RowSamplePmo(modelObject);
    }

    @SectionHeader
    @UIButton(position = -10, icon = VaadinIcon.PLUS, showIcon = true, captionType = CaptionType.NONE)
    public void plus() {
        // does nothing
    }

    @Override
    public int getPageLength() {
        return pageLength;
    }

    public String getCaption() {
        return caption;
    }
}
