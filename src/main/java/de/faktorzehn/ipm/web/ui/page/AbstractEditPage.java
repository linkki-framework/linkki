package de.faktorzehn.ipm.web.ui.page;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.BindingManager;
import de.faktorzehn.ipm.web.ui.section.AbstractSection;
import de.faktorzehn.ipm.web.ui.util.ComponentFactory;

/**
 * A page to display and edit data in a vertical layout. The page is made up of so called sections.
 * Sections can be added to the page taking either the whole width of the page or to take 50% of the
 * page.
 * 
 * @see AbstractSection
 */
public abstract class AbstractEditPage extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    /**
     * Adds the given component / section to the page taking 100% of the page width.
     */
    protected void add(Component section) {
        addComponent(section);
    }

    /**
     * Adds the two components / sections to the page, each taking 50% of the page width.
     */
    protected void add(Component leftSection, Component rightSection) {
        add(0, leftSection, rightSection);
    }

    /**
     * Adds the two components / sections to the page, each taking 50% of the page width.
     */
    protected void add(Component... sections) {
        add(0, sections);
    }

    /**
     * Adds the two components / sections to the page, and indents them. Each section is taking 50%
     * of the rest of the page width.
     * 
     * @param indentation level of indentation, indentation size is indentation * 30px. Indentation
     *            0 or less does not indent.
     */
    protected void add(int indentation, Component... sections) {
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth("100%");
        wrapper.setSpacing(true);
        addComponent(wrapper);
        if (indentation > 0) {
            ComponentFactory.addHorizontalFixedSizeSpacer(wrapper, 30 * indentation);
        }
        wrapper.addComponents(sections);
        for (Component component : wrapper) {
            wrapper.setExpandRatio(component, 1 / sections.length);
        }
    }

    /**
     * Scrolls the view so that the given model object is visible.
     * 
     * @param modelObject The model object to show
     */
    public void scrollIntoView(Object modelObject) {
        // TODO not implemented, yet. Can this be done in a generic way?
    }

    protected abstract BindingManager getBindingManager();

    public abstract void update();

    /**
     * Returns a cached binding context if present, otherwise creates a new one. Caches one binding
     * context for each edit page subclass.
     */
    protected abstract BindingContext getBindingContext();

}
