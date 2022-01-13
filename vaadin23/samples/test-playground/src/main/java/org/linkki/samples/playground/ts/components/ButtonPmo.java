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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.KeyCode;
import org.linkki.samples.playground.binding.annotation.UIRadioButtonGroup;

import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class ButtonPmo {

    private int counter;
    private String content;

    private TestShortcutBehavior testBehaviorWithTextArea = TestShortcutBehavior.BUTTON_WITH_ENTER;

    @UILabel(position = 0, label = "Counts how many times the button is triggered")
    public String getCounter() {
        return "Counter: " + counter;
    }


    @UIButton(position = 10, label = "Button with caption and icon", caption = "Increase Counter", icon = VaadinIcon.PLUS, showIcon = true)
    public void increaseCounter() {
        counter++;
    }

    @UIButton(position = 20, label = "Button with variant", caption = "Reset Counter", icon = VaadinIcon.ARROW_BACKWARD, showIcon = true, variants = ButtonVariant.LUMO_TERTIARY)
    public void resetCounter() {
        counter = 0;
    }

    @UIRadioButtonGroup(position = 30, label = "Test shortcut", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
    public TestShortcutBehavior getTestBehaviorWithTextArea() {
        return testBehaviorWithTextArea;
    }


    public void setTestBehaviorWithTextArea(TestShortcutBehavior testBehaviorWithTextArea) {
        this.testBehaviorWithTextArea = testBehaviorWithTextArea;
    }

    @UITextField(position = 35, label = "Non multi line input")
    public String getContentAsTextField() {
        return content;
    }

    public void setContentAsTextField(String content) {
        this.content = content;
    }

    @UITextArea(position = 40, label = "Multi line input", height = "5em")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @UILabel(position = 45, label = "Last updated TextArea content")
    public String getContentAsText() {
        return content;
    }

    @UIButton(position = 50, label = "Button with Enter", caption = "Increase counter", //
            shortcutKeyCode = KeyCode.ENTER, variants = ButtonVariant.LUMO_PRIMARY, //
            visible = VisibleType.DYNAMIC)
    public void buttonWithEnter() {
        increaseCounter();
    }

    public boolean isButtonWithEnterVisible() {
        return getTestBehaviorWithTextArea().equals(TestShortcutBehavior.BUTTON_WITH_ENTER);
    }

    @UIButton(position = 70, label = "Button with Crtl+Enter", caption = "Increase counter", //
            shortcutKeyCode = KeyCode.ENTER, shortcutKeyModifiers = KeyModifier.CONTROL, //
            visible = VisibleType.DYNAMIC)
    public void buttonWithModifier() {
        increaseCounter();
    }

    public boolean isButtonWithModifierVisible() {
        return getTestBehaviorWithTextArea().equals(TestShortcutBehavior.BUTTON_WITH_CRTL_ENTER);
    }

    @UIButton(position = 80, label = "Button with Enter if not empty", caption = "Increase counter", //
            shortcutKeyCode = KeyCode.ENTER, variants = ButtonVariant.LUMO_PRIMARY, //
            visible = VisibleType.DYNAMIC, enabled = EnabledType.DYNAMIC)
    public void buttonWithEnterIfNotEmpty() {
        increaseCounter();
    }

    public boolean isButtonWithEnterIfNotEmptyVisible() {
        return getTestBehaviorWithTextArea().equals(TestShortcutBehavior.BUTTON_DISABLED_IF_EMPTY);
    }

    public boolean isButtonWithEnterIfNotEmptyEnabled() {
        return StringUtils.isNotEmpty(getContent());
    }

    public static enum TestShortcutBehavior {
        BUTTON_WITH_ENTER,
        BUTTON_WITH_CRTL_ENTER,
        BUTTON_DISABLED_IF_EMPTY;
    }
}
