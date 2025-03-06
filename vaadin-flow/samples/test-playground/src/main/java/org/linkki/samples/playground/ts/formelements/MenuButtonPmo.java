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

package org.linkki.samples.playground.ts.formelements;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIMenuButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;

@UISection
public class MenuButtonPmo {

    private String dynamicCaption = "Dynamic caption";
    private boolean dynamicEnabled = true;

    @UIMenuButton(captionType = CaptionType.DYNAMIC, position = 10,
            label = "Menu Button with dynamic caption and no icon",
            caption = "",
            icon = VaadinIcon.AIRPLANE,
            showIcon = false)
    public void dynamicButton() {
        Notification.show("Dynamic caption button clicked");
    }

    public String getDynamicButtonCaption() {
        return getDynamicCaption();
    }

    @UITextField(position = 20, label = "Text Field to change the caption")
    public String getDynamicCaption() {
        return dynamicCaption;
    }

    public void setDynamicCaption(String dynamicCaption) {
        this.dynamicCaption = dynamicCaption;
    }

    @BindIcon
    @UIMenuButton(position = 30, enabled = EnabledType.DYNAMIC,
            label = "Menu Button with dynamic icon and enabled status",
            caption = "Dynamic icon",
            icon = VaadinIcon.AIRPLANE, showIcon = false)
    public void dynamicBindIconButton() {
        Notification.show("Dynamic icon button clicked");
    }

    public VaadinIcon getDynamicBindIconButtonIcon() {
        return dynamicEnabled ? VaadinIcon.CHECK : VaadinIcon.LOCK;
    }

    public boolean isDynamicBindIconButtonEnabled() {
        return isDynamicEnabled();
    }

    @UICheckBox(position = 40, label = "Button enabled?", caption = "")
    public boolean isDynamicEnabled() {
        return dynamicEnabled;
    }

    public void setDynamicEnabled(boolean dynamicEnabled) {
        this.dynamicEnabled = dynamicEnabled;
    }

    @UIMenuButton(position = 50, label = "Menu Button with Variant Primary",
            caption = "Primary Button",
            variants = MenuBarVariant.LUMO_PRIMARY,
            icon = VaadinIcon.HOME)
    public void buttonVariantPrimary() {
        Notification.show("Primary button clicked");
    }

    @UIMenuButton(position = 60, label = "Menu Button with Variant Tertiary Inline",
            caption = "Tertiary Button",
            variants = MenuBarVariant.LUMO_TERTIARY_INLINE,
            icon = VaadinIcon.HOME)
    public void buttonVariantTertiary() {
        Notification.show("Tertiary button clicked");
    }

}
