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

package org.linkki.samples.playground.bugs.uitest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.lin1797.OnlyTablePmo;
import org.linkki.samples.playground.bugs.lin1797.SectionTablePmo;
import org.linkki.samples.playground.ui.PlaygroundApplicationUI;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.By;


// LIN-1797
public class NoDuplicateTableCaptionTest extends AbstractUiTest {

    @Test
    public void testValueOnRefreshWithSameAvailbleValues() {
        openTab(PlaygroundApplicationUI.BUGS_TAB_ID);
        List<String> sectionCaptions = findElements(By.className("linkki-section-caption")).stream()
                .map(el -> el.findElement(By.tagName("h4")))
                .filter(t -> t.getText().contains(OnlyTablePmo.LIN_1797)).map(el -> el.getText())
                .collect(Collectors.toList());
        // TODO LIN-2088 which className to find tableCaptions?
        // List<String> tableCaptions = findElements(By.className("v-captiontext")).stream().map(e ->
        // e.getText())
        // .filter(t -> t.contains(OnlyTablePmo.LIN_1797)).collect(Collectors.toList());

        assertThat(sectionCaptions.contains(OnlyTablePmo.CAPTION), is(true));
        assertThat(sectionCaptions.contains(SectionTablePmo.CAPTION), is(true));
        // assertThat(tableCaptions, is(empty()));
    }
}
