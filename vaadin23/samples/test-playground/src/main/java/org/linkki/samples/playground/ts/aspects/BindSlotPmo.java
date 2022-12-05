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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.aspects.annotation.BindSlot;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.samples.playground.customlayout.BindSlotLayout;
import org.linkki.samples.playground.customlayout.annotation.UIBindSlotLayout;

import com.vaadin.flow.component.icon.VaadinIcon;

// tag::bindSlot-pmo[]
@UIBindSlotLayout
public class BindSlotPmo {

    private final RightSlotPmo rightSlot;

    public BindSlotPmo(RightSlotPmo rightSlot) {
        this.rightSlot = rightSlot;
    }

    @BindSlot(BindSlotLayout.SLOT_LEFT)
    @UIButton(position = 10, showIcon = true, icon = VaadinIcon.ARROW_LEFT, caption = StringUtils.EMPTY)
    public void leftButton() {
        // click
    }

    @BindSlot(BindSlotLayout.SLOT_RIGHT)
    @UINestedComponent(position = 20, width = "inherit")
    public Object rightSlot() {
        return rightSlot;
    }

    // end::bindSlot-pmo[]
    
    @UIHorizontalLayout
    public static class RightSlotPmo {

        @UIButton(position = 10, showIcon = true, icon = VaadinIcon.HOME, caption = StringUtils.EMPTY)
        public void homeButton() {
            // click
        }

        @UIButton(position = 20, showIcon = true, icon = VaadinIcon.ARROW_RIGHT, caption = StringUtils.EMPTY)
        public void rightButton() {
            // click
        }
    }
// tag::bindSlot-pmo[]
}
// end::bindSlot-pmo[]
