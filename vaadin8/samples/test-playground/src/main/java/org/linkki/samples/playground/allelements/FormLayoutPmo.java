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

import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormLayout;

import com.vaadin.ui.Notification;

@BindCaption("UIFormLayout")
@UIFormLayout
public class FormLayoutPmo {

    @UITextField(label = "Text 1", position = 10)
    public String getText1() {
        return "read-only text";
    }

    @UITextField(label = "Text 2", position = 20)
    public String getText2() {
        return "read-only text2";
    }

    @UILabel(position = 30)
    public String getLabel() {
        return "label";
    }

    @UIButton(position = 40, caption = "Button")
    public void button() {
        Notification.show("Button clicked");
    }
}
