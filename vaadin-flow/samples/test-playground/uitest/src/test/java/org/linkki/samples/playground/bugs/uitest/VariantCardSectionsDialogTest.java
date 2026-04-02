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
import org.linkki.samples.playground.bugs.lin4808.VariantCardSectionsDialogBug;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.linkki.testbench.pageobjects.LinkkiSectionElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;

/**
 * LIN-4808: Card styling does not work in dialogs after the migration to Vaadin 25.
 */
class VariantCardSectionsDialogTest extends AbstractLinkkiUiTest {

    private static final String TRANSPARENT = "rgba(0, 0, 0, 0)";

    @Test
    void testBindVariantNames_DirectCardSectionAppliedInDialog() {
        goToView(BugCollectionView.ROUTE);
        openTab(VariantCardSectionsDialogBug.LIN_4808);

        $(ButtonElement.class).withCaption(VariantCardSectionsDialogBug.BUTTON_CAPTION_OPEN_DIALOG_PMO).first().click();

        DialogElement dialog = findDialog(VariantCardSectionsDialogBug.DIALOG_CAPTION_PMO);

        var sections = dialog.$(LinkkiSectionElement.class).all();
        assertThat(sections).hasSize(1);
        assertThat(sections.getFirst().getCssValue("background")).isNotEqualTo(TRANSPARENT);
    }

    @Test
    void testBindVariantNames_NestedCardSectionAppliedInDialog() {
        goToView(BugCollectionView.ROUTE);
        openTab(VariantCardSectionsDialogBug.LIN_4808);

        $(ButtonElement.class).withCaption(VariantCardSectionsDialogBug.BUTTON_CAPTION_OPEN_DIALOG_NESTED_PMOS).first()
                .click();

        DialogElement dialog = findDialog(VariantCardSectionsDialogBug.DIALOG_CAPTION_NESTED_PMOS);

        var sections = dialog.$(LinkkiSectionElement.class).all();
        assertThat(sections).hasSize(3);
        assertThat(sections.getLast().getCssValue("background")).isNotEqualTo(TRANSPARENT);
    }
}
