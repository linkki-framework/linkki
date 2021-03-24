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

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "Section to test IpsDispatcher on required")
public class RequiredSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UILabel(position = 10, htmlContent = true)
    public String getRequiredDescription() {
        return "Check that the IpsDispatcher uses the value set information:<br>"
                + "Required marker should be visible if value set does not contains null.";
    }

    @UITextField(position = 20, modelAttribute = IpsModelObject.PROPERTY_UNRESTRICTEDEXCLNULL)
    public void unrestrictedValueSetExclNull() {
        // model binding
    }

    @UITextField(position = 30, modelAttribute = IpsModelObject.PROPERTY_UNRESTRICTEDINCLNULL)
    public void unrestrictedValueSetInclNull() {
        // model binding
    }

}
