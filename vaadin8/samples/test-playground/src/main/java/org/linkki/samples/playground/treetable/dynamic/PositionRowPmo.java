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

package org.linkki.samples.playground.treetable.dynamic;

import static java.util.Comparator.comparing;
import static java.util.function.Predicate.isEqual;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.linkki.samples.playground.treetable.dynamic.Player.Position;

public class PositionRowPmo extends CategoryRowPmo<Player, PlayerRowPmo> {

    private final Position position;

    public PositionRowPmo(Supplier<Stream<Player>> playerSupplier, Position position) {
        super(playersByPosition(playerSupplier, position),
                (players) -> players.sorted(comparing(Player::getLastName)),
                PlayerRowPmo::new);
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    private static Supplier<Stream<Player>> playersByPosition(Supplier<Stream<Player>> playerSupplier,
            Position position) {
        return () -> playerSupplier.get().filter(by(Player::getPosition, isEqual(position)));
    }

}
