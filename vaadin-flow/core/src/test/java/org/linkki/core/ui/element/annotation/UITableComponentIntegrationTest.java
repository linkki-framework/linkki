package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.test.TestLogAppender;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.shared.communication.PushMode;

@ExtendWith(KaribuUIExtension.class)
class UITableComponentIntegrationTest {

    private final BindingContext bindingContext = new BindingContext();
    private final TestLogAppender testLogAppender = new TestLogAppender();

    @BeforeEach
    void setupLogger() {
        testLogAppender.setContext((LoggerContext)LoggerFactory.getILoggerFactory());
        var gridItemsAspectDefinitionLogger = (Logger)LoggerFactory
                .getLogger(UITableComponent.GridItemsAspectDefinition.class);
        gridItemsAspectDefinitionLogger.setLevel(Level.DEBUG);
        gridItemsAspectDefinitionLogger.addAppender(testLogAppender);

        // Adds the logs from the error handler as exceptions during UI#access are logged by it.
        var errorHandlerLogger = (Logger)LoggerFactory
                .getLogger(UI.getCurrent().getSession().getErrorHandler().getClass());
        errorHandlerLogger.setLevel(Level.DEBUG);
        errorHandlerLogger.addAppender(testLogAppender);

        testLogAppender.start();
    }

