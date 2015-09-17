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
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

public class FormSection extends BaseSection {

    private static final long serialVersionUID = 1L;

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
        this(caption, closeable, Optional.empty());
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
        setWidth("100%");
        contentGrid = createContent();
        content = new Panel(contentGrid);
        content.setStyleName(ValoTheme.PANEL_BORDERLESS);
        addComponent(content);
        setSpacingInContent(true);
    }

    /**
     * Returns the number of "columns" i.e. the label/control pairs per row. Override in subclasses
     * if you want to have more than one column.
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

    protected GridLayout createContent() {
        int columns = getNumOfColumns();
        int gridColumns = getNumOfColumns() * 3;
        GridLayout gridLayout = new GridLayout(gridColumns, 1);
        gridLayout.setWidth("100%");
        gridLayout.setMargin(new MarginInfo(false, true, true, true));
        gridLayout.setSpacing(true);
        float controlExpandRatio = 1.0f / columns;
        for (int i = 0; i < columns; i++) {
            gridLayout.setColumnExpandRatio(i * 3, 0);
            gridLayout.setColumnExpandRatio(i * 3 + 1, 0);
            gridLayout.setColumnExpandRatio(i * 3 + 2, controlExpandRatio);
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
        if (component.getWidth() == 100.0f && component.getWidthUnits() == Unit.PERCENTAGE) {
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
        if (component.getWidth() == 100.0f && component.getWidthUnits() == Unit.PERCENTAGE) {
            // if the component has 100% width, it spans the last two grid columns to grab all
            // available space.
            int row = contentGrid.getCursorY();
            int column = contentGrid.getCursorX();
            contentGrid.addComponent(component, column, row, column + 1, row);
        } else {
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
