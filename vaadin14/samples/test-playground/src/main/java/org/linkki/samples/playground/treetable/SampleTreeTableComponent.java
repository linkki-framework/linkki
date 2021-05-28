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
package org.linkki.samples.playground.treetable;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.treetable.dynamic.Bundesliga;
import org.linkki.samples.playground.treetable.dynamic.League;
import org.linkki.samples.playground.treetable.dynamic.LeagueTablePmo;
import org.linkki.samples.playground.treetable.dynamic.PlayerTableRowPmo;
import org.linkki.samples.playground.treetable.fixed.AbstractPersonRowPmo;
import org.linkki.samples.playground.treetable.fixed.PersonRepository;
import org.linkki.samples.playground.treetable.fixed.PersonTablePmo;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class SampleTreeTableComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private final PersonRepository personRepository = new PersonRepository();
    private final League league = new Bundesliga();

    public SampleTreeTableComponent() {

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        BindingContext bindingContext = new BindingContext();

        Grid<AbstractPersonRowPmo> personsTable = GridComponentCreator
                .createGrid(new PersonTablePmo(personRepository::getPersons),
                            bindingContext);
        Grid<PlayerTableRowPmo> leagueTable = GridComponentCreator
                .createGrid(new LeagueTablePmo(league), bindingContext);

        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);
        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("PersonTable")
                                       .caption("PersonTable")
                                       .content(personsTable)
                                       .build(),
                               LinkkiTabSheet.builder("LeageTable")
                                       .caption("LeagueTable")
                                       .content(leagueTable)
                                       .build());

        add(tabLayout);

    }

}