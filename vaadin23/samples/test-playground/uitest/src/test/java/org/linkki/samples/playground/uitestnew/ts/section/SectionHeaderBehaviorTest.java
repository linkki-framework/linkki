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

package org.linkki.samples.playground.uitestnew.ts.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.pageobjects.LinkkiSectionElement;
import org.linkki.samples.playground.ts.section.ClosableSectionPmo;
import org.linkki.samples.playground.ts.section.NotClosableSectionPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

class SectionHeaderBehaviorTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(PlaygroundApplicationView.TS002, PlaygroundApplicationView.TC004);
    }

    @Test
    void testCloseToggle() {
        assertThat(getSection(ClosableSectionPmo.class).getCloseToggle().exists()).isTrue();
        assertThat(getSection(NotClosableSectionPmo.class).getCloseToggle().exists()).isFalse();
    }

    @Test
    void testEmptyHeaderShouldTakeNoSpace() {
        LinkkiSectionElement section = getSection(NotClosableSectionPmo.class);
        DivElement content = section.getContent();

        content.$(TextFieldElement.class).id("caption").setValue("");
        content.$(DivElement.class).id("button1").$(CheckboxElement.class).id("visible").setChecked(false);
        content.$(DivElement.class).id("button2").$(CheckboxElement.class).id("visible").setChecked(false);

        assertThat(section.getSize().getHeight()).isEqualTo(content.getSize().getHeight());
    }

    @Test
    void testHeaderHeightShouldNotChange_WhenAllButtonsAreHidden() {
        LinkkiSectionElement section = getSection(NotClosableSectionPmo.class);
        DivElement content = section.getContent();

        content.$(TextFieldElement.class).id("caption").setValue("FooBaa");
        content.$(DivElement.class).id("button1").$(CheckboxElement.class).id("visible").setChecked(true);
        content.$(DivElement.class).id("button2").$(CheckboxElement.class).id("visible").setChecked(true);

        int size = section.getHeader().getSize().getHeight();

        content.$(DivElement.class).id("button1").$(CheckboxElement.class).id("visible").setChecked(false);
        content.$(DivElement.class).id("button2").$(CheckboxElement.class).id("visible").setChecked(false);

        assertThat(section.getHeader().getSize().getHeight()).isEqualTo(size);
    }

    @Test
    void testHeaderHeightShouldNotChange_WhenCaptionIsHidden() {
        LinkkiSectionElement section = getSection(NotClosableSectionPmo.class);
        DivElement content = section.getContent();

        content.$(TextFieldElement.class).id("caption").setValue("Foobaa");
        content.$(DivElement.class).id("button1").$(CheckboxElement.class).id("visible").setChecked(true);
        content.$(DivElement.class).id("button2").$(CheckboxElement.class).id("visible").setChecked(true);

        int size = section.getHeader().getSize().getHeight();

        content.$(TextFieldElement.class).id("caption").setValue("");

        assertThat(section.getHeader().getSize().getHeight()).isEqualTo(size);
    }

    @Test
    void testAddHeaderButtonAndAddHeaderComponent() {
        List<TestBenchElement> headerComponents = getSection(ClosableSectionPmo.class).getHeaderComponents().all();

        assertThat(headerComponents.get(0).getTagName()).isEqualTo("h4");
        assertThat(headerComponents.get(1).getText()).isEqualTo("Button 2");
        assertThat(headerComponents.get(2).getText()).isEqualTo("Button 1");
        assertThat(headerComponents.get(3).getTagName()).isEqualTo("vaadin-combo-box");
        assertThat(headerComponents.get(4).getTagName()).isEqualTo("vaadin-checkbox");
    }

}
