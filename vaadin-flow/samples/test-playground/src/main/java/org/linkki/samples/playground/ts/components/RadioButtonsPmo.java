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
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class RadioButtonsPmo {

    private Direction directionWithoutNull;
    private Direction directionWithNull;

    @UIRadioButtons(position = 0, label = "Without null", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL,
            itemCaptionProvider = CaptionProvider.class)
    public Direction getDirectionWithoutNull() {
        return directionWithoutNull;
    }

    public void setDirectionWithoutNull(Direction directionWithoutNull) {
        this.directionWithoutNull = directionWithoutNull;
    }

    @UIRadioButtons(position = 10, label = "With null", content = AvailableValuesType.ENUM_VALUES_INCL_NULL,
            itemCaptionProvider = CaptionProvider.class)
    public Direction getDirectionWithNull() {
        return directionWithNull;
    }

    public void setDirectionWithNull(Direction directionWithNull) {
        this.directionWithNull = directionWithNull;
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
            return StringUtils.capitalize(value.name().toLowerCase());
        }

        @Override
        public String getNullCaption() {
            return "None";
        }

    }

}
