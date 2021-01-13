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

import static java.util.function.Predicate.isEqual;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.linkki.samples.treetable.dynamic.model.Player;
import org.linkki.samples.treetable.dynamic.model.Player.Position;

public class TeamRowPmo extends CategoryRowPmo<Position, PositionRowPmo> {

    private final String team;

    public TeamRowPmo(Supplier<Stream<Player>> playerSupplier, String team) {
        super(playersByTeam(playerSupplier, team),
                players -> positions(players),
                position -> new PositionRowPmo(playersByTeam(playerSupplier, team), position));
        this.team = team;
    }

    @Override
    public String getTeam() {
        return team;
    }

    private static Supplier<Stream<Player>> playersByTeam(Supplier<Stream<Player>> playerSupplier, String team) {
        return () -> playerSupplier.get().filter(by(Player::getTeam, isEqual(team)));
    }

    private static Stream<Position> positions(Stream<Player> players) {
        return players.map(Player::getPosition)
                .distinct()
                .sorted();
    }

}
