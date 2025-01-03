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

import org.linkki.core.ui.aspects.annotation.BindHelperText;
import org.linkki.core.ui.aspects.types.HelperTextType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindHelperTextPmo {

    private String helperTextText = "This field has a helper text that changes dynamically with the content of this textfield";

    @UITextField(position = 20, label = "Helper Text")
    @BindHelperText(helperTextType = HelperTextType.DYNAMIC)
    public String getHelperText() {
        return helperTextText;
    }

    public void setHelperText(String tooltipText) {
        this.helperTextText = tooltipText;
    }

    public String getHelperTextHelperText() {
        return helperTextText;
    }

    @UITextField(position = 21, label = "Static Helper Text")
    @BindHelperText(helperTextType = HelperTextType.STATIC, value = "<small>A <b>nice</b> static helper text</small>", htmlContent =
            true, placeAboveElement = true, showIcon = true)
    public String getHelperTextStaticText() {
        return "This field has a static helper text that cannot be changed with html content and is shown above the field";
    }
}