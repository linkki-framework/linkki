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
package org.linkki.samples.playground.search;

import java.time.LocalDate;
import java.util.List;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;
import org.linkki.samples.playground.search.service.SampleModelObject;
import org.linkki.search.annotation.UISearchResultAction;

public class SampleSearchResultRowPmo {

    private final SampleModelObject modelObject;
    private final List<MenuItemDefinition> additionalActions;

    public SampleSearchResultRowPmo(SampleModelObject result,
            List<MenuItemDefinition> additionalActions) {
        this.modelObject = result;
        this.additionalActions = additionalActions;
    }

    public SampleModelObject getModelObject() {
        return modelObject;
    }

    @UILink(position = 10, label = "Partnernummer")
    public String getPartnernummer() {
        return modelObject.getLink();
    }

    public String getPartnernummerCaption() {
        return modelObject.getPartnerNumber();
    }

    @UILabel(label = "Name", position = 20)
    public String getName() {
        return modelObject.getName();
    }

    @UILabel(label = "Geburtsdatum", position = 25)
    public LocalDate getGeburtsdatum() {
        return modelObject.getGeburtsdatum();
    }

    @UILabel(label = "Geschlecht", position = 30)
    public String getGeschlecht() {
        return modelObject.getGeschlecht();
    }

    @UITableColumn(flexGrow = 10)
    @UILabel(label = "Adresse", position = 40)
    public String getAdresse() {
        return modelObject.getAdresse();
    }

    @UISearchResultAction
    public List<MenuItemDefinition> getActions() {
        return additionalActions;
    }
}