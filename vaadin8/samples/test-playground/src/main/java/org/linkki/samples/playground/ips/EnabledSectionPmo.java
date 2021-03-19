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
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection(caption = "Section to test IpsDispatcher on enabled")
public class EnabledSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UILabel(position = 10, htmlContent = true)
    public String getEnabledDescription() {
        return "Check that the IpsDispatcher uses the value set information:<br>"
                + "Fields should be enabled if value set is not empty.";
    }

    /*
     * Should be enabled
     */
    @UIComboBox(position = 20, modelAttribute = IpsModelObject.PROPERTY_VALUESETEXCLNULL)
    public void corge() {
        // model binding
    }

    /*
     * Should be enabled
     */
    @UIComboBox(position = 30, modelAttribute = IpsModelObject.PROPERTY_VALUESETINCLNULL)
    public void grault() {
        // model binding
    }

    @UILabel(position = 40, htmlContent = true)
    public String getGarplyDescription() {
        return "The following field should be invisible and disabled,<br>"
                + "but it's forced to be shown by VisibleType.DYNAMIC=true";
    }

    /*
     * Should be visible due to VisibleType.DYNAMIC but disabled
     */
    @UIComboBox(position = 50, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, visible = VisibleType.DYNAMIC)
    public void garply() {
        // model binding
    }

    public boolean isGarplyVisible() {
        return true;
    }

    /*
     * Should neither be visible nor enabled
     */
    @UIComboBox(position = 60, modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET)
    public void waldo() {
        // model binding
    }

}
