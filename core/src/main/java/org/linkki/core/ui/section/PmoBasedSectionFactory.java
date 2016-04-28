package org.linkki.core.ui.section;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import org.linkki.core.PresentationModelObject;
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
 * 
 * @see UISection
 * @see UITextField
 * @see UICheckBox
 * @see UIDateField
 * @see UIComboBox
 * @see UIIntegerField
 */
public class PmoBasedSectionFactory {

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO.
     */
    public BaseSection createSection(PresentationModelObject pmo, BindingContext bindingContext) {
        checkNotNull(pmo);
        checkNotNull(bindingContext);

        SectionCreationContext creator = new SectionCreationContext(pmo, bindingContext);
        return creator.createSection();
    }

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO.
     */
    public <T extends PresentationModelObject> TableSection<T> createTableSection(ContainerPmo<T> pmo,
            BindingContext bindingContext) {
        checkNotNull(pmo);
        checkNotNull(bindingContext);

        PmoBasedTableSectionFactory<T> factory = new PmoBasedTableSectionFactory<>(pmo, bindingContext);
        return factory.createSection();
    }

}
