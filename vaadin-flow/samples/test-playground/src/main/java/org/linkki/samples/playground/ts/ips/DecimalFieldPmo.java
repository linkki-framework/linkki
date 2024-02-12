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

package org.linkki.samples.playground.ts.ips;

import org.faktorips.values.Decimal;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.ips.decimalfield.UIDecimalField;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection
public class DecimalFieldPmo {

    private Decimal decimal = Decimal.ZERO;

    @UIDecimalField(position = 0, label = "Default Format")
    public Decimal getDecimal() {
        return decimal;
    }

    public void setDecimal(Decimal decimal) {
        this.decimal = decimal;
    }

    @UIDecimalField(position = 10, format = "#,##0", label = "No Decimal Places Allowed")
    public Decimal getDecimalWithNoDecimalPlaces() {
        return decimal;
    }

    public void setDecimalWithNoDecimalPlaces(Decimal decimal) {
        this.decimal = decimal;
    }

    @UIDecimalField(position = 20, format = "#,#0.00##", label = "Thousands Separator")
    public Decimal getDecimalWithThousandsSeparator() {
        return decimal;
    }

    public void setDecimalWithThousandsSeparator(Decimal decimal) {
        this.decimal = decimal;
    }

    @UIDecimalField(position = 30,
            label = "Value Must be in Range [0..100/0.5]",
            required = RequiredType.REQUIRED,
            modelAttribute = IpsModelObject.PROPERTY_DECIMAL)
    public void decimal() {
        // model binding
    }
}
