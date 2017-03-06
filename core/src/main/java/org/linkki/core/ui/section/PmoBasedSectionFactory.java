package org.linkki.core.ui.section;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UIIntegerField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.PmoBasedTableSectionFactory;
import org.linkki.core.ui.table.TableSection;

/**
 * Base class for a factory to create sections based on an annotated PMO.
 * <p>
 * This class is used as a base class. It must be abstract to ensure distinction of different
 * implementations when used via dependency injection. If you do not need further specialization
 * just use {@link DefaultPmoBasedSectionFactory}.
 * 
 * @see UISection
 * @see UITextField
 * @see UICheckBox
 * @see UIDateField
 * @see UIComboBox
 * @see UIIntegerField
 */
public abstract class PmoBasedSectionFactory {

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO. If the given PMO is a {@link ContainerPmo}, a table section
     * is created.
     */
    public AbstractSection createSection(Object pmo, BindingContext bindingContext) {
        if (pmo instanceof ContainerPmo<?>) {
            return createTableSection((ContainerPmo<?>)pmo, bindingContext);
        } else {
            return createBaseSection(pmo, bindingContext);
        }
    }


    /**
     * Creates a new base section based on the given annotated PMO and binds the created controls
     * via the given binding context to the PMO.
     */
    public BaseSection createBaseSection(Object pmo, BindingContext bindingContext) {
        SectionCreationContext creator = new SectionCreationContext(requireNonNull(pmo, "pmo must not be null"),
                requireNonNull(bindingContext, "bindingContext must not be null"));
        return creator.createSection();
    }

    /**
     * Creates a new section containing a table based on the given annotated {@link ContainerPmo}
     * and binds the table via the given binding context to the PMO.
     */
    public <T> TableSection<T> createTableSection(ContainerPmo<T> pmo,
            BindingContext bindingContext) {

        PmoBasedTableSectionFactory<T> factory = new PmoBasedTableSectionFactory<>(
                requireNonNull(pmo, "pmo must not be null"),
                requireNonNull(bindingContext, "bindingContext must not be null"));
        return factory.createSection();
    }

}
