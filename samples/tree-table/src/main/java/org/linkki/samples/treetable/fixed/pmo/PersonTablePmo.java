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

package org.linkki.samples.treetable.fixed.pmo;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.samples.treetable.fixed.model.Person;

@UISection
public class PersonTablePmo implements ContainerPmo<AbstractPersonRowPmo> {

    private List<AbstractPersonRowPmo> rows;

    public PersonTablePmo(Supplier<List<Person>> personSupplier) {
        Map<String, Map<String, List<Person>>> personsByProvinceAndCity = personSupplier.get().stream()
                .collect(groupingBy(Person::getProvince, TreeMap::new,
                                    groupingBy(Person::getCity, TreeMap::new, Collectors.toList())));
        rows = personsByProvinceAndCity.entrySet().stream()
                .map(byProvince -> new ProvinceRowPmo(byProvince.getKey(), byCityRows(byProvince)))
                .collect(Collectors.toList());
    }

    private @NonNull List<CityRowPmo> byCityRows(Entry<String, Map<String, List<Person>>> byProvince) {
        return byProvince.getValue().entrySet().stream()
                .map(byCity -> new CityRowPmo(byCity.getKey(), personRows(byCity)))
                .collect(Collectors.toList());
    }

    private @NonNull List<PersonRowPmo> personRows(Entry<String, List<Person>> byCity) {
        return byCity.getValue().stream()
                .map(p -> new PersonRowPmo(p))
                .collect(Collectors.toList());
    }

    @Override
    public List<AbstractPersonRowPmo> getItems() {
        return rows;
    }

    @Override
    public boolean isHierarchical() {
        return true;
    }

}
