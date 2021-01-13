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
package org.linkki.samples.treetable.ui;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.samples.treetable.dynamic.model.Bundesliga;
import org.linkki.samples.treetable.dynamic.model.League;
import org.linkki.samples.treetable.dynamic.pmo.LeagueTablePmo;
import org.linkki.samples.treetable.dynamic.pmo.PlayerTableRowPmo;
import org.linkki.samples.treetable.fixed.model.PersonRepository;
import org.linkki.samples.treetable.fixed.pmo.AbstractPersonRowPmo;
import org.linkki.samples.treetable.fixed.pmo.PersonTablePmo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(Lumo.class)
@Route("")
public class SampleTreeTableUI extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private final PersonRepository personRepository = new PersonRepository();
    private final League league = new Bundesliga();

    public SampleTreeTableUI() {
        UI.getCurrent().getPage().setTitle("linkki :: TreeTable Sample");

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        BindingContext bindingContext = new BindingContext();

        // TODO LIN-2065 use TabSheet when available

        Tab personTab = new Tab("PersonTable");
        Tab leagueTab = new Tab("LeagueTable");
        Tabs tabs = new Tabs();
        Div content = new Div();
        content.setSizeFull();

        Grid<AbstractPersonRowPmo> personsTable = GridComponentCreator
                .createGrid(new PersonTablePmo(personRepository::getPersons),
                            bindingContext);
        Grid<PlayerTableRowPmo> leagueTable = GridComponentCreator
                .createGrid(new LeagueTablePmo(league), bindingContext);
        tabs.addSelectedChangeListener(e -> {
            content.removeAll();
            if (e.getSelectedTab() == personTab) {
                personsTable.setSizeFull();
                content.add(personsTable);
            } else {
                leagueTable.setSizeFull();
                content.add(leagueTable);
            }
        });
        tabs.add(personTab, leagueTab);
        add(tabs);
        add(content);
    }

}