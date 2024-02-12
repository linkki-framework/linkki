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

import static org.linkki.core.defaults.ui.aspects.types.AvailableValuesType.DYNAMIC;
import static org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode.INITIALLY_COLLAPSED;

import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;

import org.faktorips.values.Decimal;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.ips.decimalfield.UIDecimalField;

public abstract class ProductsSamplePmo {

    private final ProductsSampleModelObject sampleModelObject;
    private final String caption;

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

        @UITextField(position = 20,
                label = "Property 2",
                required = RequiredType.REQUIRED,
                modelAttribute = "property2",
                width = "")
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

        @UITextField(position = 20,
                label = "Property 2",
                required = RequiredType.REQUIRED,
                modelAttribute = "property2")
        public void property2() {
            /* model binding only */
        }

    }

    public static class RowSamplePmo extends ProductsSamplePmo {

        private static final SecureRandom SECURE_RANDOM = new SecureRandom();

        private final List<Decimal> decimalOptions = List.of(Decimal.valueOf(0), Decimal.valueOf(100),
                                                             Decimal.valueOf(200));
        private Decimal decimal;
        private Decimal decimalSelection;

        public RowSamplePmo(ProductsSampleModelObject sampleModelObject) {
            super("", sampleModelObject);
            decimal = Decimal.valueOf(SECURE_RANDOM.nextDouble() * 1000).round(2, RoundingMode.HALF_UP);
            decimalSelection = decimalOptions.get(0);
        }

        @UITableColumn(flexGrow = 1)
        @UITextField(position = 10, label = "Property 1", modelAttribute = "property")
        public void property1() {
            /* model binding only */
        }

        @UITextField(position = 20,
                label = "Property 2",
                required = RequiredType.REQUIRED,
                modelAttribute = "property2")
        public void property2() {
            /* model binding only */
        }

        @UITableColumn(textAlign = TextAlignment.RIGHT)
        @UIDecimalField(position = 30, label = "Decimal")
        public Decimal getDecimal() {
            return decimal;
        }

        public void setDecimal(Decimal decimal) {
            this.decimal = decimal;
        }

        @UITableColumn(collapsible = INITIALLY_COLLAPSED)
        @UILabel(position = 40, label = "19%")
        public Decimal getDecimalPercentage() {
            return decimal.multiply(Decimal.valueOf(0.19));
        }

        @UITableColumn(collapsible = INITIALLY_COLLAPSED)
        @UILabel(position = 50, label = "7%")
        public Decimal getDecimalOtherPercentage() {
            return decimal.multiply(Decimal.valueOf(0.07));
        }

        @UIComboBox(position = 60, label = "Selection", content = DYNAMIC)
        public Decimal getDecimalSelection() {
            return decimalSelection;
        }

        public void setDecimalSelection(Decimal decimalSelection) {
            this.decimalSelection = decimalSelection;
        }

        public List<Decimal> getDecimalSelectionAvailableValues() {
            return decimalOptions;
        }

        @UITableColumn(textAlign = TextAlignment.RIGHT)
        @UILabel(position = 70, label = "Decimal label")
        public Decimal getDecimalLabel() {
            return decimal.subtract(decimalSelection).round(2, RoundingMode.HALF_UP);
        }
    }
}