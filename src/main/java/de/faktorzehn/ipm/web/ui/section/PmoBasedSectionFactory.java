package de.faktorzehn.ipm.web.ui.section;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyBehaviorProvider;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;
import de.faktorzehn.ipm.web.ui.section.annotations.UICheckBox;
import de.faktorzehn.ipm.web.ui.section.annotations.UIComboBox;
import de.faktorzehn.ipm.web.ui.section.annotations.UIDateField;
import de.faktorzehn.ipm.web.ui.section.annotations.UIIntegerField;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextField;
import de.faktorzehn.ipm.web.ui.table.ContainerPmo;
import de.faktorzehn.ipm.web.ui.table.PmoBasedTableSectionFactory;
import de.faktorzehn.ipm.web.ui.table.TableSection;

/**
 * Base class for a factory to create a section based on an annotated PMO.
 * <p>
 * This class is used as a base class for specializations. It need to be abstract to ensure
 * distinction of different implementation when used via dependency injection. If you do not need
 * further specialization just use {@link DefaultPmoBasedSectionFactory}.
 * 
 * @see UISection
 * @see UITextField
 * @see UICheckBox
 * @see UIDateField
 * @see UIComboBox
 * @see UIIntegerField
 */
public abstract class PmoBasedSectionFactory {

    @Inject
    private PropertyBehaviorProvider propertyBehaviorProvider;

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO.
     */
    public BaseSection createSection(PresentationModelObject pmo, BindingContext bindingContext) {
        checkNotNull(pmo);
        checkNotNull(bindingContext);

        SectionCreationContext creator = new SectionCreationContext(pmo, bindingContext, getPropertyBehaviorProvider());
        return creator.createSection();
    }

    /**
     * Creates a new section based on the given annotated PMO and binds the created controls via the
     * given binding context to the PMO.
     */
    public TableSection createTableSection(ContainerPmo<?> pmo, BindingContext bindingContext) {
        checkNotNull(pmo);
        checkNotNull(bindingContext);

        PmoBasedTableSectionFactory factory = new PmoBasedTableSectionFactory(pmo, bindingContext,
                propertyBehaviorProvider);
        return factory.createSection();
    }

    public static PropertyDispatcher createDefaultDispatcher(PresentationModelObject pmo) {
        return new SectionCreationContext(pmo, null, null).createDefaultDispatcher();
    }

    public PropertyBehaviorProvider getPropertyBehaviorProvider() {
        return propertyBehaviorProvider;
    }

}
