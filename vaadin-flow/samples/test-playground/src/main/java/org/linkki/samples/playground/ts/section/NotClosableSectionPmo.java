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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

import com.vaadin.flow.component.icon.VaadinIcon;

@BindCaption
@UISection(closeable = false)
public class NotClosableSectionPmo {

    private String caption = "Not closable section";
    private ButtonPmo button1 = new ButtonPmo(true, "", VaadinIcon.PLUS);
    private ButtonPmo button2 = new ButtonPmo(true, "", VaadinIcon.EDIT);

    @SectionHeader
    @BindIcon
    @UIButton(position = 10, captionType = CaptionType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void headerButtonLeft() {
        // does nothing
    }

    public boolean isHeaderButtonLeftVisible() {
        return button1.isVisible();
    }

    public VaadinIcon getHeaderButtonLeftIcon() {
        return button1.getIcon();
    }

    public String getHeaderButtonLeftCaption() {
        return button1.getCaption();
    }

    @SectionHeader
    @BindIcon
    @UIButton(position = 30, captionType = CaptionType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void headerButtonRight() {
        // does nothing
    }

    public boolean isHeaderButtonRightVisible() {
        return button2.isVisible();
    }

    public VaadinIcon getHeaderButtonRightIcon() {
        return button2.getIcon();
    }

    public String getHeaderButtonRightCaption() {
        return button2.getCaption();
    }

    @UITextField(position = 40)
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @UINestedComponent(position = 50, label = "Header button 1")
    public ButtonPmo getButton1() {
        return button1;
    }

    @UINestedComponent(position = 60, label = "Header button 2")
    public ButtonPmo getButton2() {
        return button2;
    }

    @UIHorizontalLayout
    public static class ButtonPmo {
        private boolean visible;
        private String caption;
        private VaadinIcon icon;

        public ButtonPmo(boolean visible, String caption, VaadinIcon icon) {
            this.visible = visible;
            this.caption = caption;
            this.icon = icon;
        }

        @UICheckBox(position = 10, label = "visible")
        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @UITextField(position = 20, width = "", label = "caption")
        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        @UIComboBox(position = 30, label = "icon")
        public VaadinIcon getIcon() {
            return icon;
        }

        public void setIcon(VaadinIcon icon) {
            this.icon = icon;
        }
    }
}
