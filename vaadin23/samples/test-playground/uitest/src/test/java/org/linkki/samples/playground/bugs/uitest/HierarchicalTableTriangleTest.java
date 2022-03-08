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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.bugs.lin1917.TriangleTablePmo;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.grid.testbench.TreeGridElement;

/**
 * LIN-1890
 */
public class HierarchicalTableTriangleTest extends AbstractUiTest {

    @Test
    public void testTriangleIsShownAfterChildIsAdded() {
        goToView(BugCollectionView.ROUTE);
        openTab(TriangleTablePmo.CAPTION);

        TreeGridElement gridElement = $(TreeGridElement.class).id("TriangleTablePmo_table");

        assertThat(hasToggle(gridElement.getExpandToggleElement(0, 0)), is(false));

        gridElement.getCell(0, 0).click();
        clickSectionHeaderButton(TriangleTablePmo.class, 0);

        assertThat(hasToggle(gridElement.getExpandToggleElement(0, 0)), is(true));
    }

    private boolean hasToggle(WebElement element) {
        // vaadin-grid-tree-toggle with no toggle button have a leaf attribute
        return !Boolean.valueOf(element.getAttribute("leaf"));
    }
}
