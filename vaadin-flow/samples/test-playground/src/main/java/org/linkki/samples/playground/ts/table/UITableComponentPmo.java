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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;

import com.github.javafaker.Faker;
import com.vaadin.flow.theme.lumo.LumoUtility;

@BindStyleNames(LumoUtility.Height.FULL)
@UISection(layout = SectionLayout.VERTICAL)
public class UITableComponentPmo {

    private final List<Person> data;
    private final SimpleItemSupplier<PersonRowPmo, Person> itemSupplier;
    private boolean exception;

    public UITableComponentPmo() {
        data = new ArrayList<>();
        itemSupplier = new SimpleItemSupplier<>(() -> data, m -> new PersonRowPmo(m, () -> data.remove(m)));
        exception = false;
        addPerson();
    }

    private Person createNewPerson() {
        var faker = new Faker();
        return new Person(faker.name().fullName(), faker.number().numberBetween(10, 100));
    }

    @SectionHeader
    @UIButton(position = -90, caption = "Add a new person")
    public void addPerson() {
        data.add(createNewPerson());
    }

    @SectionHeader
    @UICheckBox(position = -80, caption = "Throw exception when getting items")
    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    @BindStyleNames(LumoUtility.Height.FULL)
    @BindPlaceholder("There are no person to be shown. Add a new person by clicking on the header button.")
    @UITableComponent(position = 20, rowPmoClass = PersonRowPmo.class)
    public CompletableFuture<List<PersonRowPmo>> getRows() {
        return CompletableFuture.supplyAsync(this::getPersonsAsync, Executors.newFixedThreadPool(1));
    }

    private List<PersonRowPmo> getPersonsAsync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CompletionException(e);
        }
        if (exception) {
            throw new CompletionException(new RuntimeException("Exception when retrieving table items. " +
                    "This message should not be displayed for security reasons, but should be logged."));
        } else {
            return itemSupplier.get();
        }
    }

    record RecordPmo(@BindVisible
                     @UILabel(position = 10)
                     String field1,
                     @UILabel(position = 20)
                     String field3) {

        boolean isField1Visible() {
            return false;
        }

        @UILabel(position = 15)
        public String getField2() {
            return field1 + " derived";
        }
    }
}