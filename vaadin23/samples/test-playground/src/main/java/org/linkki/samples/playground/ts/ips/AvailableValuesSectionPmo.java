package org.linkki.samples.playground.ts.ips;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.binding.annotation.UIRadioButtonGroup;
import org.linkki.samples.playground.ips.model.IpsModelObject;

@UISection
public class AvailableValuesSectionPmo {

    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UIComboBox(position = 10, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_INTEGERENUMERATIONVALUESET)
    public void integerEnumerationValueSet() {
        // model binding
    }

    @UIRadioButtons(position = 11, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_INTEGERENUMERATIONVALUESET)
    public void integerEnumerationValueSetRadioButtons() {
        // model binding
    }

    @UIComboBox(position = 20, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_ENUMERATIONVALUESET)
    public void enumerationValueSet() {
        // model binding
    }

    @UIRadioButtonGroup(position = 21, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_ENUMERATIONVALUESET)
    public void enumerationValueSetRadioButtons() {
        // model binding
    }

    @UIComboBox(position = 30, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_INTEGERRANGEVALUESET)
    public void integerRangeValueSet() {
        // model binding
    }

    @UIComboBox(position = 40, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_BOOLEANVALUESET)
    public void booleanValueSet() {
        // model binding
    }

    @UIRadioButtonGroup(position = 41, content = AvailableValuesType.ENUM_VALUES_INCL_NULL, modelAttribute = IpsModelObject.PROPERTY_BOOLEANVALUESET)
    public void booleanValueSetRadioButtons() {
        // model binding
    }
}
