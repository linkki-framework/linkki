package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.types.TextAlignment;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.shared.communication.PushMode;

@ExtendWith(KaribuUIExtension.class)
class UITableComponentIntegrationTest {

    private final BindingContext bindingContext = new BindingContext();

    @BeforeEach
    void disablePush() {
        UI.getCurrent().getPushConfiguration().setPushMode(PushMode.DISABLED);
    }

    @Test
    void testCreateGrid() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = TestTablePmo.class.getMethod("getItems");

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
        assertThat(grid.getListDataView().getItems()).isEmpty();
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
        var method = TestTablePmo.class.getMethod("getSuperItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();

        assertThat(grid.getColumns()).hasSize(1);
    }

    @Test
    void testUpdateItems() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = TestTablePmo.class.getMethod("getItems");

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();
        assertThat(grid.getListDataView().getItems()).isEmpty();
        assertThat(grid.getElement().getAttribute("has-items")).isNullOrEmpty();

        pmo.changeItemsList();
        bindingContext.modelChanged();

        assertThat(grid.getListDataView().getItems()).hasSize(1);
        assertThat(grid.getElement().getAttribute("has-items")).isNotNull();
    }

    @Test
    void testUpdateItems_WithPush() throws NoSuchMethodException {
        var pmo = new TestTablePmo();
        var method = TestTablePmo.class.getMethod("getItems");
        var ui = com.vaadin.flow.component.UI.getCurrent();
        ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);

        var wrapper = UiCreator.<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                                     NoLabelComponentWrapper::new);
        @SuppressWarnings("unchecked")
        var grid = (Grid<Object>)wrapper.getComponent();

        KaribuUtils.UI.push(ui);
        assertThat(grid.getListDataView().getItems()).isEmpty();

        pmo.changeItemsList();
        bindingContext.modelChanged();

        KaribuUtils.UI.push(ui);
        assertThat(grid.getListDataView().getItems()).hasSize(1);
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

        private List<TestRowPmo> items = List.of();

        @UIButton(position = 10)
        public void changeItemsList() {
            items = Stream.concat(items.stream(), Stream.of(new TestRowPmo())).toList();
        }

        @UITableComponent(position = 20, rowPmoClass = TestRowPmo.class)
        public List<TestRowPmo> getItems() {
            return items;
        }

        @UITableComponent(position = 30, rowPmoClass = TestSuperRowPmo.class)
        public List<TestRowPmo> getSuperItems() {
            return items;
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

        @UILabel(position = 10)
        public String getDefaultColumn() {
            return "default";
        }
    }
}
