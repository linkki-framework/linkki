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

import java.util.function.Function;

import org.linkki.core.ui.aspects.annotation.BindComboBoxItemStyle;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindComboBoxItemStylePmo {

    private TextColor textColor;

    @UIComboBox(position = 0, label = "Item Greyed")
    @BindComboBoxItemStyle("text-secondary")
    public TextColor getGreyText() {
        return textColor;
    }

    public void setGreyText(TextColor textColor) {
        this.textColor = textColor;
    }


    @UIComboBox(position = 10, label = "Item Text Color")
    @BindComboBoxItemStyle
    public TextColor getTextColor() {
        return textColor;
    }

    public void setTextColor(TextColor textColor) {
        this.textColor = textColor;
    }

    public Function<TextColor, String> getTextColorItemStyle() {
        return tc -> tc.styleClass;
    }

    public enum TextColor {
        PRIMARY("text-primary"),
        ERROR("text-error"),
        SUCCESS("text-success"),
        SECONDARY("text-secondary");

        private final String styleClass;

        TextColor(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getName() {
            return name().toLowerCase();
        }

        public String getStyleName() {
            return styleClass;
        }

    }

}