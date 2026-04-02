package org.linkki.samples.playground.uitest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.linkki.samples.playground.ts.TestScenarioView.TC008;
import static org.linkki.samples.playground.ts.TestScenarioView.TS011;
import static org.linkki.testbench.conditions.VaadinElementConditions.allDialogsClosed;
import static org.linkki.testbench.conditions.VaadinElementConditions.dialogClosed;
import static org.linkki.testbench.pageobjects.OkCancelDialogElement.hasCaption;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.bugs.lin4780.OverlappingDialogHeadersBug;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.OkCancelDialogElement;
import org.openqa.selenium.NoSuchElementException;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;

class OkCancelElementTest extends PlaygroundUiTest {

    @Test
    void testDialogOverDialog() {
        goToView(BugCollectionView.ROUTE);
        openTab(OverlappingDialogHeadersBug.LIN_4780);
        $(ButtonElement.class).withCaption("Open OkCancelDialog").single().click();
        var firstDialog = $(OkCancelDialogElement.class).withCondition(hasCaption("Caption")).single();

        firstDialog.$(ButtonElement.class).withCaption("Open nested OkCancelDialog").single().click();

        assertThat($(OkCancelDialogElement.class).all()).hasSize(2);
        assertThatNoException().isThrownBy(firstDialog::getOkButton);
        assertThatNoException().isThrownBy(firstDialog::getCancelButton);
        assertThatNoException().isThrownBy(firstDialog::getDialogLayout);
        assertThatNoException().isThrownBy(firstDialog::getContentArea);
        assertThatNoException().isThrownBy(firstDialog::getSize);

        var nestedDialog = $(OkCancelDialogElement.class).withCondition(hasCaption("Nested OkCancelDialog")).single();
        nestedDialog.clickOnOk();

        assertThatNoException().isThrownBy(() -> waitUntil(dialogClosed("Nested OkCancelDialog")));

        $(OkCancelDialogElement.class).single().clickOnCancel();
        waitUntil(allDialogsClosed());
    }

    @Test
    void testMessageArea() {
        var testCase = goToTestCase(TS011, TC008);

        testCase.$(ButtonElement.class).withCaption("Open dialog").single().click();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> $(OkCancelDialogElement.class).single().getMessageArea());

        $(ComboBoxElement.class).id("severity").selectByText("INFO");

        var messageArea = $(OkCancelDialogElement.class).single().getMessageArea();
        assertThat(messageArea.getText()).contains("Generic info validation message on all fields");
    }
}
