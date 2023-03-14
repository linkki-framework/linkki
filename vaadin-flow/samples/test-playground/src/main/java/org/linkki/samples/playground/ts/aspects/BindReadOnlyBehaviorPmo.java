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

import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

@BindCaption
@UISection
public class BindReadOnlyBehaviorPmo {

    private String text = "";
    private String title = "";

    public BindReadOnlyBehaviorPmo(String title) {
        this.title = title;
    }

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.DISABLED)
    @UIButton(position = 10, label = "DISABLED", caption = "Should be disabled in read-only mode")
    public void disabledButton() {
        Notification.show("Button clicked", 1000, Position.MIDDLE);
    }

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
    @UIButton(position = 20, label = "INVISIBLE", caption = "Should be invisible in read-only mode")
    public void invisibleButton() {
        Notification.show("Button clicked", 1000, Position.MIDDLE);
    }

    @BindPlaceholder("Should be writable in read-only mode")
    @BindReadOnlyBehavior(ReadOnlyBehaviorType.WRITABLE)
    @UITextField(position = 30, label = "WRITABLE")
    public String getWritableTextField() {
        return text;
    }

    public void setWritableTextField(String text) {
        this.text = text;
    }

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE)
    @UIButton(position = 40, label = "INVISIBLE_IF_WRITABLE and INVISIBLE", visible = VisibleType.INVISIBLE)
    public void invisibleIfWritableButtonDefaultInvisible() {
        Notification.show("Button clicked", 1000, Position.MIDDLE);
    }

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE)
    @UIButton(position = 50, caption = "Should be visible in read-only mode", label = "INVISIBLE_IF_WRITABLE and VISIBLE", visible = VisibleType.VISIBLE)
    public void invisibleIfWritableButtonDefaultVisible() {
        Notification.show("Button clicked", 1000, Position.MIDDLE);
    }

    public String getCaption() {
        return title;
    }

}