    @Test
    void testCreateGrid() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = pmo.getClass().getMethod("getItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        var component = wrapper.getComponent();

        assertThat(component).isInstanceOf(Grid.class);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)component;
        assertThat(grid.getThemeNames())
                .as("Default theme names of GridComponentCreator should be set")
                .containsExactly(GridVariant.LUMO_WRAP_CELL_CONTENT.getVariantName(),
                                 GridVariant.LUMO_COMPACT.getVariantName(),
                                 GridVariant.LUMO_NO_BORDER.getVariantName());
        assertThat(grid.getColumns()).hasSize(2);
        var defaultColumn = grid.getColumns().get(0);
        assertThat(defaultColumn.getWidth()).isNullOrEmpty();
        assertThat(defaultColumn.getFlexGrow()).isEqualTo(1);
        assertThat(defaultColumn.isSortable()).isFalse();
        var columnWithUITableColumn = grid.getColumns().get(1);
        assertThat(columnWithUITableColumn.getWidth()).isEqualTo("200px");
        assertThat(columnWithUITableColumn.getFlexGrow()).isEqualTo(2);
        assertThat(columnWithUITableColumn.isSortable()).isTrue();
    }

    @Test
    void testCreateGrid_SuperclassUsedAsRowPmo() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = pmo.getClass().getMethod("getSuperItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();

        assertThat(grid.getColumns()).hasSize(1);
    }

    @Test
    void testCreateGrid_SuperclassUsedAsRowPmo_Async() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = pmo.getClass().getMethod("getAsyncSuperItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();
        KaribuUtils.UI.push();

        assertThat(grid.getColumns()).hasSize(1);
    }

    @EnumSource(PushMode.class)
    @ParameterizedTest
    void testUpdateItems(PushMode pushMode) throws NoSuchMethodException {
        UI.getCurrent().getPushConfiguration().setPushMode(pushMode);
        var pmo = new TestTablePmo();
        var method = pmo.getClass().getMethod("getItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();
        assertThat(grid.getListDataView().getItems())
                .as("Initially on creation: Items should be updated synchronously disregarding the push mode")
                .hasSize(1);
        assertThat(grid.getElement().hasAttribute("has-items")).isTrue();

        pmo.clearItems();
        bindingContext.modelChanged();

        assertThat(grid.getListDataView().getItems())
                .as("On model changed: Items should be updated synchronously disregarding the push mode")
                .isEmpty();
        assertThat(grid.getElement().hasAttribute("has-items")).isFalse();
    }

    @EnumSource(PushMode.class)
    @ParameterizedTest
    void testUpdateItems_WithCompletableFuture(PushMode pushMode) throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = pmo.getClass().getMethod("getAsyncItems");
        var ui = UI.getCurrent();
        ui.getPushConfiguration().setPushMode(pushMode);

        var wrapper = UiCreator
                .<Component, NoLabelComponentWrapper> createUiElement(method,
                                                                      pmo,
                                                                      bindingContext,
                                                                      NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();

        var warningLogs = testLogAppender.getLoggedEvents(Level.WARN);
        if (!pushMode.isEnabled()) {
            assertThat(warningLogs).hasSize(1);
            assertThat(warningLogs).first().asString()
                    .contains(pmo.getClass().getName())
                    .contains("asyncItems");
        } else {
            assertThat(warningLogs).isEmpty();
        }
        assertThat(grid.getListDataView().getItems())
                .as("Initially on creation: Items should be updated asynchronously disregarding the push mode")
                .isEmpty();
        assertThat(grid.getElement().hasAttribute("items-loading")).isTrue();

        KaribuUtils.UI.push(ui);

        assertThat(grid.getListDataView().getItems()).hasSize(1);
        assertThat(grid.getElement().hasAttribute("items-loading")).isFalse();
        assertThat(grid.getElement().hasAttribute("has-items")).isTrue();

        pmo.clearItems();
        bindingContext.modelChanged();

        assertThat(grid.getListDataView().getItems())
                .as("On model changed: Items should be updated asynchronously disregarding the push mode")
                .hasSize(1);
        assertThat(grid.getElement().hasAttribute("items-loading")).isTrue();

        KaribuUtils.UI.push(ui);

        assertThat(grid.getListDataView().getItems()).isEmpty();
        assertThat(grid.getElement().hasAttribute("items-loading")).isFalse();
        assertThat(grid.getElement().hasAttribute("has-items")).isFalse();
    }

    @EnumSource(PushMode.class)
    @ParameterizedTest
    void testUpdateItems_WithCompletableFuture_Exception(PushMode pushMode) throws NoSuchMethodException {
        var ui = UI.getCurrent();
        ui.getPushConfiguration().setPushMode(pushMode);
        var pmo = new TestTablePmo();
        var method = pmo.getClass().getMethod("getAsyncItems");
        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method,
                                                                                     pmo,
                                                                                     bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();
        assertThat(grid.getElement().hasAttribute("items-loading")).isTrue();
        KaribuUtils.UI.push(ui);
        assertThat(testLogAppender.getLoggedEvents(Level.ERROR)).isEmpty();
        assertThat(grid.getListDataView().getItems()).hasSize(1);
        assertThat(grid.getElement().hasAttribute("has-errors")).isFalse();
        assertThat(grid.getStyle().get("--error-message")).isNullOrEmpty();

        assertThat(grid.getElement().hasAttribute("items-loading")).isFalse();

        pmo.setException(true);
        bindingContext.modelChanged();
        assertThat(grid.getElement().hasAttribute("items-loading")).isTrue();
        KaribuUtils.UI.push(ui);

        var errorLogs = testLogAppender.getLoggedEvents(Level.ERROR);
        assertThat(errorLogs).hasSize(1);
        assertThat(grid.getListDataView().getItems()).isEmpty();
        assertThat(grid.getElement().hasAttribute("has-errors")).isTrue();
        assertThat(grid.getStyle().get("--error-message"))
                .isNotBlank()
                .doesNotContain(TestTablePmo.EXCEPTION_MESSAGE);
        assertThat(grid.getElement().hasAttribute("items-loading")).isFalse();

        pmo.setException(false);
        bindingContext.modelChanged();
        assertThat(grid.getElement().hasAttribute("has-errors")).isFalse();
        assertThat(grid.getElement().hasAttribute("items-loading")).isTrue();
        KaribuUtils.UI.push(ui);

        assertThat(grid.getElement().hasAttribute("has-errors")).isFalse();
        assertThat(grid.getStyle().get("--error-message")).isNullOrEmpty();
        assertThat(grid.getElement().hasAttribute("items-loading")).isFalse();
    }

    @Test
    void testCreateGrid_ColumnKeysAndText() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = TestTablePmo.class.getMethod("getItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        var component = wrapper.getComponent();

        assertThat(component).isInstanceOf(Grid.class);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();
        Grid.Column<Object> defaultColumn = grid.getColumnByKey("defaultColumn");
        assertThat(defaultColumn).isNotNull();
        assertThat(defaultColumn.getHeaderText()).isEqualTo("Just a default test column");
    }

    public static class TestTablePmo {

        public static final String EXCEPTION_MESSAGE = "Exception message";
        public static final String PLACEHOLDER = "placeholder";
        private List<TestRowPmo> items = List.of(new TestRowPmo());
        private boolean exception = false;

        public void changeItemsList() {
            items = Stream.concat(items.stream(), Stream.of(new TestRowPmo())).toList();
        }

        public void clearItems() {
            items = List.of();
        }

        @UITableComponent(position = 20, rowPmoClass = TestRowPmo.class)
        public List<TestRowPmo> getItems() {
            return items;
        }

        @UITableComponent(position = 30, rowPmoClass = TestSuperRowPmo.class)
        public List<TestRowPmo> getSuperItems() {
            return items;
        }

        @BindPlaceholder(PLACEHOLDER)
        @UITableComponent(position = 20, rowPmoClass = TestRowPmo.class)
        public CompletableFuture<List<TestRowPmo>> getAsyncItems() {
            if (!exception) {
                return CompletableFuture.supplyAsync(() -> items);
            } else {
                return CompletableFuture.failedFuture(new RuntimeException(EXCEPTION_MESSAGE));
            }
        }

        @UITableComponent(position = 30, rowPmoClass = TestSuperRowPmo.class)
        public CompletableFuture<List<TestRowPmo>> getAsyncSuperItems() {
            return getAsyncItems();
        }

        public void setException(boolean exception) {
            this.exception = exception;
        }
    }

    public static class TestRowPmo extends TestSuperRowPmo {

        @UITableColumn(width = 200, flexGrow = 2, textAlign = TextAlignment.RIGHT, sortable = true)
        @UILabel(position = 20)
        public String getFancyColumn() {
            return "fancy";
        }
    }

    public static class TestSuperRowPmo {

        @UILabel(position = 10, label = "Just a default test column")
        public String getDefaultColumn() {
            return "default";
        }
    }
}
