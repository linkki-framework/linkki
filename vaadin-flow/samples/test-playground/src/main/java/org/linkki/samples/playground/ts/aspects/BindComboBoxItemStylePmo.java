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

package org.linkki.samples.playground.ts.aspects;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.linkki.core.ui.aspects.annotation.BindComboBoxItemStyle;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

@UIHorizontalLayout
public class BindComboBoxItemStylePmo {

    @UINestedComponent(position = 1)
    public Object getComboBox() {
        return new BindComboBoxItemStyleWithComboBoxPmo();
    }

    @UINestedComponent(position = 2)
    public Object getMultiSelect() {
        return new BindComboBoxItemStyleWithMultiSelectPmo();
    }

    @UISection(caption = "ComboBox")
    public static class BindComboBoxItemStyleWithComboBoxPmo {

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
            return TextColor::getStyleName;
        }

        @UIComboBox(position = 20, label = "Style with alignment", textAlign = TextAlignment.RIGHT)
        @BindComboBoxItemStyle
        public TextColor getAlignedText() {
            return textColor;
        }

        public void setAlignedText(TextColor textColor) {
            this.textColor = textColor;
        }

        public Function<TextColor, String> getAlignedTextItemStyle() {
            return tc -> "text-primary text-right";
        }

    }

    @UISection(caption = "MultiSelect")
    public static class BindComboBoxItemStyleWithMultiSelectPmo {

        private Set<TextColor> textColors;

        @UIMultiSelect(position = 0, label = "Item Greyed")
        @BindComboBoxItemStyle("text-secondary")
        public Set<TextColor> getGreyText() {
            return textColors;
        }

        public void setGreyText(Set<TextColor> textColors) {
            this.textColors = textColors;
        }

        public Collection<TextColor> getGreyTextAvailableValues() {
            return List.of(TextColor.values());
        }

        @UIMultiSelect(position = 10, label = "Item Text Color")
        @BindComboBoxItemStyle
        public Set<TextColor> getTextColor() {
            return textColors;
        }

        public void setTextColor(Set<TextColor> textColors) {
            this.textColors = textColors;
        }

        public Function<TextColor, String> getTextColorItemStyle() {
            return TextColor::getStyleName;
        }

        public Collection<TextColor> getTextColorAvailableValues() {
            return List.of(TextColor.values());
        }
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
