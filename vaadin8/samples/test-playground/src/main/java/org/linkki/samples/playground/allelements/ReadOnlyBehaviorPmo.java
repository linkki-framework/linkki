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

import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormSection;

import com.vaadin.ui.Notification;

@UIFormSection(caption = "@BindReadOnlyBehavior")
public class ReadOnlyBehaviorPmo {

    public static final String BUTTON_DISABLED_ON_READ_ONLY = "disabledOnReadOnly";
    public static final String BUTTON_INVISIBLE_ON_READ_ONLY = "invisibleOnReadOnly";
    public static final String TEXTFIELD_ACTIVE_ON_READ_ONLY = "activeOnReadOnly";

    private String activeOnReadOnly = "Textfield is writeable in readOnly mode";

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.DISABLED)
    @UIButton(position = 10, caption = "@BindReadOnlyBehavior(DISABLED)")
    public void disabledOnReadOnly() {
        Notification
                .show("You just clicked on the button that will be disabled when the application is in read-only mode");
    }

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVSIBLE)
    @UIButton(position = 20, caption = "@BindReadOnlyBehavior(INVISIBLE)")
    public void invisibleOnReadOnly() {
        Notification
                .show("You just clicked on the button that will be invisible when the application is in read-only mode");
    }

    @BindReadOnlyBehavior(ReadOnlyBehaviorType.WRITABLE)
    @UITextField(position = 25, label = "@BindReadOnlyBehavior(WRITABLE)")
    public String getActiveOnReadOnly() {
        return activeOnReadOnly;
    }

    public void setActiveOnReadOnly(String text) {
        this.activeOnReadOnly = text;
    }

    @UILabel(position = 30)
    public String getReadOnlyNote() {
        return "Set the application to read-only mode (via the Playground menu) to see the effect.";
    }
}
