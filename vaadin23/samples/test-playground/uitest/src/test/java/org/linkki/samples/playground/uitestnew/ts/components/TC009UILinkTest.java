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
import static org.linkki.samples.playground.uitestnew.ts.components.util.IconTestUtil.verifyIconPosition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.components.LinkPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

class TC009UILinkTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS005, TestScenarioView.TC009);
    }

    @Test
    void test() {
        var section = getSection(LinkPmo.class);
        var caption = section.$(TextFieldElement.class).id("caption");
        var address = section.$(TextFieldElement.class).id("href");

        caption.setValue("FaktorZehn");
        address.setValue("http://faktorzehn.de/");

        var link = section.$(LinkkiTextElement.class).id("link");
        assertThat(link.getContent().getAttribute("href")).isEqualTo("http://faktorzehn.de/");
        assertThat(link.getText()).isEqualTo("FaktorZehn");
    }

    @Test
    void testLink_withNullLink_shouldBeShowAsLabel() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue(null);

        assertThat(link.getAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_withEmptyLink_shouldBeShowAsLabel() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue("");

        assertThat(link.getAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_withWhiteSpaceLink_shouldBeShowAsLabel() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue(" ");

        assertThat(link.getAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_WithIconLeft() {
        LinkkiTextElement link = $(LinkkiTextElement.class).id("linkIconLeft");

        verifyIconPosition(link, IconPosition.LEFT);
    }

    @Test
    void testLink_WithIconRight() {
        LinkkiTextElement link = $(LinkkiTextElement.class).id("linkIconRight");

        verifyIconPosition(link, IconPosition.RIGHT);
    }
}
