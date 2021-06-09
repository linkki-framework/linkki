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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.AllUiElementsUiSectionPmo;
import org.linkki.samples.playground.uitest.extensions.DriverExtension.Configuration;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;

class UIDateFieldTest {

    @Nested
    @Configuration(locale = "de")
    class UIDateFieldTestDe extends BaseUIDateFieldTest {
        UIDateFieldTestDe() {
            super("Der eingegebene Datum ist ungültig.");
        }
    }

    @Nested
    @Configuration(locale = "en")
    class UIDateFieldTestEn extends BaseUIDateFieldTest {
        UIDateFieldTestEn() {
            super("The input date is invalid.");
        }
    }

    private abstract class BaseUIDateFieldTest extends AbstractUiTest {

        private String expectedInvalidInputMessage;

        BaseUIDateFieldTest(String expectedInvalidFormatMessage) {
            this.expectedInvalidInputMessage = expectedInvalidFormatMessage;
        }

        @Test
        public void testInvalidInputMessage() {
            DateFieldElement dateFieldElement = $(VerticalLayoutElement.class)
                    .id(AllUiElementsUiSectionPmo.class.getSimpleName()).$(DateFieldElement.class).first();
            dateFieldElement.setValue("awefaüdfkj");
            // first time doesn't trigger error input?!
            dateFieldElement.setValue("awefawef");

            dateFieldElement.showTooltip();


            WebElement tooltip = findElement(By.className("v-tooltip"));
            assertThat(tooltip.getText(), is(expectedInvalidInputMessage));
        }

    }
}
