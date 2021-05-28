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

package org.linkki.samples.playground.bugs.lin1795;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "LIN-1795")
public class ComboBoxPmo implements PresentationModelObject {

    private String value;
    private String availableValues;

    @UILabel(position = 0)
    public String getDescription() {
        return "It should be possible to set the dynamic combo box to null, even if it has no allowed values";
    }

    @UIComboBox(position = 10, content = AvailableValuesType.DYNAMIC)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getValueAvailableValues() {
        if (StringUtils.isEmpty(availableValues)) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(availableValues.split(","));
        }
    }

    @UITextField(position = 20, label = "Available Values (comma separated)")
    public String getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(String availableValues) {
        this.availableValues = availableValues;
    }

}
