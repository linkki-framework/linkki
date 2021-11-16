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

package org.linkki.samples.playground.bugs.lin2622;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "LIN-2622")
public class MassValuesComboBoxPmo {

    public static final String PROPERTY_MASS_VALUE = "massValue";

    // Need a list of more than 500 values
    private final List<String> items = concat(concat(
                                                     Arrays.stream(Locale.getAvailableLocales())
                                                             .map(Locale::getDisplayName),
                                                     Arrays.stream(Locale.getISOCountries())),
                                              Arrays.stream(Locale.getISOLanguages()))
                                                      .collect(toList());

    private String massValue;

    @UIComboBox(position = 10, label = "ComboBox with a lot of values", content = AvailableValuesType.DYNAMIC)
    public String getMassValue() {
        return massValue;
    }

    public void setMassValue(String value) {
        this.massValue = value;
    }

    public List<String> getMassValueAvailableValues() {
        return items;
    }

}
