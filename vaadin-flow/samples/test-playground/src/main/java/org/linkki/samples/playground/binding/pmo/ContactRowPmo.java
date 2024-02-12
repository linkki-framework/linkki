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
package org.linkki.samples.playground.binding.pmo;

import java.util.function.Consumer;

import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;
import org.linkki.core.util.HtmlContent;
import org.linkki.samples.playground.binding.model.Contact;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

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
    // TODO LIN-2150
    @UICheckBox(position = 1, label = "&#9733;", caption = "")
    public boolean isFavorite() {
        return contact.isFavorite();
    }

    public void setFavorite(boolean favorite) {
        contact.setFavorite(favorite);
    }

    @UITableColumn(width = 50)
    // tag::contactRowPmo-labelHtmlContent[]
    @UILabel(position = 5, label = "")
    public HtmlContent getGender() {
        var icon = switch (contact.getGender()) {
            case FEMALE -> VaadinIcon.FEMALE;
            case MALE -> VaadinIcon.MALE;
            default -> VaadinIcon.CHILD;
        };
        return HtmlContent.icon(icon.create());
    }
    // end::contactRowPmo-labelHtmlContent[]

    // tag::contactRowPmo-labelBinding[]
    @UITableColumn(flexGrow = 10)
    @UILabel(position = 10, label = "Name")
    public String getName() {
        return contact.getName();
    }
    // end::contactRowPmo-labelBinding[]

    @UITableColumn(flexGrow = 20, collapsible = CollapseMode.INITIALLY_COLLAPSED)
    @UILabel(position = 20, label = "Address")
    public String getAddress() {
        return contact.getAddress().asSingleLineString();
    }

    @UITableColumn(width = 60)
    // tag::contactRowPmo-buttonBinding[]
    @BindTooltip("Edit")
    @UIButton(position = 30,
            icon = VaadinIcon.EDIT,
            showIcon = true,
            caption = "",
            variants = ButtonVariant.LUMO_TERTIARY_INLINE)
    public void edit() {
        editAction.accept(contact);
    }
    // end::contactRowPmo-buttonBinding[]

    @UITableColumn(width = 60)
    @BindTooltip("Delete")
    @UIButton(position = 40,
            icon = VaadinIcon.TRASH,
            showIcon = true,
            caption = "",
            variants = ButtonVariant.LUMO_TERTIARY_INLINE)
    public void delete() {
        deleteAction.accept(contact);
    }
}