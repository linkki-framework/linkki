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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.samples.playground.pageobjects.TestCaseComponentElement;
import org.linkki.samples.playground.ts.section.UiSectionLayoutFormComponentsPmo;
import org.linkki.samples.playground.ts.section.UiSectionLayoutHorizontalComponentsPmo;
import org.linkki.samples.playground.ts.section.UiSectionLayoutVerticalComponentsPmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;

import com.vaadin.testbench.TestBenchElement;

public class SectionLayoutComponentWidthTest {

    private static final String INTEGER_FIELD_WITH_DEFAULT_WIDTH = "integerFieldWithDefaultWidth";
    private static final String DOUBLE_FIELD_WITH_DEFAULT_WIDTH = "doubleFieldWithDefaultWidth";
    private static final String TEXT_AREA_WITH_DEFAULT_WIDTH = "textAreaWithDefaultWidth";
    private static final String TEXT_FIELD_WITH_DEFAULT_WIDTH = "textFieldWithDefaultWidth";
    private static final String INTEGER_FIELD_WITH_CUSTOM_WIDTH = "integerFieldWithCustomWidth";
    private static final String DOUBLE_FIELD_WITH_CUSTOM_WIDTH = "doubleFieldWithCustomWidth";
    private static final String TEXT_AREA_WITH_CUSTOM_WIDTH = "textAreaWithCustomWidth";
    private static final String TEXT_FIELD_WITH_CUSTOM_WIDTH = "textFieldWithCustomWidth";

    abstract class AbstractBaseSectionComponentWidthTest extends PlaygroundUiTest {
        private TestCaseComponentElement testCaseSection;

        @BeforeEach
        void setup() {
            super.setUp();
            testCaseSection = goToTestCase(PlaygroundApplicationView.TS002, PlaygroundApplicationView.TC003);
        }

        protected TestCaseComponentElement getTestCaseSection() {
            return testCaseSection;
        }

        protected abstract Class<?> getSectionClassPmo();

        protected Double getComponentWidth(String id) {
            return Double.valueOf(getSection(getSectionClassPmo()).$(TestBenchElement.class).id(id).getCssValue("width")
                    .replaceAll("px", ""));
        }

        /**
         * All elements do have the same (custom) width set
         */
        @Test
        void testComponents_CustomWidth() {
            Double textFieldWidth = getComponentWidth(TEXT_FIELD_WITH_CUSTOM_WIDTH);
            Double textAreaWidth = getComponentWidth(TEXT_AREA_WITH_CUSTOM_WIDTH);
            Double integerFieldWidth = getComponentWidth(INTEGER_FIELD_WITH_CUSTOM_WIDTH);
            Double doubleFieldWidth = getComponentWidth(DOUBLE_FIELD_WITH_CUSTOM_WIDTH);

            assertThat(textAreaWidth).isEqualTo(textFieldWidth).isEqualTo(integerFieldWidth)
                    .isEqualTo(doubleFieldWidth);
        }
    }

    abstract class AbstractSectionComponentWidthTest extends AbstractBaseSectionComponentWidthTest {

        /**
         * <ul>
         * <li>{@link UITextField} and {@link UITextArea} do have the same default width of 100%</li>
         * <li>{@link UIIntegerField} and {@link UIDoubleField} do have the same default width of
         * 100%</li>
         * <li>{@link UIIntegerField} and {@link UIDoubleField} do have a less default width than
         * {@link UITextField} and {@link UITextArea}</li>
         * </ul>
         */
        @Test
        void testComponents_DefaultWidth() {
            Double textFieldWidth = getComponentWidth(TEXT_FIELD_WITH_DEFAULT_WIDTH);
            Double textAreaWidth = getComponentWidth(TEXT_AREA_WITH_DEFAULT_WIDTH);
            Double integerFieldWidth = getComponentWidth(INTEGER_FIELD_WITH_DEFAULT_WIDTH);
            Double doubleFieldWidth = getComponentWidth(DOUBLE_FIELD_WITH_DEFAULT_WIDTH);

            assertThat(textAreaWidth).isEqualTo(textFieldWidth);
            assertThat(integerFieldWidth).isEqualTo(doubleFieldWidth).isLessThan(textFieldWidth);
        }

    }

    /**
     * {@link SectionLayout#FORM} and {@link SectionLayout#VERTICAL} have same component widths.
     */
    @Nested
    class UISectionFormComponentWidthTest extends AbstractSectionComponentWidthTest {

        @Override
        protected Class<?> getSectionClassPmo() {
            return UiSectionLayoutFormComponentsPmo.class;
        }

    }

    /**
     * {@link SectionLayout#VERTICAL} and {@link SectionLayout#FORM} have same component widths. See
     * {@link UISectionFormComponentWidthTest}
     */
    @Nested
    class UISectionVerticalComponentWidthTest extends AbstractSectionComponentWidthTest {

        @Override
        protected Class<?> getSectionClassPmo() {
            return UiSectionLayoutVerticalComponentsPmo.class;
        }

    }

    @Nested
    class UISectionHorizontalComponentWidthTest extends AbstractBaseSectionComponentWidthTest {

        @Override
        protected Class<?> getSectionClassPmo() {
            return UiSectionLayoutHorizontalComponentsPmo.class;
        }

        /**
         * All elements do have the same (default) width set
         */
        @Test
        void testComponents_DefaultWidth() {
            Double textFieldWidth = getComponentWidth(TEXT_FIELD_WITH_DEFAULT_WIDTH);
            Double textAreaWidth = getComponentWidth(TEXT_AREA_WITH_DEFAULT_WIDTH);
            Double integerFieldWidth = getComponentWidth(INTEGER_FIELD_WITH_DEFAULT_WIDTH);
            Double doubleFieldWidth = getComponentWidth(DOUBLE_FIELD_WITH_DEFAULT_WIDTH);

            assertThat(textAreaWidth).isEqualTo(textFieldWidth);
            assertThat(integerFieldWidth).isEqualTo(doubleFieldWidth).isLessThan(textFieldWidth);
        }

    }
}
