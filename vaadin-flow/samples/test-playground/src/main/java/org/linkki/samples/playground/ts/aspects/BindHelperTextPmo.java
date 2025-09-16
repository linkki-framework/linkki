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
    public static final String STATIC_HELPER_TEXT = "Static Helper Text";

    private String helperText = "I can be changed";
    private String staticText = "";
    private String dynamicText = "";

    @BindHelperText(value = STATIC_HELPER_TEXT, helperTextType = HelperTextType.STATIC)
    @UITextField(position = 0, label = "Text field with static helper text")
    public String getTextFieldWithStaticHelperText() {
        return staticText;
    }

    public void setTextFieldWithStaticHelperText(String staticText) {
        this.staticText = staticText;
    }

    @BindHelperText(value = "This helper text value will be ignored", helperTextType = HelperTextType.DYNAMIC)
    @UITextField(position = 10, label = "Text field with dynamic helper text")
    public String getTextFieldWithDynamicHelperText() {
        return dynamicText;
    }

    public void setTextFieldWithDynamicHelperText(String dynamicText) {
        this.dynamicText = dynamicText;
    }

    public String getTextFieldWithDynamicHelperTextHelperText() {
        return helperText;
    }

    @UITextField(position = 20, label = "Helper text value")
    public String getHelperText() {
        return helperText;
    }

    public void setHelperText(String helperText) {
        this.helperText = helperText;
    }

}
