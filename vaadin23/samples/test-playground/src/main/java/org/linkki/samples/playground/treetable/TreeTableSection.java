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
import org.linkki.samples.playground.treetable.dynamic.Bundesliga;
import org.linkki.samples.playground.treetable.dynamic.League;
import org.linkki.samples.playground.treetable.dynamic.LeagueTablePmo;
import org.linkki.samples.playground.treetable.fixed.PersonRepository;
import org.linkki.samples.playground.treetable.fixed.PersonTablePmo;

import com.vaadin.flow.component.Component;

public class TreeTableSection {

    private TreeTableSection() {
        // no instances
    }

    public static Component createPersonTreeTableSection() {
        final PersonRepository personRepository = new PersonRepository();
        BindingContext bindingContext = new BindingContext();
        return GridComponentCreator
                .createGrid(new PersonTablePmo(personRepository::getPersons),
                            bindingContext);
    }

    public static Component createLeagueTreeTableSection() {
        final League league = new Bundesliga();
        BindingContext bindingContext = new BindingContext();
        return GridComponentCreator.createGrid(new LeagueTablePmo(league),
                                               bindingContext);

    }

    public static Component createUpdateNodeTreeTableSection() {
        return GridComponentCreator
                .createGrid(new TreeTableUpdateNodePmo(),
                            new BindingContext(
                                    TreeTableUpdateNodePmo.class.getSimpleName()));
    }
}