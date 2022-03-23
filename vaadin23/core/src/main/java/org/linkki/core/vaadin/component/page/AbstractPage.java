/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.vaadin.component.page;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.OverrideMustInvoke;

/**
 * A page allows displaying and editing data in a vertical layout. It usually consists of so called
 * sections but can also contain arbitrary UI components. Sections (or other components) can be added to
 * take up either the whole width or 50% of the page.
 * 
 * If the page is created via injection framework, the {@link #init()} method is called automatically
 * and ensures that the {@link #createContent()} method is called. Additionally, margins are added to
 * the page.
 * 
 * Note: If the page is not injected you need to call {@link #init()} manually!
 * 
 * @see LinkkiSection
 */
@CssImport(value = "./styles/linkki-page.css")
public abstract class AbstractPage extends VerticalLayout implements Page {

    private static final long serialVersionUID = 1L;

    private final PmoBasedSectionFactory sectionFactory;

    /**
     * Creates a page without top margin and with margins on left, right and bottom. Uses the
     * {@link PmoBasedSectionFactory} to create sections based on given PMOs.
     */
    public AbstractPage() {
        this(new PmoBasedSectionFactory());
    }

    /**
     * Creates a page without top margin and with margins on left, right and bottom.
     * 
     * @param sectionFactory Factory used to create sections based on given PMOs.
     */
    public AbstractPage(PmoBasedSectionFactory sectionFactory) {
        super();
        this.sectionFactory = requireNonNull(sectionFactory, "sectionFactory must not be null");
        addClassName("linkki-page");
        setPadding(true);
    }

    /**
     * Creates the actual UI. This cannot be done in the constructor, because clients can provide
     * subclasses with specialized BindingManagers and/or section-factories that are not available in
     * this super-class. In order to be able to create a UI, the initialization must be performed
     * <em>after</em> constructors, subclass constructors and dependency injection (constructor and
     * field injection). Hence a separate init-method. It is annotated as post-construct so the DI
     * framework can call it automatically.
     * <p>
     * Must be called manually if no dependency injection framework is used.
     */
    @OverrideMustInvoke
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
     * Creates a section based on the given PMO and adds it to the page taking 100% of the page width.
     * If the PMO is a {@link ContainerPmo} a table section is created.
     * 
     * @return The new section created based on the given PMO.
     */
    protected LinkkiSection addSection(Object pmo) {
        LinkkiSection section = sectionFactory.createSection(pmo, getBindingContext());
        add(section);
        return section;
    }

    /**
     * Creates sections based on the given PMOs and adds them horizontally, each taking up equal part of
     * the page width. If a PMO is a {@link ContainerPmo} a table section is created.
     * <p>
     * To add sections vertically, call {@link #addSection(Object)} for each PMO instead.
     * 
     * @throws NullPointerException if one of the given PMOs is <code>null</code>.
     */
    protected void addSections(@NonNull Object... pmos) {
        addHorizontally(Arrays.stream(pmos)
                .map(pmo -> sectionFactory.createSection(pmo, getBindingContext()))
                .toArray(LinkkiSection[]::new));
    }

    /**
     * Adds the components / sections horizontally, each section taking up an equal part of the page
     * width.
     * <p>
     * To add components vertically, call {@link #add(Component...)} for each component instead.
     */
    public void addHorizontally(Component... sections) {
        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.setWidth("100%");
        wrapper.setSpacing(true);
        add(wrapper);
        wrapper.addAndExpand(sections);
    }

    /**
     * Refreshes the content displayed on this page. Data bindings are reloaded, but no sections are
     * removed/added.
     * <p>
     * This method should be overridden if the page contains components that do not use data binding and
     * have to be refreshed manually.
     */
    @Override
    @OverrideMustInvoke
    public void reloadBindings() {
        getBindingContext().uiUpdated();
    }

    /**
     * Returns the binding manager used to obtain or create binding contexts.
     * 
     * @see #getBindingContext()
     */
    protected abstract BindingManager getBindingManager();

    /**
     * Returns the binding context for this page.
     * 
     * @implNote the default binding context is identified by the {@link #getClass() current class}
     */
    protected BindingContext getBindingContext() {
        return getBindingManager().getContext(getClass());
    }

}
