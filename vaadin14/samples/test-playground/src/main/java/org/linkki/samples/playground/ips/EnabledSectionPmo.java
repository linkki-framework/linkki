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

package org.linkki.samples.playground.ips;

import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "IpsDispatcher on enabled")
public class EnabledSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UILabel(position = 10)
    public String getEnabledSectionPmoDescription() {
        return "Check that the IpsDispatcher uses the value set information to derive enabled state";
    }

    @UILabel(position = 15)
    public String getNotEmptyValueSetDescription() {
        return "The following field should be enabled as the value set is not empty.";
    }

    @UITextField(position = 20, modelAttribute = IpsModelObject.PROPERTY_UNRESTRICTEDINCLNULL, //
            label = "Not empty value set")
    public void notEmptyValueSet() {
        // model binding
    }

    @UILabel(position = 40)
    public String getEmptyValueSetDescription() {
        return "The following field should be disabled as the value set is empty.";
    }

    @UIComboBox(position = 50, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, //
            visible = VisibleType.DYNAMIC, label = "Empty value set")
    public void emptyValueSet() {
        // model binding
    }

    /*
     * Needs to be visible to be able to see the field.
     */
    public boolean isEmptyValueSetVisible() {
        return true;
    }

    @UILabel(position = 60)
    public String getDynamicEnabledEmptyValueSetDescription() {
        return "The following field should be enabled although the value set is empty as it is explicitly set as to enabled with enabled = DYNAMIC.";
    }

    @UIComboBox(position = 70, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, //
            visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC, //
            label = "Dynamic enabled empty value set")
    public void dynamicEnabledEmptyValueSet() {
        // model binding
    }

    /*
     * Needs to be visible to be able to see the field.
     */
    public boolean isDynamicEnabledEmptyValueSetVisible() {
        return true;
    }

    public boolean isDynamicEnabledEmptyValueSetEnabled() {
        return true;
    }
}
