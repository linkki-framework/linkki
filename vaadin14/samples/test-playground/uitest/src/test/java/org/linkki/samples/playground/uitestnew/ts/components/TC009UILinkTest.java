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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.components.LinkPmo;
import org.linkki.samples.playground.uitestnew.BaseUITest;

import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class TC009UILinkTest extends BaseUITest {

    @BeforeEach
    void setUp() {
        goToTestCase("TS005", "TC009");
    }

    @Test
    void test() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(LinkPmo.class.getSimpleName());
        TextFieldElement caption = section.$(TextFieldElement.class).id("caption");
        TextFieldElement address = section.$(TextFieldElement.class).id("href");

        caption.setValue("FaktorZehn");
        address.setValue("http://faktorzehn.de/");

        AnchorElement link = section.$(AnchorElement.class).id("link");
        assertThat(link.getAttribute("href")).isEqualTo("http://faktorzehn.de/");
        assertThat(link.getText()).isEqualTo("FaktorZehn");
    }

    @Test
    void testLink_withNullLink_shouldBeShowAsLabel() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(LinkPmo.class.getSimpleName());
        TextFieldElement address = section.$(TextFieldElement.class).id("href");
        AnchorElement link = section.$(AnchorElement.class).id("link");

        address.setValue(null);

        assertThat(link.getAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_withEmptyLink_shouldBeShowAsLabel() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(LinkPmo.class.getSimpleName());
        TextFieldElement address = section.$(TextFieldElement.class).id("href");
        AnchorElement link = section.$(AnchorElement.class).id("link");

        address.setValue("");

        assertThat(link.getAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_withWhiteSpaceLink_shouldBeShowAsLabel() {
        VerticalLayoutElement section = $(VerticalLayoutElement.class).id(LinkPmo.class.getSimpleName());
        TextFieldElement address = section.$(TextFieldElement.class).id("href");
        AnchorElement link = section.$(AnchorElement.class).id("link");

        address.setValue(" ");

        assertThat(link.getAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

}
