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

import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindTooltipPmo {

    private String tooltipText = "This fiels has a tooltip that changes dynamically with the content of this textfield";
    private String tooltipHtmlText =
            "<p>This is a nice <br /> text with some <br> new lines </p><div> <br> and some html</div>";

    @UITextField(position = 20, label = "Tooltip")
    @BindTooltip(tooltipType = TooltipType.DYNAMIC)
    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public String getTooltipTextTooltip() {
        return tooltipText;
    }

    @UITextField(position = 21, label = "Html Tooltip")
    @BindTooltip(tooltipType = TooltipType.DYNAMIC)
    public String getTooltipHtmlText() {
        return tooltipHtmlText;
    }

    public void setTooltipHtmlText(String tooltipText) {
        this.tooltipHtmlText = tooltipText;
    }

    public String getTooltipHtmlTextTooltip() {
        return tooltipHtmlText;
    }

    @UITextField(position = 22, label = "Static Tooltip")
    @BindTooltip(tooltipType = TooltipType.STATIC, value = "A nice static tooltip")
    public String getTooltipStaticText() {
        return "This field has a static tooltip that cannot be changed";
    }
}