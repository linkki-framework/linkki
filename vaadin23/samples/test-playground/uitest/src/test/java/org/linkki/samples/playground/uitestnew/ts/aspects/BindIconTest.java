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
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.AnchorElement;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.testbench.IconElement;
import com.vaadin.testbench.TestBenchElement;


public class BindIconTest extends PlaygroundUiTest {

    private static final String VAADIN_STATIC_ICON = "vaadin:compile";
    private static final String VAADIN_MOBILE_ICON = "vaadin:mobile";
    private static final String VAADIN_DOWNLOAD_ICON = "vaadin:download";

    private static final String ICON_COMBOBOX_ID = "componentIcon";
    private static final String STATIC_LINK = "staticLink";
    private static final String DYNAMIC_LINK = "dynamicLink";
    private TestCaseComponentElement section;


    @BeforeEach
    public void setup() {
        section = goToTestCase(PlaygroundApplicationView.TS008, PlaygroundApplicationView.TC007);
    }

    /**
     * Button with @BindIcon should show an icon even if showIcon = false in @UIButton
     */
    @Test
    public void testBindIcon_withButton() {
        ButtonElement button = section.$(ButtonElement.class).id("staticButton");

        WebElement icon = getIconElement(button);
        assertThat(icon.getAttribute("icon"), is(VAADIN_STATIC_ICON));
    }

    /**
     * Dynamic @BindIcon should work and should override icon attribute of @UIButton
     */
    @Test
    public void testBindIcon_withButton_dynamic() {
        selectCombobox(ICON_COMBOBOX_ID, VaadinIcon.MOBILE.toString());

        ButtonElement button = section.$(ButtonElement.class).id("dynamicButton");
        assertThat(getIconElement(button).getAttribute("icon"), is(VAADIN_MOBILE_ICON));

        selectCombobox(ICON_COMBOBOX_ID, VaadinIcon.DOWNLOAD.toString());
        assertThat(getIconElement(button).getAttribute("icon"), is(VAADIN_DOWNLOAD_ICON));
    }

    @Test
    public void testBindIcon_withLinkAndIconOnly() {
        AnchorElement anchor = section.$(AnchorElement.class).id(STATIC_LINK);

        WebElement icon = getIconElement(anchor);
        assertThat(anchor.getText(), is("Text"));
        assertThat(icon.getAttribute("icon"), is(VAADIN_STATIC_ICON));
    }

    @Test
    public void testBindIcon_withLinkTextAndIcon() {
        selectCombobox(ICON_COMBOBOX_ID, VaadinIcon.MOBILE.toString());

        AnchorElement anchor = section.$(AnchorElement.class).id(DYNAMIC_LINK);
        assertThat(anchor.getText(), is("Text"));
        assertThat(getIconElement(anchor).getAttribute("icon"), is(VAADIN_MOBILE_ICON));


        selectCombobox(ICON_COMBOBOX_ID, VaadinIcon.MOBILE.toString());
        assertThat(getIconElement(anchor).getAttribute("icon"), is(VAADIN_MOBILE_ICON));
    }

    private WebElement getIconElement(TestBenchElement element) {
        return element.$(IconElement.class).first();
    }

}