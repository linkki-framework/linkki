/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.samples.binding.pmo;

import java.util.function.Consumer;

import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.linkki.samples.binding.model.Person;

import com.vaadin.server.FontAwesome;

public class PersonRowPmo {

    private final Person person;
    private final Consumer<Person> editAction;
    private final Consumer<Person> deleteAction;

    public PersonRowPmo(Person person, Consumer<Person> editAction, Consumer<Person> deleteAction) {

        this.person = person;
        this.editAction = editAction;
        this.deleteAction = deleteAction;
    }

    @UITableColumn(expandRatio = 0.05F)
    // tag::personRowPmo-labelHtmlContent[]
    @UILabel(position = 1, label = "", htmlContent = true)
    public String getGender() {
        switch (person.getGender()) {
            case FEMALE:
                return FontAwesome.FEMALE.getHtml();
            case MALE:
                return FontAwesome.MALE.getHtml();

            default:
                return FontAwesome.GENDERLESS.getHtml();
        }

    }
    // end::personRowPmo-labelHtmlContent[]

    // tag::personRowPmo-labelBinding[]
    @UITableColumn(expandRatio = 0.25F)
    @UILabel(position = 10, label = "Name")
    public String getName() {
        return person.getName();
    }
    // end::personRowPmo-labelBinding[]

    @UITableColumn(expandRatio = 0.8F)
    @UILabel(position = 20, label = "Address")
    public String getAddress() {
        return person.getAddress().asSingleLineString();
    }

    // tag::personRowPmo-buttonBinding[]
    @UITableColumn(expandRatio = 0.1F)
    @UIToolTip(text = "Edit")
    @UIButton(position = 30, icon = FontAwesome.EDIT, showIcon = true)
    public void edit() {
        editAction.accept(person);
    }
    // end::personRowPmo-buttonBinding[]

    @UITableColumn(expandRatio = 0.1F)
    @UIToolTip(text = "Delete")
    @UIButton(position = 40, icon = FontAwesome.TRASH, showIcon = true)
    public void delete() {
        deleteAction.accept(person);
    }
}
