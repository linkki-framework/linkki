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
package org.linkki.core.nls.pmo.sample;

import java.util.LinkedHashSet;
import java.util.Set;

import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UISubsetChooser;
import org.linkki.core.ui.section.annotations.UITextField;


@UISection(caption = "SamplePmoCaption")
public class SamplePmo {

    private final Set<TestEnum> foo = new LinkedHashSet<>();


    @UITextField(position = 1, label = "Some Label")
    public String getTextField() {
        return "";
    }

    @UITextField(position = 2)
    public String getCbField() {
        return "";
    }


    @UIButton(position = 3, label = "Button label", caption = "Button Caption", showLabel = true)
    public void myButton() {
        // nothing here
    }

    @UIButton(position = 4, label = "Button2 label", caption = "Button2 Caption")
    public void myButton2() {
        // nothing here
    }

    @UISubsetChooser(position = 5, leftColumnCaption = "Left Column Caption", rightColumnCaption = "Right Column Caption")
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

