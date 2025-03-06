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

package org.linkki.samples.playground.ts.formelements;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class RadioButtonsPmo {

    private boolean childrenReadonly = true;
    private boolean childrenEnabled = false;
    private Direction directionWithoutNull = Direction.UP;
    private Direction directionWithNull;
    @ModelObject
    private final BooleanModelObject modelObject = new BooleanModelObject();

    @SectionHeader
    @UICheckBox(position = -20)
    public boolean isChildrenReadonly() {
        return childrenReadonly;
    }

    public void setChildrenReadonly(boolean childrenReadonly) {
        this.childrenReadonly = childrenReadonly;
    }

    @SectionHeader
    @UICheckBox(position = -10)
    public boolean isChildrenEnabled() {
        return childrenEnabled;
    }

    public void setChildrenEnabled(boolean childrenEnabled) {
        this.childrenEnabled = childrenEnabled;
    }

    @BindReadOnly(BindReadOnly.ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 0,
            label = "Without null",
            content = AvailableValuesType.ENUM_VALUES_EXCL_NULL,
            itemCaptionProvider = CaptionProvider.class,
            enabled = EnabledType.DYNAMIC)
    public Direction getDirectionWithoutNull() {
        return directionWithoutNull;
    }

    public void setDirectionWithoutNull(Direction directionWithoutNull) {
        this.directionWithoutNull = directionWithoutNull;
    }

    public boolean isDirectionWithoutNullEnabled() {
        return isChildrenEnabled();
    }

    public boolean isDirectionWithoutNullReadOnly() {
        return isChildrenReadonly();
    }

    @BindReadOnly(BindReadOnly.ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 10,
            label = "With null",
            content = AvailableValuesType.ENUM_VALUES_INCL_NULL,
            itemCaptionProvider = CaptionProvider.class,
            enabled = EnabledType.DYNAMIC)
    public Direction getDirectionWithNull() {
        return directionWithNull;
    }

    public void setDirectionWithNull(Direction directionWithNull) {
        this.directionWithNull = directionWithNull;
    }

    public boolean isDirectionWithNullEnabled() {
        return isChildrenEnabled();
    }

    public boolean isDirectionWithNullReadOnly() {
        return isChildrenReadonly();
    }

    @BindReadOnly(BindReadOnly.ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 20,
            label = "Primitive boolean",
            modelAttribute = BooleanModelObject.PRIMITIVE_BOOLEAN,
            enabled = EnabledType.DYNAMIC)
    public void primitiveBoolean() {
        // model binding
    }

    public boolean isPrimitiveBooleanEnabled() {
        return isChildrenEnabled();
    }

    public boolean isPrimitiveBooleanReadOnly() {
        return isChildrenReadonly();
    }

    @BindReadOnly(BindReadOnly.ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 30,
            label = "Object Boolean",
            content = AvailableValuesType.ENUM_VALUES_INCL_NULL,
            modelAttribute = BooleanModelObject.OBJECT_BOOLEAN,
            enabled = EnabledType.DYNAMIC)
    public void objectBoolean() {
        // model binding
    }

    public boolean isObjectBooleanEnabled() {
        return isChildrenEnabled();
    }

    public boolean isObjectBooleanReadOnly() {
        return isChildrenReadonly();
    }

    @BindReadOnly(BindReadOnly.ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 40,
            label = "Horizontal",
            buttonAlignment = AlignmentType.HORIZONTAL,
            content = AvailableValuesType.ENUM_VALUES_INCL_NULL,
            modelAttribute = BooleanModelObject.OBJECT_BOOLEAN,
            enabled = EnabledType.DYNAMIC)
    public void horizontal() {
        // model binding
    }

    public boolean isHorizontalEnabled() {
        return isChildrenEnabled();
    }

    public boolean isHorizontalReadOnly() {
        return isChildrenReadonly();
    }

    @BindReadOnly(BindReadOnly.ReadOnlyType.DYNAMIC)
    @UIRadioButtons(position = 50,
            label = "Object Boolean Exclusive Null",
            modelAttribute = BooleanModelObject.OBJECT_BOOLEAN,
            enabled = EnabledType.DYNAMIC)
    public void withoutNull() {
        // model binding
    }

    public boolean isWithoutNullEnabled() {
        return isChildrenEnabled();
    }

    public boolean isWithoutNullReadOnly() {
        return isChildrenReadonly();
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

    public static class BooleanModelObject {
        public static final String PRIMITIVE_BOOLEAN = "primitiveBoolean";
        public static final String OBJECT_BOOLEAN = "objectBoolean";

        private boolean primitiveBoolean;
        private Boolean objectBoolean;

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean primitiveBoolean) {
            this.primitiveBoolean = primitiveBoolean;
        }

        public Boolean getObjectBoolean() {
            return objectBoolean;
        }

        public void setObjectBoolean(Boolean objectBoolean) {
            this.objectBoolean = objectBoolean;
        }
    }

}
