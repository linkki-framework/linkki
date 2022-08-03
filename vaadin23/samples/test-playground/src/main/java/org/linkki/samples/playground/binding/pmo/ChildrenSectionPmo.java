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
package org.linkki.samples.playground.binding.pmo;

import java.util.List;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.binding.model.ChildInfo;
import org.linkki.samples.playground.binding.model.Contact;

import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;

// tag::sectionCaption[]
@UISection(caption = "Children", closeable = true)
public class ChildrenSectionPmo {
    // end::sectionCaption[]

    @ModelObject(name = "Contact")
    private Contact contact;

    @CheckForNull
    @ModelObject
    private ChildInfo child;

    public ChildrenSectionPmo(Contact contact) {
        this.contact = contact;
        selectChild();
    }

    @UIIntegerField(position = 10, label = "Number of children", modelObject = "Contact",
            modelAttribute = Contact.PROPERTY_NO_OF_CHILDREN)
    public int getNoOfChildren() {
        return contact.getNoOfChildren();
    }

    public void setNoOfChildren(int noOfChildren) {
        contact.setNoOfChildren(noOfChildren);
        selectChild();
    }

    @Nullable
    @UIComboBox(position = 20, label = "", visible = VisibleType.DYNAMIC, content = AvailableValuesType.DYNAMIC)
    public ChildInfo getChild() {
        return child;
    }

    public void setChild(ChildInfo child) {
        this.child = child;
    }

    public List<ChildInfo> getChildAvailableValues() {
        return contact.getChildren();
    }

    public boolean isChildVisible() {
        return contact.getChildren().size() > 1;
    }

    @UITextField(position = 30, label = "Firstname", modelAttribute = ChildInfo.PROPERTY_FIRSTNAME,
            visible = VisibleType.DYNAMIC)
    public String getFirstname() {
        return child != null ? child.getFirstname() : "";
    }

    public void setFirstname(String firstname) {
        if (child != null) {
            child.setFirstname(firstname);
        }
    }

    public boolean isFirstnameVisible() {
        return child != null;
    }

    @UITextField(position = 40, label = "Lastname", modelAttribute = ChildInfo.PROPERTY_LASTNAME,
            visible = VisibleType.DYNAMIC)
    public String getLastname() {
        return child != null ? child.getLastname() : "";
    }

    public void setLastname(String lastname) {
        if (child != null) {
            child.setLastname(lastname);
        }
    }

    public boolean isLastnameVisible() {
        return child != null;
    }

    @UITextArea(position = 50, label = "Note", modelAttribute = ChildInfo.PROPERTY_NOTE, visible = VisibleType.DYNAMIC)
    public String getNote() {
        return child != null ? child.getNote() : "";
    }

    public void setNote(String note) {
        if (child != null) {
            child.setNote(note);
        }
    }

    public boolean isNoteVisible() {
        return child != null;
    }

    @UIButton(position = 60, showIcon = true, icon = VaadinIcon.TRASH, visible = VisibleType.DYNAMIC)
    public void remove() {
        contact.getChildren().remove(child);
        this.child = contact.getChildren().isEmpty() ? null : contact.getChildren().get(0);
    }

    public boolean isRemoveVisible() {
        return this.child != null;
    }

    public void reset(Contact newContact) {
        this.contact = newContact;
        selectChild();
    }

    private void selectChild() {
        List<ChildInfo> children = contact.getChildren();
        if (!children.contains(child)) {
            if (!children.isEmpty()) {
                child = contact.getChildren().get(0);
            } else {
                child = null;
            }
        }
    }
}