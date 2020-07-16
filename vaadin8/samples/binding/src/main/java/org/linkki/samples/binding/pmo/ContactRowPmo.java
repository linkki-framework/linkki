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
package org.linkki.samples.binding.pmo;

import java.util.function.Consumer;

import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;
import org.linkki.samples.binding.model.Contact;

import com.vaadin.icons.VaadinIcons;

public class ContactRowPmo {

    public static final String PROPERTY_FAVORITE = "favorite";

    private final Contact contact;
    private final Consumer<Contact> editAction;
    private final Consumer<Contact> deleteAction;

    public ContactRowPmo(Contact contact, Consumer<Contact> editAction, Consumer<Contact> deleteAction) {
        this.contact = contact;
        this.editAction = editAction;
        this.deleteAction = deleteAction;
    }

    @UITableColumn(width = 50)
    @UICheckBox(position = 1, label = "&#9733;", caption = "")
    public boolean isFavorite() {
        return contact.isFavorite();
    }

    public void setFavorite(boolean favorite) {
        contact.setFavorite(favorite);
    }

    @UITableColumn(width = 50)
    // tag::contactRowPmo-labelHtmlContent[]
    @UILabel(position = 5, label = "", htmlContent = true)
    public String getGender() {
        switch (contact.getGender()) {
            case FEMALE:
                return VaadinIcons.FEMALE.getHtml();
            case MALE:
                return VaadinIcons.MALE.getHtml();

            default:
                return VaadinIcons.CHILD.getHtml();
        }

    }
    // end::contactRowPmo-labelHtmlContent[]


    // tag::contactRowPmo-labelBinding[]
    @UITableColumn(expandRatio = 1F)
    @UILabel(position = 10, label = "Name")
    public String getName() {
        return contact.getName();
    }
    // end::contactRowPmo-labelBinding[]

    @UITableColumn(expandRatio = 2F, collapsible = CollapseMode.INITIALLY_COLLAPSED)
    @UILabel(position = 20, label = "Address")
    public String getAddress() {
        return contact.getAddress().asSingleLineString();
    }

    // tag::contactRowPmo-buttonBinding[]
    @UITableColumn(width = 50)
    @BindTooltip("Edit")
    @UIButton(position = 30, icon = VaadinIcons.EDIT, showIcon = true, caption = "")
    public void edit() {
        editAction.accept(contact);
    }
    // end::contactRowPmo-buttonBinding[]

    @UITableColumn(width = 50)
    @BindTooltip("Delete")
    @UIButton(position = 40, icon = VaadinIcons.TRASH, showIcon = true, caption = "")
    public void delete() {
        deleteAction.accept(contact);
    }
}