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
import java.util.concurrent.TimeUnit;

import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@BindStyleNames(LumoUtility.Height.FULL)
@UISection(layout = SectionLayout.VERTICAL)
public class UITableComponentPmo {

    @UINestedComponent(position = 10)
    public UITableComponentSectionPmo getFixHeightTable() {
        return new UITableComponentSectionPmo("Table with fix height", List.of("h-10"));
    }

    /**
     * Table taking up the rest of the space to test scrolling.
     */
    @BindStyleNames(LumoUtility.Flex.GROW)
    @UINestedComponent(position = 20)
    public UITableComponentSectionPmo getFullHeightTable() {
        return new UITableComponentSectionPmo("Table with rest of the height", List.of(LumoUtility.Flex.GROW));
    }

    @BindStyleNames(LumoUtility.Height.FULL)
    @BindCaption
    @UISection(layout = SectionLayout.VERTICAL)
    public static class UITableComponentSectionPmo {

        private final PersonRepository repo;
        private final SimpleItemSupplier<PersonRowPmo, Person> itemSupplier;
        private final String caption;
        private final List<String> styleNamesForTable;
        private boolean exception;
        private boolean reload;
        @CheckForNull
        private CompletableFuture<List<PersonRowPmo>> lastLoadedRows;

        public UITableComponentSectionPmo(String caption, List<String> styleNamesForTable) {
            this.repo = new PersonRepository();
            itemSupplier = new SimpleItemSupplier<>(repo::getData, person -> new PersonRowPmo(person, () -> {
                repo.remove(person);
                reload = true;
            }));
            this.caption = caption;
            this.styleNamesForTable = styleNamesForTable;
            this.exception = false;
            this.reload = true;
            addPerson();
        }

        public String getCaption() {
            return caption;
        }

        @SectionHeader
        @UIButton(position = -90, caption = "Add a new person")
        public void addPerson() {
            var faker = new Faker();
            var person = new Person(faker.name().fullName(), faker.number().numberBetween(10, 100));
            repo.add(person);
            reload = true;
        }

        @SectionHeader
        @UIButton(position = -85, caption = "Reload")
        public void reload() {
            reload = true;
        }

        @SectionHeader
        @UICheckBox(position = -80, caption = "Throw exception when getting items")
        public boolean isException() {
            return exception;
        }

        public void setException(boolean exception) {
            this.exception = exception;
        }

        @BindStyleNames
        @BindPlaceholder("There are no person to be shown. Add a new person by clicking on the header button.")
        @UITableComponent(position = 10, rowPmoClass = PersonRowPmo.class)
        public CompletableFuture<List<PersonRowPmo>> getRows() {
            if (lastLoadedRows == null || reload) {
                this.lastLoadedRows = CompletableFuture.supplyAsync(this::getPersonsAsync);
                reload = false;
            }
            return lastLoadedRows;
        }

        public List<String> getRowsStyleNames() {
            return styleNamesForTable;
        }

        /**
         * Simulate wait
         */
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
    }

    private static class PersonRepository {

        private final List<Person> data;

        private PersonRepository() {
            this.data = new ArrayList<>();
        }

        public void add(Person person) {
            data.add(person);
        }

        public void remove(Person person) {
            data.remove(person);
        }

        public List<Person> getData() {
            return ImmutableList.copyOf(data);
        }
    }

}