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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormSection;

@UIFormSection(columns = 2)
public class UiFormSectionMultiColumnComponentsPmo {

    private String text;

    @UITextField(position = 10, label = "Default width")
    public String getDefaultWidthText() {
        return getText();
    }

    public void setDefaultWidthText(String text) {
        setText(text);
    }

    @UITextField(position = 20, label = "Width 500px", width = "500px")
    public String getFixedWidthText() {
        return getText();
    }

    public void setFixedWidthText(String text) {
        setText(text);
    }

    @UITextField(position = 30)
    public String getText3() {
        return getText();
    }

    public void setText3(String text) {
        setText(text);
    }

    @UITextField(position = 40)
    public String getText4() {
        return getText();
    }

    public void setText4(String text) {
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
