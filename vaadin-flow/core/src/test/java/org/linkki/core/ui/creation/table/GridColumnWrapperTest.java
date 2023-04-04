package org.linkki.core.ui.creation.table;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.grid.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.table.column.annotation.UITableColumn.CollapseMode;

import static org.assertj.core.api.Assertions.assertThat;

class GridColumnWrapperTest {

    private Grid.Column<Object> column;

    @BeforeEach
    void createGrid() {
        var grid = new Grid<>();
        column = grid.addColumn(Object::toString);
    }

    @Test
    void testSetLabel_SetData() {
        var columnWrapper = new GridColumnWrapper(column);

        columnWrapper.setLabel("label");

        assertThat(ComponentUtil.getData(column, GridColumnWrapper.KEY_HEADER)).isEqualTo("label");
    }

    @Test
    void testSetCollapseMode_SetData() {
        var columnWrapper = new GridColumnWrapper(column);

        columnWrapper.setCollapseMode(CollapseMode.COLLAPSIBLE);

        assertThat(ComponentUtil.getData(column, CollapseMode.class)).isEqualTo(CollapseMode.COLLAPSIBLE);

        columnWrapper.setCollapseMode(CollapseMode.NOT_COLLAPSIBLE);

        assertThat(ComponentUtil.getData(column, CollapseMode.class)).isEqualTo(CollapseMode.NOT_COLLAPSIBLE);

        columnWrapper.setCollapseMode(CollapseMode.INITIALLY_COLLAPSED);

        assertThat(ComponentUtil.getData(column, CollapseMode.class)).isEqualTo(CollapseMode.INITIALLY_COLLAPSED);
    }
}
