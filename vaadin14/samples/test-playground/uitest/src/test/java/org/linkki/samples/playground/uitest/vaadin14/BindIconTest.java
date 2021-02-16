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

package org.linkki.samples.playground.uitest.vaadin14;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.dynamicannotations.BindIconComponentsPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.TestBenchElement;


public class BindIconTest extends AbstractUiTest {

    private static final String VAADIN_ANCHOR_ICON = "vaadin:anchor";
    private static final String VAADIN_DOWNLOAD_ICON = "vaadin:download";
    private static final String VAADIN_ARROW_RIGHT_ICON = "vaadin:arrow-circle-right";

    private static final String BUTTON_ID = "buttonWithStaticIcon";
    private static final String DOWNLOAD_LINK_FIELD_ID = "downloadLinkField";
    private static final String LINK_FIELD_ID = "linkField";

    @BeforeEach
    public void setup() {
        openTab(PlaygroundApplicationUI.DYNAMIC_ASPECT_TAB_ID);
    }

    @Test
    public void testBindIcon_withButton() {
        VerticalLayoutElement section = getSection(BindIconComponentsPmo.class);
        ButtonElement button = section.$(ButtonElement.class).id(BUTTON_ID);

        WebElement icon = getIconElement(button);
        assertThat(icon.getAttribute("icon"), is(VAADIN_ANCHOR_ICON));
    }

    @Test
    public void testBindIcon_withLinkAndIconOnly() {
        VerticalLayoutElement section = getSection(BindIconComponentsPmo.class);
        AnchorElement anchor = section.$(AnchorElement.class).id(DOWNLOAD_LINK_FIELD_ID);

        WebElement icon = getIconElement(anchor);
        assertThat(anchor.getText(), is(""));
        assertThat(icon.getAttribute("icon"), is(VAADIN_DOWNLOAD_ICON));
    }

    @Test
    public void testBindIcon_withLinkTextAndIcon() {
        VerticalLayoutElement section = getSection(BindIconComponentsPmo.class);
        AnchorElement anchor = section.$(AnchorElement.class).id(LINK_FIELD_ID);

        WebElement icon = getIconElement(anchor);
        assertThat(anchor.getText(), is("Click to continue"));
        assertThat(icon.getAttribute("icon"), is(VAADIN_ARROW_RIGHT_ICON));
    }

    private VerticalLayoutElement getSection(Class<?> cls) {
        return $(VerticalLayoutElement.class).id(cls.getSimpleName());
    }

    private WebElement getIconElement(TestBenchElement element) {
        return element.findElement(By.tagName("iron-icon"));
    }

}