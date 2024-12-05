package org.linkki.samples.playground.uitestnew.ts.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.SpanElement;

public class ErrorDialogTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS017, TestScenarioView.TC001);
    }

    @Test
    void testErrorDialog_ProductionMode() {

        $(ButtonElement.class).id("showExceptionDialogWithDefaultConfiguration").click();

        var errorDialog = findDialog("Unerwarteter Fehler");
        assertThat(errorDialog.$(SpanElement.class).get(0).getText())
                .isEqualTo("Es ist ein Fehler aufgetreten.");
        assertThat(errorDialog.$(SpanElement.class).get(1).getText())
                .matches("Timestamp: .* \\[.*]");
        assertNoExceptionMessageVisibleInProductionMode(getDriver(), errorDialog);
        assertNoStacktraceVisibleInProductionMode(getDriver(), errorDialog);

        $(ButtonElement.class).get(0).click();
        assertThat(getDriver().getCurrentUrl()).endsWith(DEFAULT_CONTEXT_PATH + "/");
    }

    private void assertNoExceptionMessageVisibleInProductionMode(WebDriver driver, DialogElement dialog) {
        assertThat(driver.getPageSource())
                .doesNotContain("This message should not be shown in production mode");
        dialog.getContext().findElements(By.tagName("input"))
                .forEach(element -> assertThat(element.getAttribute("value"))
                        .doesNotContain("This message should not be shown in production mode"));
    }

    private void assertNoStacktraceVisibleInProductionMode(WebDriver driver, DialogElement dialog) {
        assertThat(driver.getPageSource())
                .doesNotContain("java.lang.IllegalStateException: Error calling invoke method");
        dialog.getContext().findElements(By.tagName("textarea"))
                .forEach(element -> assertThat(element.getAttribute("value"))
                        .doesNotContain("java.lang.IllegalStateException: Error calling invoke method"));
    }

}
