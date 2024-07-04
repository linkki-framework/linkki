package org.linkki.samples.playground.uitestnew.ts.tables;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.samples.playground.uitestnew.PlaygroundUiTest;
import org.linkki.testbench.pageobjects.LinkkiGridElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.checkbox.testbench.CheckboxElement;

class TC011UITableComponentTest extends PlaygroundUiTest {

    @BeforeEach
    void goToTestCase() {
        goToTestCase(TestScenarioView.TS012, TestScenarioView.TC011);
    }

    @Test
    void testPlaceholder() {
        var tableId = "rows";
        var table = $(LinkkiGridElement.class).id(tableId);

        assertThat(table.$("table").get(0).getSize().getHeight()).isEqualTo(0);
        assertThat(table.getPlaceholderText(tableId))
                .as("Shows loading placeholder during initial loading")
                .hasValue("Loading...");

        waitUntil(d -> table.getRowCount() > 0, 10);
        assertThat(table.getPlaceholderText(tableId)).isEmpty();

        $(ButtonElement.class).id("addPerson").click();

        assertThat(table.getPlaceholderText(tableId))
                .as("Shows no loading placeholder if the table has items")
                .isEmpty();
        waitUntil(d -> table.getRowCount() > 1, 10);

        table.$(ButtonElement.class).all().stream()
                .filter(b -> "remove".equals(b.getAttribute("id")))
                .forEach(ButtonElement::click);
        waitUntil(d -> table.getRowCount() == 0, 10);

        assertThat(table.getPlaceholderText(tableId))
                .as("Shows really placeholder if no items are present after loading")
                .isPresent()
                .hasValueSatisfying(s -> assertThat(s).isNotEqualTo("Loading..."));

        $(CheckboxElement.class).id("exception").setChecked(true);
        waitUntil(d -> !table.getPlaceholderText(tableId).orElse("").equals("Loading..."), 2);
        assertThat(table.getPlaceholderText(tableId))
                .as("Shows error placeholder if exception occurred during getItems")
                .hasValue("Tabelleninhalt konnte nicht ermittelt werden.");
    }

}
