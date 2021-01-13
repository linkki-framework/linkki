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

package org.linkki.samples.treetable.dynamic.pmo;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Function;
import java.util.function.Predicate;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;
import org.linkki.samples.treetable.dynamic.model.Player.Position;

public abstract class PlayerTableRowPmo {

    @UITextField(position = 10, label = "Team")
    public abstract String getTeam();

    @UITableColumn(width = 200)
    @UIComboBox(position = 20, label = "Position", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, itemCaptionProvider = PositionCaptionProvider.class)
    public abstract Position getPosition();

    @UITextField(position = 30, label = "First Name")
    public abstract String getFirstName();

    @UITableColumn(collapsible = CollapseMode.COLLAPSIBLE)
    @UITextField(position = 40, label = "Last Name")
    public abstract String getLastName();

    @UITableColumn(width = 60, collapsible = CollapseMode.INITIALLY_COLLAPSED)
    @UILabel(position = 50, label = "Age")
    public abstract String getAge();


    protected static int getAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    protected static <S, T> Predicate<? super S> by(Function<S, T> attributeAccessor,
            Predicate<? super T> attributePredicate) {
        return s -> attributePredicate.test(attributeAccessor.apply(s));
    }

    public static class PositionCaptionProvider implements ItemCaptionProvider<Position> {

        @Override
        public String getCaption(Position value) {
            return getName(value);
        }

        public static String getName(Position value) {
            if (value == null) {
                return "";
            }
            switch (value) {
                case TW:
                    return "goalkeeper";
                case AB:
                    return "defence";
                case MF:
                    return "midfield";
                case ST:
                    return "offence";

                default:
                    return "";
            }
        }

    }

}
