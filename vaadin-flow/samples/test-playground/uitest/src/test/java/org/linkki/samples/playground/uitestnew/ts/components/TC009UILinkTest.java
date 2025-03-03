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

package org.linkki.samples.playground.uitestnew.ts.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.samples.playground.uitestnew.ts.components.util.IconTestUtil.verifyIconPosition;

import java.net.URISyntaxException;

import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.ts.components.LinkPmo;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
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
        assertThat(link.getContent().getDomAttribute("href")).isEqualTo("http://faktorzehn.de/");
        assertThat(link.getText()).isEqualTo("FaktorZehn");
    }

    @Test
    void testLink_withNullLink_shouldBeShowAsLabel() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue(null);

        assertThat(link.getDomAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_withEmptyLink_shouldBeShowAsLabel() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue("");

        assertThat(link.getDomAttribute("href")).isNull();
        assertThat(link.getCssValue("pointer-events")).isEqualTo("none");
        assertThat(link.getCssValue("text-decoration")).startsWith("none");
    }

    @Test
    void testLink_withWhiteSpaceLink_shouldBeShowAsLabel() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue(" ");

        assertThat(link.getDomAttribute("href")).isNull();
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

    @Test
    void testLink_absoluteURLWithSameContextButWrongPathAndTargetBlank_RouteErrorPage() throws URISyntaxException {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        var currentUrl = getDriver().getCurrentUrl();
        URIBuilder uriBuilder = new URIBuilder(currentUrl);
        uriBuilder.setPathSegmentsRootless(uriBuilder.getPathSegments().get(0));
        uriBuilder.appendPathSegments("non-existent");
        address.setValue(uriBuilder.toString());

        String originalWindow = getDriver().getWindowHandle();
        assertThat(getDriver().getWindowHandles().size()).isOne();

        target.selectByText(UILink.LinkTarget.BLANK);

        link.click();

        waitUntil(ExpectedConditions.numberOfWindowsToBe(2));

        // Loop through until we find a new window handle
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }

        var title = getDriver().getTitle();

        getDriver().close();
        getDriver().switchTo().window(originalWindow);

        assertThat(title).contains("Unerwarteter Fehler");
    }

    @Test
    void testLink_absoluteURLWithSameContextButWrongPathAndTargetSelf_RouteErrorPage() throws URISyntaxException {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        var currentUrl = getDriver().getCurrentUrl();
        URIBuilder uriBuilder = new URIBuilder(currentUrl);
        uriBuilder.setPathSegmentsRootless(uriBuilder.getPathSegments().get(0));
        uriBuilder.appendPathSegments("non-existent");
        address.setValue(uriBuilder.toString());

        target.selectByText(UILink.LinkTarget.SELF);
        link.click();

        var title = getDriver().getTitle();

        // we need to go back to the application
        getDriver().navigate().back();

        assertThat(title).contains("Unerwarteter Fehler");
        assertThat(getDriver().getWindowHandles().size()).isOne();
    }

    @Test
    void testLink_absoluteURLWithSameContextButWrongPathAndTargetParent_RouteErrorPage() throws URISyntaxException {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        var currentUrl = getDriver().getCurrentUrl();
        URIBuilder uriBuilder = new URIBuilder(currentUrl);
        uriBuilder.setPathSegmentsRootless(uriBuilder.getPathSegments().get(0));
        uriBuilder.appendPathSegments("non-existent");
        address.setValue(uriBuilder.toString());

        target.selectByText(UILink.LinkTarget.PARENT);
        link.click();

        var title = getDriver().getTitle();

        // we need to go back to the application
        getDriver().navigate().back();

        assertThat(title).contains("Unerwarteter Fehler");
        assertThat(getDriver().getWindowHandles().size()).isOne();
    }

    @Test
    void testLink_absoluteURLWithOtherContextAndTargetBlank_404() throws URISyntaxException {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        var currentUrl = getDriver().getCurrentUrl();
        URIBuilder uriBuilder = new URIBuilder(currentUrl);
        uriBuilder.setPathSegmentsRootless("non-existent");
        address.setValue(uriBuilder.toString());

        String originalWindow = getDriver().getWindowHandle();
        assertThat(getDriver().getWindowHandles().size()).isOne();

        target.selectByText(UILink.LinkTarget.BLANK);

        link.click();

        waitUntil(ExpectedConditions.numberOfWindowsToBe(2));

        // Loop through until we find a new window handle
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }

        var title = getDriver().getTitle();

        getDriver().close();
        getDriver().switchTo().window(originalWindow);

        assertThat(title).contains("404");
    }

    @Test
    void testLink_absoluteURLWithOtherContextAndTargetSelf_404() throws URISyntaxException {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        var currentUrl = getDriver().getCurrentUrl();
        URIBuilder uriBuilder = new URIBuilder(currentUrl);
        uriBuilder.setPathSegmentsRootless("non-existent");
        address.setValue(uriBuilder.toString());

        target.selectByText(UILink.LinkTarget.SELF);
        link.click();

        var title = getDriver().getTitle();

        // we need to go back to the application
        getDriver().navigate().back();

        assertThat(title).contains("404");
        assertThat(getDriver().getWindowHandles().size()).isOne();
    }

    @Test
    void testLink_absoluteURLWithOtherContextAndTargetParent_404() throws URISyntaxException {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        var currentUrl = getDriver().getCurrentUrl();
        URIBuilder uriBuilder = new URIBuilder(currentUrl);
        uriBuilder.setPathSegmentsRootless("non-existent");
        address.setValue(uriBuilder.toString());

        target.selectByText(UILink.LinkTarget.PARENT);
        link.click();

        var title = getDriver().getTitle();

        // we need to go back to the application
        getDriver().navigate().back();

        assertThat(title).contains("404");
        assertThat(getDriver().getWindowHandles().size()).isOne();
    }

    @Test
    void testLink_relativeURLWithWrongPathAndTargetBlank_RouteErrorPage() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue("non-existent");

        String originalWindow = getDriver().getWindowHandle();
        assertThat(getDriver().getWindowHandles().size()).isOne();

        target.selectByText(UILink.LinkTarget.BLANK);

        link.click();

        waitUntil(ExpectedConditions.numberOfWindowsToBe(2));

        // Loop through until we find a new window handle
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }

        var title = getDriver().getTitle();

        getDriver().close();
        getDriver().switchTo().window(originalWindow);

        assertThat(title).contains("Unerwarteter Fehler");
    }

    @Test
    void testLink_relativeURLWithWrongPathAndTargetSelf_RouteErrorPage() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue("non-existent");

        target.selectByText(UILink.LinkTarget.SELF);
        link.click();

        var title = getDriver().getTitle();

        // we need to go back to the application
        getDriver().navigate().back();

        assertThat(title).contains("Unerwarteter Fehler");
        assertThat(getDriver().getWindowHandles().size()).isOne();
    }

    @Test
    void testLink_relativeURLWithWrongPathAndTargetParent_RouteErrorPage() {
        var section = getSection(LinkPmo.class);
        var address = section.$(TextFieldElement.class).id("href");
        var target = section.$(ComboBoxElement.class).id("linkTarget");
        var link = section.$(LinkkiTextElement.class).id("link").getContent();

        address.setValue("non-existent");

        target.selectByText(UILink.LinkTarget.PARENT);
        link.click();

        var title = getDriver().getTitle();

        // we need to go back to the application
        getDriver().navigate().back();

        assertThat(title).contains("Unerwarteter Fehler");
        assertThat(getDriver().getWindowHandles().size()).isOne();
    }
}
