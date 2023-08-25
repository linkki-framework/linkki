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

package org.linkki.samples.playground.table;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.faktorips.values.Decimal;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.samples.playground.table.NumberFooterTablePmo.NumberFooterRowPmo;

@UISection(caption = "Table with table right aligned column and table footer")
@BindStyleNames(LinkkiTheme.GRID_FOOTER_BOLD)
public class NumberFooterTablePmo implements ContainerPmo<NumberFooterRowPmo> {

    private final TableFooterPmo footer;

    public NumberFooterTablePmo() {
        this.footer = this::createFooter;
    }

    @Override
    public List<NumberFooterRowPmo> getItems() {
        return IntStream.range(0, 11)
                .boxed()
                .map(i -> new NumberFooterRowPmo())
                .collect(Collectors.toList());
    }

    @Override
    public int getPageLength() {
        return 0;
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.of(footer);
    }

    private String createFooter(String column) {
        switch (column) {
            case "decimal":
                return String.valueOf(getItems().stream()
                        .map(NumberFooterRowPmo::getDecimal)
                        .mapToDouble(Decimal::doubleValue)
                        .boxed()
                        .collect(Collectors.summingDouble(d -> d)));
            case "description":
                return "Total:";
            default:
                return "";
        }
    }

    public static class NumberFooterRowPmo {

        private static final SecureRandom SECURE_RANDOM = new SecureRandom();

        @UITableColumn(width = 100)
        @UILabel(position = 10)
        public String getDescription() {
            return "";
        }

        @UITableColumn(width = 300, textAlign = TextAlignment.RIGHT)
        @UILabel(position = 20)
        public Decimal getDecimal() {
            return Decimal.valueOf(SECURE_RANDOM.nextDouble() * 10);
        }
    }
}
