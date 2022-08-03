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

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.binding.annotation.UIRadioButtonGroup;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection
public class AvailableValuesSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UIComboBox(position = 10, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_INTEGERENUMERATIONVALUESET)
    public void integerEnumerationValueSet() {
        // model binding
    }

    @UIRadioButtons(position = 11, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_INTEGERENUMERATIONVALUESET)
    public void integerEnumerationValueSetRadioButtons() {
        // model binding
    }

    @UIComboBox(position = 20, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_ENUMERATIONVALUESET)
    public void enumerationValueSet() {
        // model binding
    }

    @UIRadioButtonGroup(position = 21, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_ENUMERATIONVALUESET)
    public void enumerationValueSetRadioButtons() {
        // model binding
    }

    @UIComboBox(position = 30, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_INTEGERRANGEVALUESET)
    public void integerRangeValueSet() {
        // model binding
    }

    @UIComboBox(position = 40, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_BOOLEANVALUESET)
    public void booleanValueSet() {
        // model binding
    }

    @UIRadioButtonGroup(position = 50, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_BOOLEANVALUESET)
    public void booleanValueSetRadioButtons() {
        // model binding
    }

    @UIComboBox(position = 70, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_EMPTYSTRINGVALUESET, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC, label = "Empty String ValueSet should show an empty ComboBox")
    public void emptyStringValueSet() {
        // model binding
    }

    public boolean isEmptyStringValueSetVisible() {
        return true;
    }

    public boolean isEmptyStringValueSetEnabled() {
        return true;
    }

}
