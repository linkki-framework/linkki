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
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class ButtonPmo {

    private int counter;

    @UILabel(position = 0)
    public String getCounter() {
        return "Counter: " + counter;
    }

    @UIButton(position = 10, caption = "Increase Counter", icon = VaadinIcon.PLUS, showIcon = true)
    public void increaseCounter() {
        counter++;
    }

    @UIButton(position = 11, caption = "Reset Counter", icon = VaadinIcon.MINUS, showIcon = true)
    public void resetCounter() {
        counter = 0;
    }

}
