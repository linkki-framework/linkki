/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.ts.aspects;

import static com.vaadin.flow.component.shared.Tooltip.TooltipPosition.BOTTOM;
import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.PREFIX;
import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.SUFFIX;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindToggletip;
import org.linkki.core.ui.aspects.annotation.UINestedToggleTip;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class BindToggletipPmo {

    private String toggletipText = "This field has a toggletip at suffix position that changes dynamically with the content of this textfield";
    private String nestedToggletip = "This field has a nested toggletip";

    @UITextField(position = 20, label = "Toggletip")
    @BindToggletip(toggletipPosition = SUFFIX)
    public String getToggletipText() {
        return toggletipText;
    }

    public void setToggletipText(String toggletipText) {
        this.toggletipText = toggletipText;
    }

    public String getToggletipTextToggletip() {
        return toggletipText;
    }

    @UIComboBox(position = 21, label = "Static Toggletip", content = AvailableValuesType.NO_VALUES)
    @BindToggletip(toggletipPosition = PREFIX, value = "A nice static toggletip", position = BOTTOM, icon = VaadinIcon.ABACUS)
    public String getToggletipStaticText() {
        return "This field has a static toggletip that cannot be changed with tooltip position bottom and a different icon";
    }

    @UITextField(position = 22)
    @UINestedToggleTip(position = 22, label = "Nested Toggletip Suffix")
    public String getNestedToggletipSuffix() {
        return nestedToggletip;
    }

    public void setNestedToggletipSuffix(String nestedToggletip) {
        this.nestedToggletip = nestedToggletip;
    }

    public Class<?> getNestedToggletipSuffixComponentType() {
        return UINestedToggleTip.class;
    }

    public String getNestedToggletipSuffixToggletip() {
        return "Toggletip: " + StringUtils.defaultIfBlank(nestedToggletip, "No content");
    }

    @UILabel(position = 23)
    @UINestedToggleTip(position = 23, label = "Nested Toggletip Prefix",
            toggletipPosition = PREFIX, icon = VaadinIcon.EXCLAMATION_CIRCLE_O, value = "Toggletip")
    public String getNestedToggletipPrefix() {
        return nestedToggletip;
    }

    public Class<?> getNestedToggletipPrefixComponentType() {
        return UINestedToggleTip.class;
    }
}