/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.page;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.ui.section.BaseSection;
import de.faktorzehn.ipm.web.ui.section.DefaultPmoBasedSectionFactory;
import de.faktorzehn.ipm.web.ui.section.PmoBasedSectionFactory;
import de.faktorzehn.ipm.web.ui.table.ContainerPmo;
import de.faktorzehn.ipm.web.ui.table.TableSection;

/**
 * An edit page that makes it easy to a create and add the sections based on PMOs via the
 * {@link DefaultPmoBasedSectionFactory}.
 *
 * @author Jan Ortmann
 */
public abstract class SectionFactoryBasedEditPage extends AbstractEditPage {

    private static final long serialVersionUID = 1L;

    private PmoBasedSectionFactory sectionFactory;

    public SectionFactoryBasedEditPage(@Nonnull PmoBasedSectionFactory sectionFactory) {
        super();
        checkNotNull(sectionFactory);
        this.sectionFactory = sectionFactory;
    }

    /**
     * Creates a new section based on the given PMO and adds the section to the page.
     */
    protected void createSection(PresentationModelObject pmo) {
        BaseSection newSection = sectionFactory.createSection(pmo, getBindingContext());
        add(newSection);
    }

    /**
     * Creates a new section based on the given PMO and adds the section to the page.
     */
    protected TableSection createTableSection(ContainerPmo<? extends PresentationModelObject> containerPmo) {
        TableSection newSection = sectionFactory.createTableSection(containerPmo, getBindingContext());
        add(newSection);
        return newSection;
    }

    /**
     * Creates a new left section and a new right section based on the given PMOs and adds the new
     * sections to the page.
     */
    protected void createSections(PresentationModelObject pmo4Left, PresentationModelObject pmo4Right) {
        BaseSection newLeftSection = sectionFactory.createSection(pmo4Left, getBindingContext());
        BaseSection newRightSection = sectionFactory.createSection(pmo4Right, getBindingContext());
        add(newLeftSection, newRightSection);
    }

    /**
     * Creates a new left section and a new right section based on the given PMOs and adds the new
     * sections to the page.
     * 
     * @param indentation The level of indentation, 0 zero means no indentation.
     */
    protected void createSections(int indentation, PresentationModelObject pmo4Left, PresentationModelObject pmo4Right) {
        BaseSection newLeftSection = sectionFactory.createSection(pmo4Left, getBindingContext());
        BaseSection newRightSection = sectionFactory.createSection(pmo4Right, getBindingContext());
        add(indentation, newLeftSection, newRightSection);
    }
}
