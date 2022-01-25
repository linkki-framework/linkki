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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;

public abstract class AbstractUiSectionLayoutComponentsPmo {

    @UITextField(position = 10, label = "TextField with default width")
    public String getTextFieldWithDefaultWidth() {
        return "default width";
    }

    @UITextArea(position = 20, height = "5em", label = "TextArea with default width")
    public String getTextAreaWithDefaultWidth() {
        return "default width";
    }

    @BindTooltip(value = "IntegerField with default width")
    @UIIntegerField(position = 30, label = "IntegerField with default width")
    public Integer getIntegerFieldWithDefaultWidth() {
        return null;
    }

    @BindTooltip(value = "DoubleField with default width")
    @UIDoubleField(position = 40, label = "DoubleField with default width")
    public Double getDoubleFieldWithDefaultWidth() {
        return null;
    }

    @UITextField(position = 50, label = "TextField with custom width", width = "50%")
    public String getTextFieldWithCustomWidth() {
        return "custom width";
    }

    @UITextArea(position = 60, height = "5em", label = "TextArea with custom width", width = "50%")
    public String getTextAreaWithCustomWidth() {
        return "custom width";
    }

    @UIIntegerField(position = 70, label = "IntegerField with custom width", width = "50%")
    public Integer getIntegerFieldWithCustomWidth() {
        return null;
    }

    @UIDoubleField(position = 80, label = "DoubleField with custom width", width = "50%")
    public Double getDoubleFieldWithCustomWidth() {
        return null;
    }

}
