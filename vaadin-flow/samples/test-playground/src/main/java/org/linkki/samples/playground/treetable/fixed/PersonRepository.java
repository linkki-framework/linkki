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
package org.linkki.samples.playground.treetable.fixed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.linkki.samples.playground.treetable.fixed.Person.PersonBuilder;

public class PersonRepository {

    // from https://www.briandunning.com/sample-data/
    private static final String PERSON_SAMPLE_DATA = "ca-500.csv";
    private static final Pattern COMMA_AND_QUOTES = Pattern.compile("\",\"");
    private final List<Person> persons;

    public PersonRepository() {
        try (InputStream s = getClass().getClassLoader().getResourceAsStream(PERSON_SAMPLE_DATA);
                Reader r = new InputStreamReader(s, StandardCharsets.UTF_8);
                var br = new BufferedReader(r)) {
            persons = br.lines()
                    .skip(1)
                    .map(PersonRepository::csvToPerson)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Person csvToPerson(String csvLine) {
        String[] values = csvLine.split(COMMA_AND_QUOTES.pattern());
        return new PersonBuilder()
                .firstName(values[0].substring(1))
                .lastName(values[1])
                .company(values[2])
                .address(values[3])
                .city(values[4])
                .province(values[5])
                .zipCode(values[6])
                .phone1(values[7])
                .phone2(values[8])
                .email(values[9])
                .web(values[10].substring(0, values[10].length() - 1)).build();
    }

    public List<Person> getPersons() {
        return persons;
    }

}
