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

import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "IpsDispatcher on visibility")
public class VisibleSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UILabel(position = 10)
    public String getSectionDescription() {
        return "Check that the IpsDispatcher uses the value set information to derive visible state";
    }

    @UILabel(position = 15)
    public String getNotEmptyValueSetDescription() {
        return "The following field should be visible as value set is not empty.";
    }

    @UITextField(position = 20, modelAttribute = IpsModelObject.PROPERTY_UNRESTRICTEDEXCLNULL, label = "Not empty value set")
    public void notEmptyValueSet() {
        // model binding
    }

    @UILabel(position = 25)
    public String getEmptyValueSetDescription() {
        return "The following field should not be visible as the value set is empty.";
    }

    @UIComboBox(position = 30, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, label = "Empty value set")
    public void emptyValueSet() {
        // model binding
    }

    @UILabel(position = 35)
    public String getDynamicVisibleEmptyValueSetDescription() {
        return "The following field should be visible although the value set is empty as it is explicitly set to visible with visible = DYNAMIC";
    }

    @UIComboBox(position = 40, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, //
            visible = VisibleType.DYNAMIC, label = "Dynamic visible empty value set")
    public void dynamicVisibleEmptyValueSet() {
        // model binding
    }

    public boolean isDynamicVisibleEmptyValueSetVisible() {
        return true;
    }

}
