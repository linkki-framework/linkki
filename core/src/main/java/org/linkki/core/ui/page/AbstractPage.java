package org.linkki.core.ui.page;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.PostConstruct;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingManager;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * A page allows displaying and editing data in a vertical layout. It usually consists of so called
 * sections but can also contain arbitrary UI components. Sections (or other components) can be
 * added to take up either the whole width or 50% of the page.
 * 
 * If the page is created via injection framework, the {@link #init()} method is called
 * automatically and ensures that the {@link #createContent()} method is called. Additionally,
 * margins are added to the page.
 * 
 * Note: If the page is not injected you need to call {@link #init()} manually!
 * 
 * @see AbstractSection
 */
public abstract class AbstractPage extends VerticalLayout implements Page {

    private static final long serialVersionUID = 1L;

    private final PmoBasedSectionFactory sectionFactory;

    /**
     * Creates a page without top margin and with margins on left, right and bottom. Uses the
     * {@link DefaultPmoBasedSectionFactory} to create sections based on given PMOs.
     */
    public AbstractPage() {
        this(new DefaultPmoBasedSectionFactory());
    }

    /**
     * Creates a page without top margin and with margins on left, right and bottom.
     * 
     * @param sectionFactory Factory used to create sections based on given PMOs.
     */
    public AbstractPage(PmoBasedSectionFactory sectionFactory) {
        super();
        this.sectionFactory = requireNonNull(sectionFactory, "sectionFactory must not be null");
        setMargin(new MarginInfo(false, true, true, true));
    }

    @PostConstruct
    public final void init() {
        createContent();
    }

    /**
     * Returns the factory used to create PMO based sections.
     */
    public PmoBasedSectionFactory getPmoBasedSectionFactory() {
        return sectionFactory;
    }

    /**
     * Adds the given component / section to the page taking 100% of the page width.
     */
    protected void add(Component section) {
        addComponent(section);
    }

    /**
     * Creates a section based on the given PMO and adds it to the page taking 100% of the page
     * width. If the PMO is a {@link ContainerPmo} a table section is created.
     * 
     * @return The new section created based on the given PMO.
     */
    protected AbstractSection addSection(Object pmo) {
        AbstractSection section = sectionFactory.createSection(pmo, getBindingContext());
        addComponent(section);
        return section;
    }

    /**
     * Adds the two components / sections to the page, each taking 50% of the page width.
     */
    protected void add(Component leftSection, Component rightSection) {
        add(0, leftSection, rightSection);
    }

    /**
     * Adds the components / sections to the page, each taking an equal part of the page width.
     */
    protected void add(Component... sections) {
        add(0, sections);
    }

    /**
     * Creates sections based on the given PMOs and adds them to the page each taking equal part of
     * the page width. If a PMO is a {@link ContainerPmo} a table section is created.
     * 
     * @throws NullPointerException if one of the given PMOs is <code>null</code>.
     */
    protected void addSections(Object... pmos) {
        AbstractSection[] sections = Arrays.stream(pmos)
                .map(pmo -> sectionFactory.createSection(pmo, getBindingContext()))
                .toArray(AbstractSection[]::new);
        add(0, sections);
    }

    /**
     * Adds the two components / sections to the page, and indents them. Each section is taking an
     * equal part of the rest of the page width.
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
            wrapper.setExpandRatio(component, 1f / sections.length);
        }
    }

    /**
     * Refreshes the content displayed on this page. Data bindings are reloaded, but no sections are
     * removed/added.
     * <p>
     * This method should be overridden if the page contains components that do not use data binding
     * and have to be refreshed manually.
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    public void reloadBindings() {
        getBindingContext().updateUI();
    }

    /**
     * Returns the binding manager used to obtain or create binding contexts.
     * 
     * @see #getBindingContext()
     */
    protected abstract BindingManager getBindingManager();

    /**
     * Returns a cached binding context if present, otherwise creates a new one. Caches one binding
     * context for each class.
     */
    protected BindingContext getBindingContext() {
        return getBindingManager().getExistingContextOrStartNewOne(getClass());
    }

}
