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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.bugs.lin4805.NestedTabLayoutBug;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;

import com.vaadin.flow.component.tabs.testbench.TabElement;

/**
 * LIN-4805: Horizontal tabs nested inside a vertical LinkkiTabLayout should not receive the padding
 * rule intended only for vertical tabs.
 */
class NestedTabLayoutTest extends AbstractLinkkiUiTest {

    @Test
    void testNestedHorizontalTab_hasNoPaddingFromOuterVerticalRule() {
        goToView(BugCollectionView.ROUTE);
        openTab(NestedTabLayoutBug.LIN_4805);

        TabElement verticalTab = $(TabElement.class).id(NestedTabLayoutBug.LIN_4805);
        TabElement nestedTab = $(TabElement.class).id(NestedTabLayoutBug.NESTED_TAB_ID);

        assertThat(verticalTab.getCssValue("padding")).isEqualTo("8px 16px");
        assertThat(nestedTab.getCssValue("padding")).isEqualTo("8px 12px");
    }
}
