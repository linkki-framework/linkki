/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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
    public void getTextField() {
        // nothing to do
    }

    @UITextField(position = 2)
    public void getCbField() {
        // nothing to do
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

