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
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;

public abstract class ProductsSamplePmo {

    private ProductsSampleModelObject sampleModelObject;
    private String caption;

    public ProductsSamplePmo(String caption) {
        this(caption, new ProductsSampleModelObject());
    }

    public ProductsSamplePmo(String caption, ProductsSampleModelObject sampleModelObject) {
        this.caption = caption;
        this.sampleModelObject = sampleModelObject;
    }

    @ModelObject
    public ProductsSampleModelObject getSampleModelObject() {
        return sampleModelObject;
    }

    public String getCaption() {
        return caption;
    }

    @BindCaption
    @UISection(layout = SectionLayout.HORIZONTAL, columns = 2)
    public static class HorizontalSamplePmo extends ProductsSamplePmo {

        public HorizontalSamplePmo() {
            super("Sample Section Horizontal");
        }

        public HorizontalSamplePmo(String caption) {
            super(caption);
        }

        @UITextField(position = 10, label = "Property 1", modelAttribute = "property", width = "")
        public void property1() {
            /* model binding only */
        }

        @UITextField(position = 20, label = "Property 2", required = RequiredType.REQUIRED, modelAttribute = "property2", width = "")
        public void property2() {
            /* model binding only */
        }
    }

    @BindCaption
    @UISection(layout = SectionLayout.FORM)
    public static class VerticalSamplePmo extends ProductsSamplePmo {

        public VerticalSamplePmo() {
            super("Sample Section Horizontal");
        }

        public VerticalSamplePmo(String caption) {
            super(caption);
        }

        @UITextField(position = 10, label = "Property 1", modelAttribute = "property")
        public void property1() {
            /* model binding only */
        }

        @UITextField(position = 20, label = "Property 2", required = RequiredType.REQUIRED, modelAttribute = "property2")
        public void property2() {
            /* model binding only */
        }

    }

    public static class RowSamplePmo extends ProductsSamplePmo {

        public RowSamplePmo(ProductsSampleModelObject sampleModelObject) {
            super("", sampleModelObject);
        }

        @UITextField(position = 10, label = "Property 1", modelAttribute = "property")
        public void property1() {
            /* model binding only */
        }

        @UITextField(position = 20, label = "Property 2", required = RequiredType.REQUIRED, modelAttribute = "property2")
        public void property2() {
            /* model binding only */
        }

    }
}