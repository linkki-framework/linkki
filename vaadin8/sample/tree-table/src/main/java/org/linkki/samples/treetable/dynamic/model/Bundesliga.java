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

package org.linkki.samples.treetable.dynamic.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.linkki.samples.treetable.dynamic.model.Player.Position;


public class Bundesliga implements League {

    private static final String PLAYER_DATA = "/bundesliga2018.csv";
    private static final String COMMA = ",";
    private final List<Player> players;

    public Bundesliga() {
        try (InputStream s = getClass().getClassLoader().getResourceAsStream(PLAYER_DATA);
                Reader r = new InputStreamReader(s, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(r)) {
            players = br.lines()
                    .skip(1)
                    .map(Bundesliga::csvToPlayer)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Player csvToPlayer(String csvLine) {
        String[] values = csvLine.split(COMMA);
        String firstName = values[0].substring(0, values[0].indexOf(' '));
        String lastName = values[0].substring(values[0].indexOf(' ') + 1);
        String team = values[1];
        LocalDate dateOfBirth = LocalDate.parse(values[2], DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY));
        Position position = Position.valueOf(values[3]);
        return new Player(firstName, lastName, team, dateOfBirth, position);
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

}
