
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
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class CollidingPositionsPmo {

    @ModelObject
    public Person getPerson() {
        return null;
    }

    @UILabel(position = 10, modelAttribute = Person.PROPERTY_FIRSTNAME)
    public void label1() {
        // model binding
    }

    @UILabel(position = 10, modelAttribute = Person.PROPERTY_LASTNAME)
    public void label2() {
        // model binding
    }
}
