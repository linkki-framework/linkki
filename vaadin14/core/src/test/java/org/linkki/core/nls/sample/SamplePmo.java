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
package org.linkki.core.nls.sample;

import java.util.LinkedHashSet;
import java.util.Set;

import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UISubsetChooser;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;


@UISection(caption = "SamplePmoCaption")
public class SamplePmo {

    // must be different than the translated label
    public static final String PMO_LABEL = "No translation label";
    public static final String PMO_CAPTION = "No translation caption";

    public static final String PROPERTY_TEXTFIELD = "textField";
    public static final String PROPERTY_CBFIELD = "cbField";
    public static final String PROPERTY_MYBUTTON = "myButton";
    public static final String PROPERTY_MYBUTTON2 = "myButton2";
    public static final String PROPERTY_FOO = "foo";

    private final Set<TestEnum> foo = new LinkedHashSet<>();


    @UITextField(position = 1, label = PMO_LABEL)
    public String getTextField() {
        return "";
    }

    @UITextField(position = 2, label = PMO_LABEL)
    public String getCbField() {
        return "";
    }


    @UIButton(position = 3, label = PMO_LABEL, caption = PMO_CAPTION)
    public void myButton() {
        // nothing here
    }

    @UIButton(position = 4, caption = PMO_CAPTION)
    public void myButton2() {
        // nothing here
    }

    @UISubsetChooser(position = 5, label = "", leftColumnCaption = "Left Column Caption", rightColumnCaption = "Right Column Caption")
    public Set<TestEnum> getFoo() {
        return foo;
    }

    public void setFoo(Set<TestEnum> selectedFoos) {
        foo.clear();
        foo.addAll(selectedFoos);
    }

    public Set<TestEnum> getFooAvailableValues() {
        LinkedHashSet<TestEnum> someValues = new LinkedHashSet<>();
        someValues.add(TestEnum.ONE);
        someValues.add(TestEnum.TWO);
        someValues.add(TestEnum.THREE);
        return someValues;
    }


}

