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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

import java.util.Arrays;
import java.util.List;

@UISection
public class ComboBoxPmo {

    private Direction directionWithoutNull = Direction.DOWN;
    private Direction directionWithNull;
    private Direction leftAligned;
    private Direction centerAligned;
    private Direction rightAligned;

    @UIComboBox(position = 0, label = "Enum without null", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, itemCaptionProvider = CaptionProvider.class)
    public Direction getDirectionWithoutNull() {
        return directionWithoutNull;
    }

    public void setDirectionWithoutNull(Direction directionWithoutNull) {
        this.directionWithoutNull = directionWithoutNull;
    }

    @UIComboBox(position = 10, label = "Enum With null", content = AvailableValuesType.ENUM_VALUES_INCL_NULL, itemCaptionProvider = CaptionProvider.class)
    public Direction getDirectionWithNull() {
        return directionWithNull;
    }

    public void setDirectionWithNull(Direction directionWithNull) {
        this.directionWithNull = directionWithNull;
    }

    @UIComboBox(position = 20, label = "Left align", itemCaptionProvider = CaptionProvider.class, textAlign = TextAlignment.LEFT)
    public Direction getLeftAligned() {
        return leftAligned;
    }

    public void setLeftAligned(Direction direction) {
        this.leftAligned = direction;
    }

    @UIComboBox(position = 21, label = "Center align", itemCaptionProvider = CaptionProvider.class, textAlign = TextAlignment.CENTER)
    public Direction getCenterAligned() {
        return centerAligned;
    }

    public void setCenterAligned(Direction direction) {
        this.centerAligned = direction;
    }

    @UIComboBox(position = 22, label = "Right align", itemCaptionProvider = CaptionProvider.class, textAlign = TextAlignment.RIGHT)
    public Direction getRightAligned() {
        return rightAligned;
    }

    public void setRightAligned(Direction direction) {
        this.rightAligned = direction;
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public static class CaptionProvider implements ItemCaptionProvider<Direction> {

        @Override
        public String getCaption(Direction value) {
            if (value != null) {
                return StringUtils.capitalize(value.name().toLowerCase());
            } else {
                return "None";
            }
        }

    }

}
