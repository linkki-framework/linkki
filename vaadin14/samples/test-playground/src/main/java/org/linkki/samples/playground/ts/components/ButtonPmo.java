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

package org.linkki.samples.playground.ts.components;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.KeyCode;

import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class ButtonPmo {

    private int counter;
    private String content;

    @UILabel(position = 0)
    public String getCounter() {
        return "Counter: " + counter;
    }

    @UILabel(position = 5)
    public String getContentAsText() {
        return content;
    }

    @UIButton(position = 10, caption = "Increase Counter", icon = VaadinIcon.PLUS, showIcon = true)
    public void increaseCounter() {
        counter++;
    }

    @UIButton(position = 20, caption = "Reset Counter", icon = VaadinIcon.ARROW_BACKWARD, showIcon = true, variants = ButtonVariant.LUMO_TERTIARY, label = "Button with variant")
    public void resetCounter() {
        counter = 0;
    }

    @UITextArea(position = 40, label = "@UITextArea", height = "5em")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @UIButton(position = 50, label = "Button with Enter", caption = "Increase counter", shortcutKeyCode = KeyCode.ENTER, variants = ButtonVariant.LUMO_PRIMARY)
    public void button() {
        counter++;
    }

    @UIButton(position = 70, label = "Button with Crtl+Enter", caption = "Increase counter", shortcutKeyCode = KeyCode.ENTER, shortcutKeyModifiers = {
            KeyModifier.CONTROL })
    public void buttonWithModifier() {
        counter++;
    }
}
