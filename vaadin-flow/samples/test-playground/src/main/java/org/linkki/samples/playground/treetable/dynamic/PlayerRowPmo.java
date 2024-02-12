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

package org.linkki.samples.playground.treetable.dynamic;

import org.linkki.core.pmo.ModelObject;
import org.linkki.samples.playground.treetable.dynamic.Player.Position;

public class PlayerRowPmo extends PlayerTableRowPmo {

    private final Player player;

    public PlayerRowPmo(Player player) {
        this.player = player;
    }

    @ModelObject
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getTeam() {
        return player.getTeam();
    }

    public void setTeam(String team) {
        player.setTeam(team);
    }

    @Override
    public Position getPosition() {
        return player.getPosition();
    }

    public void setPosition(Position position) {
        player.setPosition(position);
    }

    @Override
    public String getFirstName() {
        return player.getFirstName();
    }

    public void setFirstName(String firstName) {
        player.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return player.getLastName();
    }

    public void setLastName(String lastName) {
        player.setLastName(lastName);
    }

    @Override
    public String getAge() {
        return Integer.toString(getAge(player.getDateOfBirth()));
    }

}
