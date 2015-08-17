package de.faktorzehn.ipm.web.ui.section;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

import de.faktorzehn.ipm.web.EditAction;
import de.faktorzehn.ipm.web.ui.application.ApplicationStyles;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

/**
 * A form section is a section that contains a label and control in each line (by default). The form
 * section also allows to have two or more "columns", each column containing label/control pairs.
 */
public class FormSection extends BaseSection {

    private static final long serialVersionUID = 1L;

    // The panel containing the content grid
    private Panel content;

    private GridLayout contentGrid;

    /**
     * Creates a new section with the given caption that is not closeable.
     */
    public FormSection(@Nonnull String caption) {
        this(caption, false);
    }

    /**
     * Creates a new section with a caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed/opened.
     */
    public FormSection(String caption, boolean closeable) {
        this(caption, closeable, null);
    }

    /**
     * Creates a new, section with the given caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editAction If present the section has a button to trigger the given action.
     */
    public FormSection(String caption, boolean closeable, Optional<EditAction> editAction) {
        super(caption, closeable, editAction);
        createContent();
        setSpacingInContent(true);
    }

    /**
     * Returns the number of "columns" / label/control pairs per row. Override in subclasses if you
     * want to have more than one column.
     */
    protected int getNumOfColumns() {
        return 1;
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

    /**
     * Creates a grid for the content part of the section. By default the label contains 3 columns.
     * The first column for the labels, the second for the control and the third to grab any
     * available space if the control has a fixed size.
     */
    private void createContent() {
        int columns = getNumOfColumns();
        int gridColumns = getNumOfColumns() * 3;
        contentGrid = new GridLayout(gridColumns, 1);
        contentGrid.setStyleName(ApplicationStyles.GRID);
        contentGrid.setMargin(new MarginInfo(false, true, true, true));
        float controlExpandRatio = 1.0f / columns;
        for (int i = 0; i < columns; i++) {
            contentGrid.setColumnExpandRatio(i * 3, 0);
            contentGrid.setColumnExpandRatio(i * 3 + 1, 0);
            contentGrid.setColumnExpandRatio(i * 3 + 2, controlExpandRatio);
        }

        content = new Panel(contentGrid);
        content.setStyleName(ValoTheme.PANEL_BORDERLESS);
        addComponent(content);
    }

    /**
     * Adds a component to the content without a label in front. The component takes the whole
     * available width.
     */
    @Override
    public void add(Component component) {
        int row = contentGrid.getCursorY();
        int column = contentGrid.getCursorX();
        if (component.getWidth() == 100.0f && component.getWidthUnits() == Unit.PERCENTAGE) {
            contentGrid.addComponent(component, column, row, column + 2, row);
        } else {
            contentGrid.addComponent(component, column, row, column + 1, row);
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
        contentGrid.addComponent(l);
        contentGrid.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        if (component.getWidth() == 100.0f && component.getWidthUnits() == Unit.PERCENTAGE) {
            // if the component has 100% width, it spans the last two grid columns to grab all
            // available space.
            int row = contentGrid.getCursorY();
            int column = contentGrid.getCursorX();
            contentGrid.addComponent(component, column, row, column + 1, row);
        } else {
            // if the component has a fixed size, the control is placed in the second column,
            // so that the third column can grab the available space.
            contentGrid.addComponent(component);
            ComponentFactory.addHorizontalSpacer(contentGrid);
        }
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
