package org.linkki.core.ui.section;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

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
 * Base class for a factory to create a section based on an annotated PMO.
 * <p>
 * This class is used as a base class. It must be abstract to ensure distinction of different
 * implementations when used via dependency injection. You should never use
 * {@link PmoBasedSectionFactory} directly, only one of its subclasses. If you do not need further
 * specialization just use {@link DefaultPmoBasedSectionFactory}.
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
     * given binding context to the PMO.
     */
    public BaseSection createSection(Object pmo, BindingContext bindingContext) {
        checkNotNull(pmo);
        checkNotNull(bindingContext);

        SectionCreationContext creator = new SectionCreationContext(pmo, bindingContext);
        return creator.createSection();
    }

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO.
     */
    public <T> TableSection<T> createTableSection(ContainerPmo<T> pmo, BindingContext bindingContext) {
        checkNotNull(pmo);
        checkNotNull(bindingContext);

        PmoBasedTableSectionFactory<T> factory = new PmoBasedTableSectionFactory<>(pmo, bindingContext);
        return factory.createSection();
    }

}
