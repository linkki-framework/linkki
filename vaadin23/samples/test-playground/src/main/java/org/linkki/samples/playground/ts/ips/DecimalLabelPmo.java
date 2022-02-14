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

package org.linkki.samples.playground.ts.ips;

import org.faktorips.values.Decimal;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.ips.decimalfield.UIDecimalField;

@UISection
public class DecimalLabelPmo {

    private Decimal decimal = Decimal.NULL;

    @UILabel(position = 90, label = "Label with a Decimal as Value")
    public Decimal getDecimalLabel() {
        return decimal;
    }

    @UIDecimalField(position = 100, label = "Change Decimal Value")
    public Decimal getDecimalValue() {
        return decimal;
    }

    public void setDecimalValue(Decimal decimal) {
        this.decimal = decimal;
    }

}
