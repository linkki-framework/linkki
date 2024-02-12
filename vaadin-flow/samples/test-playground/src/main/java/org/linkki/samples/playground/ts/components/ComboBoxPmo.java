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

package org.linkki.samples.playground.ts.components;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.faktorips.values.Decimal;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.ips.decimalfield.FormattedDecimalFieldToStringConverter;

import com.vaadin.flow.data.binder.ValueContext;

@UIVerticalLayout
public class ComboBoxPmo {

    @UINestedComponent(position = 10)
    public NullValuePmo getNullValuePmo() {
        return new NullValuePmo();
    }

    @UINestedComponent(position = 20)
    public AlignmentPmo getAlignmentPmo() {
        return new AlignmentPmo();
    }

    @UINestedComponent(position = 30)
    public CaptionProviderPmo getCaptionProviderPmo() {
        return new CaptionProviderPmo();
    }

    @UINestedComponent(position = 40)
    public NonNullEmptyValuePmo getEmptyValuePmo() {
        return new NonNullEmptyValuePmo();
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    @UISection(caption = "Alignment")
    public static class AlignmentPmo {

        private Direction leftAligned;
        private Direction centerAligned;
        private Direction rightAligned;

        @UIComboBox(position = 20, label = "Left align", textAlign = TextAlignment.LEFT)
        public Direction getLeftAligned() {
            return leftAligned;
        }

        public void setLeftAligned(Direction direction) {
            this.leftAligned = direction;
        }

        @UIComboBox(position = 21, label = "Center align", textAlign = TextAlignment.CENTER)
        public Direction getCenterAligned() {
            return centerAligned;
        }

        public void setCenterAligned(Direction direction) {
            this.centerAligned = direction;
        }

        @UIComboBox(position = 22, label = "Right align", textAlign = TextAlignment.RIGHT)
        public Direction getRightAligned() {
            return rightAligned;
        }

        public void setRightAligned(Direction direction) {
            this.rightAligned = direction;
        }
    }

    @UISection(caption = "Caption provider")
    public static class CaptionProviderPmo {

        private Direction value;

        @UIComboBox(position = 10,
                label = "With capitalized lower case caption",
                itemCaptionProvider = CapitalizedLowerCaseCaptionProvider.class)
        public Direction getValue() {
            return value;
        }

        public void setValue(Direction value) {
            this.value = value;
        }
    }

    @UISection(caption = "Empty value that is not null")
    public static class NonNullEmptyValuePmo {

        private String requiredStringValue = "";
        private Decimal requiredDecimalValue = Decimal.NULL;
        private Decimal notRequiredDecimalValue = Decimal.NULL;

        @UIComboBox(position = 10,
                label = "Required with initial value \"\"",
                content = AvailableValuesType.DYNAMIC,
                required = RequiredType.REQUIRED)
        public String getRequiredStringValue() {
            return requiredStringValue;
        }

        public void setRequiredStringValue(String nonNullStringValue) {
            this.requiredStringValue = nonNullStringValue;
        }

        public List<String> getRequiredStringValueAvailableValues() {
            return Arrays.asList("1", "2", "3");
        }

        @UIComboBox(position = 20,
                label = "Required with initial value Decimal.NULL",
                content = AvailableValuesType.DYNAMIC,
                required = RequiredType.REQUIRED,
                itemCaptionProvider = DecimalCaptionProvider.class)
        public Decimal getRequiredDecimalValue() {
            return requiredDecimalValue;
        }

        public void setRequiredDecimalValue(Decimal nonNullDecimalValue) {
            this.requiredDecimalValue = nonNullDecimalValue;
        }

        public List<Decimal> getRequiredDecimalValueAvailableValues() {
            return List.of(Decimal.valueOf(1), Decimal.valueOf(2.2));
        }

        @UIComboBox(position = 30,
                label = "Not required Decimal",
                content = AvailableValuesType.DYNAMIC,
                itemCaptionProvider = DecimalCaptionProvider.class)
        public Decimal getNotRequiredDecimalValue() {
            return notRequiredDecimalValue;
        }

        public void setNotRequiredDecimalValue(Decimal notRequiredDecimalValue) {
            this.notRequiredDecimalValue = notRequiredDecimalValue;
        }

        public List<Decimal> getNotRequiredDecimalValueAvailableValues() {
            return List.of(Decimal.NULL, Decimal.valueOf(1), Decimal.valueOf(2.2));
        }

        public static class DecimalCaptionProvider implements ItemCaptionProvider<Decimal> {

            private static final FormattedDecimalFieldToStringConverter CONVERTER =
                    new FormattedDecimalFieldToStringConverter();

            @Override
            public String getCaption(Decimal value) {
                return CONVERTER.convertToPresentation(value, new ValueContext());
            }

        }

    }

    public static class CapitalizedLowerCaseCaptionProvider implements ItemCaptionProvider<Direction> {

        @Override
        public String getCaption(Direction value) {
            if (value != null) {
                return StringUtils.capitalize(value.name().toLowerCase());
            } else {
                return "None";
            }
        }

    }

    @UISection(caption = "Null value")
    public class NullValuePmo {

        private Direction directionWithoutNull = Direction.DOWN;
        private Direction directionWithNull;

        @UIComboBox(position = 10, label = "Enum Without null", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
        public Direction getDirectionWithoutNull() {
            return directionWithoutNull;
        }

        public void setDirectionWithoutNull(Direction directionWithoutNull) {
            this.directionWithoutNull = directionWithoutNull;
        }

        @UIComboBox(position = 20, label = "Enum With null", content = AvailableValuesType.ENUM_VALUES_INCL_NULL)
        public Direction getDirectionWithNull() {
            return directionWithNull;
        }

        public void setDirectionWithNull(Direction directionWithNull) {
            this.directionWithNull = directionWithNull;
        }

    }

}
