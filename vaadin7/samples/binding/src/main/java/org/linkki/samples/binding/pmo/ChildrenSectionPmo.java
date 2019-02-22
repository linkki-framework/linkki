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

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UIIntegerField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextArea;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.samples.binding.model.Contact;

@UISection(caption = "Children", closeable = true)
public class ChildrenSectionPmo {

    @ModelObject
    private Contact contact;

    public ChildrenSectionPmo(Contact contact) {
        this.contact = contact;
    }

    @UIIntegerField(position = 10, label = "Number of children", modelAttribute = Contact.PROPERTY_NO_OF_CHILDREN)
    public void noOfChildren() {
        // model binding
    }

    @UITextArea(position = 20, label = "Notes", modelAttribute = Contact.PROPERTY_NOTES_ON_CHILDREN, visible = VisibleType.DYNAMIC)
    public void notes() {
        // model binding
    }

    public boolean isNotesVisible() {
        return contact.getNoOfChildren() > 0;
    }

    public void reset(Contact newContact) {
        this.contact = newContact;
    }
}