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

package org.linkki.samples.playground.application.uitest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.linkki.samples.playground.uitest.DriverProperties;

import com.vaadin.flow.component.notification.testbench.NotificationElement;

public class ApplicationMenuItemDefinitionTest extends AbstractUiTest {

    @BeforeEach
    void setup() {
        getDriver().get(DriverProperties.getTestUrl(""));
        clickMenuItem("Custom Layout");
    }

    @Test
    void testApplicationMenuItem_ExternalLink() {
        clickMenuItem("Click Handler Constructor Variants");
        clickContextMenuItem("External Link");

        Assertions.assertThat(getDriver().getCurrentUrl()).isEqualTo("https://www.linkki-framework.org/");
    }

    @Test
    void testApplicationMenuItem_RelativeLink() {
        String applicationSampleUrl = getDriver().getCurrentUrl();
        clickMenuItem("Click Handler Constructor Variants");
        clickContextMenuItem("Relative Link to Bugs");

        Assertions.assertThat(getDriver().getCurrentUrl())
                .isEqualTo(applicationSampleUrl.replace("custom-layout", "bugs"));
    }

    @Test
    void testApplicationMenuItem_NavigateToClass() {
        String applicationSampleUrl = getDriver().getCurrentUrl();
        clickMenuItem("Click Handler Constructor Variants");
        clickContextMenuItem("Route Class to Bugs");

        Assertions.assertThat(getDriver().getCurrentUrl())
                .isEqualTo(applicationSampleUrl.replace("custom-layout", "bugs"));
    }

    @Test
    void testApplicationMenuItem_Handler() {
        clickMenuItem("Click Handler Constructor Variants");
        clickContextMenuItem("Handler");

        Assertions.assertThat($(NotificationElement.class).exists()).isTrue();
    }

}
