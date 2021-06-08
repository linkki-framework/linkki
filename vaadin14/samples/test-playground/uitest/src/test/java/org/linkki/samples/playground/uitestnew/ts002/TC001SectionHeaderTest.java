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

package org.linkki.samples.playground.uitestnew.ts002;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitestnew.BaseUITest;
import org.linkki.samples.playground.uitestnew.pageobjects.LinkkiSectionCaptionElement;
import org.linkki.samples.playground.uitestnew.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.uitestnew.pageobjects.TestCaseSectionElement;
import org.openqa.selenium.Dimension;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;

public class TC001SectionHeaderTest extends BaseUITest {

    private TestCaseSectionElement testCaseSection;
    private LinkkiSectionElement section;
    private LinkkiSectionCaptionElement sectionCaption;
    private ButtonElement button1;
    private ButtonElement button2;
    private DivElement label;

    @BeforeAll
    void setup() {
        testCaseSection = gotoTestCaseSection("TS002", "TC001");

        section = testCaseSection.getContentWrapper().$(LinkkiSectionElement.class).first();
        sectionCaption = section.getCaption();

        button1 = sectionCaption.getHeaderButton("headerButtonLeft");
        button2 = sectionCaption.getHeaderButton("headerButtonRight");

        label = section.$(DivElement.class).id("label");
    }

    @Test
    void testSectionHeaderTitle() {
        assertThat(sectionCaption.getTitle().getText())
                .isEqualTo("I am a SectionHeader title");
    }

    @Test
    void testSectionHeaderButtons() {
        assertThat(sectionCaption.getHeaderButtons())
                .extracting(ButtonElement::getText)
                .contains("Button 1", "Button 2");
    }

    @Test
    void testSectionHeaderButtonsPosition_SameY() {
        driver.manage().window().setSize(new Dimension(1920, 1080));

        assertThat(button1.getLocation().getY())
                .describedAs("Button 1 and Button 2 should have the same y position")
                .isEqualTo(button2.getLocation().getY());
    }

    @Test
    void testSectionHeaderButtonsPosition_Order() {
        driver.manage().window().setSize(new Dimension(1920, 1080));

        assertThat(button1.getLocation().getX())
                .describedAs("Button 1 should be left of Button 2")
                .isLessThan(button2.getLocation().getX());
    }

    @Test
    void testLableChangeOnButtonClick() {
        assertThat(label.getText()).isEqualTo("none");

        button1.click();
        assertThat(label.getText()).isEqualTo("Button 1");

        button2.click();
        assertThat(label.getText()).isEqualTo("Button 2");
    }

}
