package org.linkki.samples.playground.bugs.uitest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.bugs.lin4104.TestCustomFieldLayout;
import org.linkki.samples.playground.uitest.AbstractLinkkiUiTest;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.customfield.testbench.CustomFieldElement;
import com.vaadin.flow.component.html.testbench.SpanElement;

/**
 * LIN-4104
 */
class CustomFieldSetValueTest extends AbstractLinkkiUiTest {

    @Test
    void test() {
        goToView(BugCollectionView.ROUTE);
        openTab(TestCustomFieldLayout.CAPTION);
        assertThat($(SpanElement.class).id("custom-field-vaadin-value").getText())
                .doesNotContain("2");

        $(CustomFieldElement.class).id("custom-field-vaadin")
                .$(ComboBoxElement.class).single()
                .selectByText("2");

        assertThat($(SpanElement.class).id("custom-field-vaadin-value").getText())
                .as("Setting a value in a not required custom field should set the value on the server side")
                .contains("2");

        assertThat($(LinkkiTextElement.class).id("valueLabel").getText())
                .doesNotContain("2");

        $(CustomFieldElement.class).id("value")
                .$(ComboBoxElement.class).single()
                .selectByText("2");

        assertThat($(LinkkiTextElement.class).id("valueLabel").getText())
                .as("Setting a value in a not required custom field in a PMO should set the value on the server side")
                .contains("2");
    }
}
