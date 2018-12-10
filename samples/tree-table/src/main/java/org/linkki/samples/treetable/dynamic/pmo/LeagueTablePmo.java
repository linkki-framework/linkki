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

package org.linkki.samples.treetable.dynamic.pmo;

import java.util.Optional;
import java.util.stream.Collectors;

import org.linkki.core.ui.table.SimpleTablePmo;
import org.linkki.core.ui.table.TableFooterPmo;
import org.linkki.samples.treetable.dynamic.model.League;
import org.linkki.samples.treetable.dynamic.model.Player;

public class LeagueTablePmo extends SimpleTablePmo<String, PlayerTableRowPmo> {

    private League league;

    public LeagueTablePmo(League league) {
        super(() -> league
                .getPlayers().stream()
                .map(Player::getTeam)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
        this.league = league;
    }

    @Override
    protected TeamRowPmo createRow(String team) {
        return new TeamRowPmo(league.getPlayers()::stream, team);
    }

    @Override
    public boolean isHierarchical() {
        return true;
    }

    @Override
    public int getPageLength() {
        return 18;
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.of(id -> {
            switch (id) {
                case "team":
                    return league
                            .getPlayers().stream()
                            .map(Player::getTeam)
                            .distinct().count() + " Teams";
                case "age":
                    return String.format("Ø %.2f", league
                            .getPlayers().stream()
                            .map(Player::getDateOfBirth)
                            .collect(Collectors.averagingInt(d -> PlayerTableRowPmo.getAge(d))));

                default:
                    return "";
            }
        });
    }

}
