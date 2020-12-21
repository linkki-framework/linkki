
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

import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class NonEmptyPmo {

    @ModelObject
    public Person getModelObject() {
        return null;
    }

    @BindReadOnly
    @UITextField(position = 10, label = "firstname", modelAttribute = Person.PROPERTY_FIRSTNAME)
    @UITextArea(position = 10, label = "firstname", modelAttribute = Person.PROPERTY_FIRSTNAME)
    public void firstname() {
        // model binding
    }

    @BindTooltip("lastname")
    @BindVisible
    @UITextField(position = 20, label = "lastname", modelAttribute = Person.PROPERTY_LASTNAME)
    public void lastname() {
        // model binding
    }

    @UITextArea(position = 20, label = "lastname")
    public String getLastname() {
        return getModelObject().getLastname();
    }
    
    public boolean isLastnameVisible() {
        return true;
    }

}
