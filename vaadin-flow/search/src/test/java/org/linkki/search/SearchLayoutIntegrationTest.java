package org.linkki.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.ComponentConditions.anyVisibleChildOfType;
import static org.linkki.core.ui.test.ComponentConditions.exactlyOneVisibleChildOfType;
import static org.linkki.core.ui.test.KaribuUtils.getWithId;
import static org.linkki.core.ui.test.KaribuUtils.Fields.setValue;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.test.ComponentConditions;
import org.linkki.core.ui.test.ComponentTreeRepresentation;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils.Grids;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.component.Headline;
import org.linkki.search.component.SearchInputLayout;
import org.linkki.search.component.SearchResultLayout;
import org.linkki.search.model.SimpleSearchController;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.TextField;

@ExtendWith(KaribuUIExtension.class)
class SearchLayoutIntegrationTest {

    private static final String EMPTY_SEARCH_ENTRY = "EMPTY";
    private static final String SEARCH_ENTRY = "SEARCH_NAME";

    /**
     * A search layout is created using {@link SearchLayoutBuilder} together with a
     * {@link SimpleSearchController}. The test checks the initial, post-search and post-reset state
     * of the UI elements.
     */
    @Test
    void testInitialLayout() {
        var searchController = new SimpleSearchController<>(
                TestSearchParameters::new,
                // initially no results should be shown. The search function returning the empty
                // entry should not be called in this test
                s -> new TestSearchResult(List.of(new TestModelObject(EMPTY_SEARCH_ENTRY)),
                        new MessageList(Message.newError("code", "error message text"))),
                TestSearchResult::messages);

        var searchLayoutPmo = SearchLayoutBuilder
                .<TestSearchParameters, TestSearchResult, TestModelObject, TestSearchResultRowPmo> with()
                .searchParametersPmo(TestSearchParametersPmo::new)
                .searchResultRowPmo(TestSearchResultRowPmo::new,
                                    TestSearchResultRowPmo::modelObject,
                                    TestSearchResultRowPmo.class)
                .searchController(searchController, TestSearchResult::result)
                .primaryAction(p -> searchController.search())
                .caption("Caption")
                .build();
        var bindingContext = new BindingContext();
        var layout = VaadinUiCreator.createComponent(searchLayoutPmo, bindingContext);

        assertThat(layout)
                .withRepresentation(new ComponentTreeRepresentation())
                .as("The overall layout should be created correctly")
                .has(ComponentConditions.exactlyOneVisibleChildOfType(Headline.class))
                .has(exactlyOneVisibleChildOfType(H2.class, spec -> spec.withText("Caption")))
                .has(ComponentConditions.exactlyOneVisibleChildOfType(SearchInputLayout.class))
                .has(ComponentConditions.exactlyOneVisibleChildOfType(SearchResultLayout.class))
                .as("An input prompt should be displayed")
                .has(exactlyOneVisibleChildOfType(LinkkiText.class,
                                                  spec -> spec.withText("Enter search criteria and start search")))
                .as("There should be no error messages")
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withText("error message text")))
                .as("Result count should not be displayed")
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withText("No results were found")))
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withId("searchResultCount")))
                .as("The table of results should not be displayed")
                .doesNotHave(anyVisibleChildOfType(Grid.class, spec -> spec.withId("SearchResultTablePmo")));
    }

    @Test
    void testSearch() {
        var searchController = new SimpleSearchController<>(
                TestSearchParameters::new,
                s -> new TestSearchResult(
                        Objects.equals(SEARCH_ENTRY, s.name)
                                ? List.of(new TestModelObject(s.name + "1"), new TestModelObject(s.name + "2"))
                                : List.of(new TestModelObject(s.name + "3"), new TestModelObject(s.name + "4"),
                                          new TestModelObject(s.name + "5")),
                        new MessageList(
                                Message.newInfo("info", "info message"),
                                Message.newWarning("error", "error message"))),
                TestSearchResult::messages);

        var searchLayoutPmo = SearchLayoutBuilder
                .<TestSearchParameters, TestSearchResult, TestModelObject, TestSearchResultRowPmo> with()
                .searchParametersPmo(TestSearchParametersPmo::new)
                .searchResultRowPmo(TestSearchResultRowPmo::new,
                                    TestSearchResultRowPmo::modelObject,
                                    TestSearchResultRowPmo.class)
                .searchController(searchController, TestSearchResult::result)
                .primaryAction(o -> searchController.search())
                .maxResult(2)
                .build();
        var bindingContext = new BindingContext();
        var layout = VaadinUiCreator.createComponent(searchLayoutPmo, bindingContext);

        var nameField = getWithId(layout, TextField.class, "name");
        setValue(nameField, SEARCH_ENTRY);
        var searchButton = getWithId(layout, Button.class, "search");
        searchButton.click();

        assertThat(layout)
                .withRepresentation(new ComponentTreeRepresentation())
                .as("The input prompt should not be displayed")
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withText("Enter search criteria and start search")))
                .as("Message with the highest severity should be displayed")
                .has(exactlyOneVisibleChildOfType(LinkkiText.class,
                                                  spec -> spec.withText("error message")))
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withText("info message")))
                .as("Result count should be displayed")
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withText("No results were found")))
                .has(exactlyOneVisibleChildOfType(LinkkiText.class,
                                                  spec -> spec.withText("2 results were found.")))
                .as("The table of results should be displayed")
                .has(exactlyOneVisibleChildOfType(Grid.class, spec -> spec.withId("SearchResultTablePmo")));
        var resultTable = Grids.get(layout);
        assertThat(Grids.getTextContentsInColumn(resultTable, "column"))
                .as("Results should be displayed using the given PMO")
                .containsExactly(SEARCH_ENTRY + "1 suffix",
                                 SEARCH_ENTRY + "2 suffix");

        // Sets the value that leads to 3 results, but only maxResult of 2 should be displayed
        setValue(nameField, "something else-");
        searchButton.click();

        assertThat(layout)
                .withRepresentation(new ComponentTreeRepresentation())
                .as("Result count should be displayed")
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withText("No results were found")))
                .has(exactlyOneVisibleChildOfType(LinkkiText.class,
                                                  spec -> spec.withText("Only the first 2 results are displayed.")))
                .as("The table of results should be displayed")
                .has(exactlyOneVisibleChildOfType(Grid.class, spec -> spec.withId("SearchResultTablePmo")));

        assertThat(Grids.getTextContentsInColumn(resultTable, "column"))
                .as("Only the first two results should be displayed in the table")
                .containsExactly("something else-3 suffix", "something else-4 suffix");
    }

    @Test
    void testSearch_NoResult() {
        var searchController = new SimpleSearchController<>(
                TestSearchParameters::new,
                s -> new TestSearchResult(List.of(),
                        // This is mandatory by design for the no result text
                        new MessageList(Message.newInfo("noResult", "No result found"))),
                TestSearchResult::messages);
        var searchLayoutPmo = SearchLayoutBuilder
                .<TestSearchParameters, TestSearchResult, TestModelObject, TestSearchResultRowPmo> with()
                .searchParametersPmo(TestSearchParametersPmo::new)
                .searchResultRowPmo(TestSearchResultRowPmo::new,
                                    TestSearchResultRowPmo::modelObject,
                                    TestSearchResultRowPmo.class)
                .searchController(searchController, TestSearchResult::result)
                .primaryAction(o -> searchController.search())
                .build();
        var bindingContext = new BindingContext();
        var layout = VaadinUiCreator.createComponent(searchLayoutPmo, bindingContext);

        var nameField = getWithId(layout, TextField.class, "name");
        setValue(nameField, SEARCH_ENTRY);
        var searchButton = getWithId(layout, Button.class, "search");
        searchButton.click();

        assertThat(layout)
                .withRepresentation(new ComponentTreeRepresentation())
                .has(exactlyOneVisibleChildOfType(LinkkiText.class,
                                                  spec -> spec.withText("No results were found.")))
                .doesNotHave(anyVisibleChildOfType(Component.class,
                                                   spec -> spec.withId("searchResultCount")))
                .as("The table of results should not be displayed")
                .doesNotHave(anyVisibleChildOfType(Grid.class, spec -> spec.withId("SearchResultTablePmo")));
    }

    @Test
    void testReset() {
        var searchController = new SimpleSearchController<>(
                TestSearchParameters::new,
                s -> new TestSearchResult(List.of(new TestModelObject(s.name)), new MessageList()),
                TestSearchResult::messages);
        var searchLayoutPmo = SearchLayoutBuilder
                .<TestSearchParameters, TestSearchResult, TestModelObject, TestSearchResultRowPmo> with()
                .searchParametersPmo(TestSearchParametersPmo::new)
                .searchResultRowPmo(TestSearchResultRowPmo::new,
                                    TestSearchResultRowPmo::modelObject,
                                    TestSearchResultRowPmo.class)
                .searchController(searchController, TestSearchResult::result)
                .primaryAction(o -> searchController.search())
                .build();
        var bindingContext = new BindingContext();
        var layout = VaadinUiCreator.createComponent(searchLayoutPmo, bindingContext);

        var nameField = getWithId(layout, TextField.class, "name");
        setValue(nameField, SEARCH_ENTRY);
        var searchButton = getWithId(layout, Button.class, "search");
        searchButton.click();
        assertThat(layout)
                .withRepresentation(new ComponentTreeRepresentation())
                .as("First search should display the grid")
                .has(exactlyOneVisibleChildOfType(Grid.class, spec -> spec.withId("SearchResultTablePmo")));

        var resetButton = getWithId(layout, Button.class, "reset");
        resetButton.click();

        assertThat(nameField.getValue())
                .as("The input field should be empty after reset")
                .isEmpty();
        assertThat(layout)
                .withRepresentation(new ComponentTreeRepresentation())
                .as("The input prompt should be displayed after reset")
                .has(anyVisibleChildOfType(Component.class,
                                           spec -> spec.withText("Enter search criteria and start search")))
                .as("The result table should not be displayed after reset")
                .doesNotHave(anyVisibleChildOfType(Grid.class, spec -> spec.withId("SearchResultTablePmo")));
    }

    public static class TestSearchParameters {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestSearchParametersPmo {

        private final Supplier<TestSearchParameters> searchParameters;

        @ModelObject
        public TestSearchParameters getSearchParameters() {
            return searchParameters.get();
        }

        public TestSearchParametersPmo(Supplier<TestSearchParameters> searchParameters) {
            this.searchParameters = searchParameters;
        }

        @UITextField(position = 10, label = "Name",
                modelAttribute = "name")
        public void name() {
            // model binding
        }

    }

    public record TestSearchResultRowPmo(TestModelObject modelObject) {

        @UILabel(position = 0)
        public String getColumn() {
            return modelObject.getName() + " suffix";
        }
    }

    public static class TestModelObject {

        private String name;

        public TestModelObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "TestModelObject{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public record TestSearchResult(List<TestModelObject> result, MessageList messages) {

    }
}
