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

package org.linkki.samples.playground.ts.table;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;

public class PersonRowPmo {

    private final Person person;

    public PersonRowPmo(Person person) {
        this.person = person;
    }

    @ModelObject
    public Person getModelObject() {
        return this.person;
    }

    @UILabel(position = 0)
    public String getDisplayName() {
        return person.getName() + " (" + person.getAge() + ")";
    }

    @UIIntegerField(position = 10)
    public void age() {
        // model binding
    }

    @UILabel(position = 20)
    public void likes() {
        // model binding
    }

    @UIButton(position = 100, label = "Like")
    public void like() {
        person.like();
    }

}
