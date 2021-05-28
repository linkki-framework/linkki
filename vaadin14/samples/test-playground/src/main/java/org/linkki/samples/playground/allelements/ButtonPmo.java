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

package org.linkki.samples.playground.allelements;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.vaadin.component.KeyCode;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

@UIHorizontalLayout
public class ButtonPmo {

    @UIButton(position = 0, caption = "Normal Button", shortcutKeyCode = KeyCode.ENTER)
    public void normal() {
        Notification.show("Normal Button clicked", 1000, Position.MIDDLE);
    }

    @UIButton(position = 1, caption = "Primary Button", variants = ButtonVariant.LUMO_PRIMARY)
    public void primary() {
        Notification.show("Primary Button clicked", 1000, Position.MIDDLE);
    }

    @UIButton(position = 2, caption = "Inline Button", variants = ButtonVariant.LUMO_TERTIARY_INLINE)
    public void inline() {
        Notification.show("Inline Button clicked", 1000, Position.MIDDLE);
    }

    @UIButton(position = 3, caption = "Small Success Button", variants = {
            ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL })
    public void smallSuccess() {
        Notification.show("Small Success Button clicked", 1000, Position.MIDDLE);
    }

}
