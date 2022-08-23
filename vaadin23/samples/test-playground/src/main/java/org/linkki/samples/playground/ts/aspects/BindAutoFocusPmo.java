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

package org.linkki.samples.playground.ts.aspects;

import org.linkki.core.ui.aspects.annotation.BindAutoFocus;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormSection;

@UIFormSection
public class BindAutoFocusPmo {

    private String text = "";

    @UITextField(position = 10, label = "no autofocus")
    public String getNotAutoFocusedTextField() {
        return text;
    }

    public void setNotAutoFocusedTextField(String text) {
        this.text = text;
    }

    @BindAutoFocus
    @UITextField(position = 20, label = "autofocus")
    public String getAutoFocusedTextField() {
        return text;
    }

    public void setAutoFocusedTextField(String text) {
        this.text = text;
    }

}
