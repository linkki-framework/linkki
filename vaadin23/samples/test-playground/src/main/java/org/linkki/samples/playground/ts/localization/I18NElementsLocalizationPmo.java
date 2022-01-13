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

package org.linkki.samples.playground.ts.localization;

import java.time.LocalDate;

import org.faktorips.values.Decimal;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.ips.decimalfield.UIDecimalField;

@UISection(caption = I18NElementsLocalizationPmo.I18N_LOCALIZED_LABEL)
public class I18NElementsLocalizationPmo {

    public static final String I18N_LOCALIZED_LABEL = "i18n localized label";

    @UILabel(position = 0, label = I18N_LOCALIZED_LABEL)
    public String getLocalizedLabel() {
        return "I am a non localized text";
    }

    @UIDoubleField(position = 10, label = I18N_LOCALIZED_LABEL)
    public Double getLocalizedDoubleField() {
        return Double.valueOf(12345.67);
    }

    @UIDecimalField(position = 20, label = I18N_LOCALIZED_LABEL)
    public Decimal getLocalizedDecimalField() {
        return Decimal.valueOf(12345.67);
    }

    @UIDateField(position = 30, label = I18N_LOCALIZED_LABEL)
    public LocalDate getLocalizedDateField() {
        return LocalDate.of(2020, 6, 13);
    }

    @UIButton(position = 40, caption = I18N_LOCALIZED_LABEL)
    public void localizedButtonCaption() {
        // no action required
    }

}
