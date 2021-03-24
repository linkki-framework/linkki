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
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "Section to test IpsDispatcher on visibility")
public class VisibleSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UILabel(position = 10, htmlContent = true)
    public String getVisibleDescription() {
        return "Check that the IpsDispatcher uses the value set information:<br>"
                + "Fields should be visible if value set is not empty.";
    }

    @UIComboBox(position = 20, modelAttribute = IpsModelObject.PROPERTY_VALUESETEXCLNULL)
    public void valueSetExclNull() {
        // model binding
    }

    @UIComboBox(position = 30, modelAttribute = IpsModelObject.PROPERTY_VALUESETINCLNULL)
    public void valueSetInclNull() {
        // model binding
    }

    @UILabel(position = 40, htmlContent = true)
    public String getVisibleDynamicDescription() {
        return "The following field would be invisible and disabled,<br>"
                + "but is overridden with VisibleType.DYNAMIC=true and EnabledType.DYNAMIC=true";
    }

    // Should be invisible, but is forced to be visible and enabled
    @UIComboBox(position = 50, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
    public void emptyValueSet() {
        // model binding
    }

    public boolean isEmptyValueSetVisible() {
        return true;
    }

    public boolean isEmptyValueSetEnabled() {
        return true;
    }

    // Not visible at all
    @UIComboBox(position = 60, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET)
    public void invisibleEmptyValueSet() {
        // model binding
    }
}
