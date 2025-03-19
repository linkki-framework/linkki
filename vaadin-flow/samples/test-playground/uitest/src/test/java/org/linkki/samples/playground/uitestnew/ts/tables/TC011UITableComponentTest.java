package org.linkki.samples.playground.uitestnew.ts.tables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.UITestConfiguration;
import org.linkki.testbench.pageobjects.LinkkiGridElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;
import com.vaadin.flow.component.html.testbench.DivElement;

@UITestConfiguration(locale = "en")
class TC011UITableComponentTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS012, TestScenarioView.TC011);
    }

    @Test
    void testPlaceholder() {
        var tableId = "rows";
        var nestedComponent = $(DivElement.class).id("fixHeightTable");
        var table = nestedComponent.$(LinkkiGridElement.class).id(tableId);

        assertThat(table.$("table").get(0).getSize().getHeight()).isEqualTo(0);
        assertThat(table.getPlaceholderText())
                .as("Don't show placeholder during initial loading")
                .isEmpty();
        assertThat(table.getWrappedElement().getSize().getHeight())
                .as("Loading indicator is shown").isGreaterThan(0);

        waitUntil(d -> table.getRowCount() > 0, 10);
        assertThat(table.getPlaceholderText()).isEmpty();

        nestedComponent.$(ButtonElement.class).id("addPerson").click();

        assertThat(table.getPlaceholderText())
                .as("Don't show placeholder if the table has items")
                .isEmpty();
        waitUntil(d -> table.getRowCount() > 1, 10);

        table.$(ButtonElement.class).all().stream()
                .filter(b -> "remove".equals(b.getDomAttribute("id")))
                .forEach(ButtonElement::click);
        waitUntil(d -> table.getRowCount() == 0, 10);

        assertThat(table.getPlaceholderText())
                .as("Show placeholder if no items are present after loading")
                .isPresent()
                .hasValue("There are no person to be shown. Add a new person by clicking on the header button.");

        nestedComponent.$(CheckboxElement.class).id("exception").setChecked(true);
        nestedComponent.$(ButtonElement.class).id("reload").click();
        waitUntil(d -> !table.getPlaceholderText().orElse("").isBlank(), 10);
        assertThat(table.getPlaceholderText())
                .as("Shows error placeholder if exception occurred during getItems")
                .hasValue("An error occurred when retrieving table content.");
    }

}
