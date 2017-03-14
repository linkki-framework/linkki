package org.linkki.core.ui.section;

import java.util.Optional;

import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.core.ui.util.UiUtil;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

public class FormSection extends BaseSection {

    private static final long serialVersionUID = 1L;

    private final Panel content;

    private final GridLayout contentGrid;

    private final int numberOfColumns;

    /**
     * Creates a new section non-closable section with the given caption and 1 column.
     */
    public FormSection(String caption) {
        this(caption, false);
    }

    /**
     * Creates a new section with the given caption and 1 column.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed/opened.
     */
    public FormSection(String caption, boolean closeable) {
        this(caption, closeable, Optional.empty(), 1);
    }

    /**
     * Creates a new section with the given caption, closable state, edit button and number of
     * columns.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editButton If present the section has an edit button in the header.
     * @param numberOfColumns the number of the section's columns
     */
    public FormSection(String caption, boolean closeable, Optional<Button> editButton, int numberOfColumns) {
        super(caption, closeable, editButton);
        this.numberOfColumns = numberOfColumns;
        setWidth("100%");
        contentGrid = createContent();
        content = new Panel(contentGrid);
        content.setStyleName(ValoTheme.PANEL_BORDERLESS);
        addComponent(content);
        setSpacingInContent(true);
    }

    /**
     * Returns the number of "columns" i.e. the label/control pairs per row.
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * Returns the panel containing the content.
     */
    protected Panel getContent() {
        return content;
    }

    /**
     * Returns the grid containing the labels and controls.
     */
    protected GridLayout getContentGrid() {
        return contentGrid;
    }

    protected void setSpacingInContent(boolean spacing) {
        contentGrid.setSpacing(spacing);
    }

    protected GridLayout createContent() {
        int columns = getNumberOfColumns();
        int gridColumns = columns * 3;
        GridLayout gridLayout = new GridLayout(gridColumns, 1);
        gridLayout.setWidth("100%");
        gridLayout.setMargin(new MarginInfo(true, true, true, true));
        gridLayout.setSpacing(true);
        for (int i = 0; i < columns; i++) {
            gridLayout.setColumnExpandRatio(i * 3, 0);
            gridLayout.setColumnExpandRatio(i * 3 + 1, 0);
            gridLayout.setColumnExpandRatio(i * 3 + 2, 1);
        }
        return gridLayout;
    }

    /**
     * Adds a component to the content without a label in front. The component takes the whole
     * available width.
     */
    @Override
    public void add(Component component) {
        int row = contentGrid.getCursorY();
        int column = contentGrid.getCursorX();
        if (UiUtil.isWidth100Pct(component)) {
            contentGrid.addComponent(component, column, row, column + 2, row);
        } else {
            contentGrid.addComponent(component, column, row, column + 1, row);
            ComponentFactory.addHorizontalSpacer(contentGrid);
        }
    }

    /**
     * Adds a component with a label in front. If the component has 100% width, the component will
     * span the 2. and 3. column. If the component hasn't 100% width the component will be placed in
     * the 2. column and an empty label grabbing any available space will be placed in the 3.
     * column.
     */
    @Override
    public Label add(String label, Component component) {
        Label l = new Label(label);
        l.setWidthUndefined();
        contentGrid.addComponent(l);
        contentGrid.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        if (UiUtil.isWidth100Pct(component)) {
            // if the component has 100% width, it spans the last two grid columns to grab all
            // available space.
            int row = contentGrid.getCursorY();
            int column = contentGrid.getCursorX();
            contentGrid.addComponent(component, column, row, column + 1, row);
        } else {
            contentGrid.addComponent(component);
            ComponentFactory.addHorizontalSpacer(contentGrid);
        }
        contentGrid.setComponentAlignment(component, Alignment.MIDDLE_LEFT);
        return l;
    }

    @Override
    protected void switchOpenStatus() {
        super.switchOpenStatus();
        content.setVisible(true);
        contentGrid.setVisible(true);
        super.setAllChildComponentsVisibilityBasedOnOpenStatus(contentGrid);
    }

}
