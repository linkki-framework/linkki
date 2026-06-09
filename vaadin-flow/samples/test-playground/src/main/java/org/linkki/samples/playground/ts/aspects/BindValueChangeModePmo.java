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

import org.linkki.core.ui.aspects.annotation.BindValueChangeMode;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.data.value.ValueChangeMode;

@UISection
public class BindValueChangeModePmo {

    private String text = "";

    @UILabel(position = 10, label = "Text")
    public String getText() {
        return text;
    }

    // tag::bindValueChangeMode[]
    @BindValueChangeMode(ValueChangeMode.EAGER)
    @UITextField(position = 20, label = "EAGER")
    public String getValueChangeModeTextField() {
        return text;
    }
    // end::bindValueChangeMode[]

    public void setValueChangeModeTextField(String text) {
        this.text = text;
    }

    @UITextField(position = 30, label = "Default")
    public String getDefaultTextField() {
        return text;
    }

    public void setDefaultTextField(String text) {
        this.text = text;
    }
}
