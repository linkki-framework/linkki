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

package org.linkki.samples.playground.bugs.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.bugs.lin2200.ComboBoxNewInstancePmo;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import com.vaadin.flow.component.button.testbench.ButtonElement;

/**
 * Test for {@link ComboBoxNewInstancePmo}.
 */
public class ComboBoxNewInstanceTest extends AbstractLinkkiUiTest {

    @Test
    public void testValueOnResettingListItemValues() throws InterruptedException {
        goToView(BugCollectionView.ROUTE);
        openTab(ComboBoxNewInstancePmo.CAPTION);

        LinkkiSectionElement section = getSection(ComboBoxNewInstancePmo.class);
        section.$(ButtonElement.class).id("changeChoicesValues").click();
        resetLogEntries();

        Thread.sleep(2500);
        assertThat(getRequestCount(), is(lessThan(5)));
    }

    private void resetLogEntries() {
        getDriver().manage().logs().get(LogType.PERFORMANCE);
    }

    private int getRequestCount() {
        List<LogEntry> entries = getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
        return (int)entries.stream()
                .filter(e -> e.getMessage().contains("\"method\":\"Network.requestWillBeSent\""))
                .count();
    }
}
