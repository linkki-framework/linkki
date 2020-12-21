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
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "@SectionHeader")
public class SectionHeaderPmo {

    private String lastPressed = "none";

    @SectionHeader
    @UIButton(position = 10, caption = "left")
    public void headerButtonLeft() {
        lastPressed = "left";
    }

    @UITextField(position = 20, label = "Sample content")
    public String getText() {
        return "This component has a position between the buttons, but no @SectionHeader annotation";
    }

    @SectionHeader
    @UIButton(position = 30, caption = "right")
    public void headerButtonRight() {
        lastPressed = "right";
    }

    @UILabel(position = 40, label = "Last button pressed:")
    public String getLabel() {
        return lastPressed;
    }

}
