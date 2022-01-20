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

package org.linkki.samples.playground.uitestnew.ts.aspects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class BindPlaceholderTest extends PlaygroundUiTest {

    private TestCaseComponentElement section;

    @BeforeEach
    public void setup() {
        super.setUp();
        section = goToTestCase(PlaygroundApplicationView.TS008,
                               PlaygroundApplicationView.TC010);
    }

    @Test
    public void testPlaceholderStatic() {
        TextFieldElement placeholderTextField = section.$(TextFieldElement.class).id("placeholderStaticText");

        assertThat(placeholderTextField.getPlaceholder(), is("A nice static placeholder"));
    }

    @Test
    public void testPlaceholderDynamic_WithValue() {
        TextFieldElement placeholderTextField = section.$(TextFieldElement.class).id("placeholderDynamicText");
        TextFieldElement input = section.$(TextFieldElement.class).id("placeholderText");

        input.setValue("I can be changed");

        assertThat(placeholderTextField.getPlaceholder(), is("I can be changed"));

        input.setValue("I got changed");

        assertThat(placeholderTextField.getPlaceholder(), is("I got changed"));
    }

    @Test
    public void testPlaceholderDynamic_WithEmptyValue() {
        TextFieldElement placeholderTextField = section.$(TextFieldElement.class).id("placeholderDynamicText");
        TextFieldElement input = section.$(TextFieldElement.class).id("placeholderText");

        input.setValue("I can be changed");

        assertThat(placeholderTextField.getPlaceholder(), is("I can be changed"));

        input.setValue("");

        assertThat(placeholderTextField.getPlaceholder(), is(""));
    }

}
