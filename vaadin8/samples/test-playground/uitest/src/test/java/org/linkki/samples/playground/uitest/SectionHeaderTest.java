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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Point;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

public class SectionHeaderTest extends AbstractUiTest {

    @Test
    public void testButtonLocation() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("SectionHeaderPmo");
        Point buttonLeft = section.$(ButtonElement.class).id("headerButtonLeft").getLocation();
        Point buttonRight = section.$(ButtonElement.class).id("headerButtonRight").getLocation();

        assertThat(buttonLeft.getY() == buttonRight.getY(), is(true));
        assertThat(buttonLeft.getX() < buttonRight.getX(), is(true));
    }

    @Test
    public void testButtonPress() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id("SectionHeaderPmo");
        ButtonElement buttonLeft = section.$(ButtonElement.class).id("headerButtonLeft");
        ButtonElement buttonRight = section.$(ButtonElement.class).id("headerButtonRight");
        LabelElement label = section.$(LabelElement.class).id("label");

        buttonLeft.click();
        assertThat(label.getText(), is("left"));
        buttonRight.click();
        assertThat(label.getText(), is("right"));
    }

}
