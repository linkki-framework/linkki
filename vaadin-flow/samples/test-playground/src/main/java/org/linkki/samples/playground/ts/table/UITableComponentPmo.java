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

package org.linkki.samples.playground.ts.table;

import java.util.List;
import java.util.stream.Stream;

import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;

import com.github.javafaker.Faker;
import com.vaadin.flow.theme.lumo.LumoUtility;

@BindStyleNames(LumoUtility.Height.FULL)
@UISection(layout = SectionLayout.VERTICAL)
public class UITableComponentPmo {

    private List<Person> data;
    private final SimpleItemSupplier<PersonRowPmo, Person> itemSupplier;

    public UITableComponentPmo() {
        data = List.of(createNewPerson());
        itemSupplier = new SimpleItemSupplier<>(() -> data, PersonRowPmo::new);
    }

    private Person createNewPerson() {
        var faker = new Faker();
        return new Person(faker.name().fullName(), faker.number().numberBetween(10, 100));
    }

    @SectionHeader
    @UIButton(position = -1, caption = "Add a new person")
    public void addPerson() {
        data = Stream.concat(data.stream(), Stream.of(createNewPerson())).toList();
    }

    @BindStyleNames(LumoUtility.Height.FULL)
    @BindPlaceholder("There are no person to be shown. Add a new person by clicking on the header button.")
    @UITableComponent(position = 0, rowPmoClass = PersonRowPmo.class)
    public List<PersonRowPmo> getRows() throws InterruptedException {
        Thread.sleep(2000);
        return itemSupplier.get();
    }

}
