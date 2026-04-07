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
import org.linkki.samples.playground.bugs.lin4803.DialogMinWidthBug;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;

/**
 * LIN-4803: Dialog overlay should have a min-width of 400px.
 */
class DialogMinWidthTest extends AbstractLinkkiUiTest {

    @Test
    void testDialogHasMinWidth() {
        goToView(BugCollectionView.ROUTE);
        openTab(DialogMinWidthBug.LIN_4803);

        $(ButtonElement.class).withCaption("Open Dialog").first().click();

        OkCancelDialogElement dialog = $(OkCancelDialogElement.class).waitForFirst();
        assertThat(dialog.getSize().getWidth()).isGreaterThanOrEqualTo(400);
    }
}
