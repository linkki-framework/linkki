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
import org.linkki.samples.playground.bugs.lin4798.LongLabelPmo;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;

import com.vaadin.flow.component.formlayout.testbench.FormItemElement;
import com.vaadin.flow.component.html.testbench.NativeLabelElement;

/**
 * LIN-4798: Long labels in form items should be truncated instead of wrapping into multiple lines.
 */
class LongLabelTest extends AbstractLinkkiUiTest {

    @Test
    void testLongLabel_IsNotWrapped() {
        goToView(BugCollectionView.ROUTE);
        openTab(LongLabelPmo.LIN_4798);

        var section = getSection(LongLabelPmo.class);
        var formItem = section.getContent().$(FormItemElement.class).last();
        var label = formItem.$(NativeLabelElement.class).withTextContaining("TextField").first();

        int labelHeight = label.getSize().getHeight();

        assertThat(labelHeight).isLessThanOrEqualTo(40);
    }
}
