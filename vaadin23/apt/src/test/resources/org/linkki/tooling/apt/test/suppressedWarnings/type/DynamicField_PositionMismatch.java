package org.linkki.tooling.apt.test.suppressedWarnings.type;


import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@SuppressWarnings("linkki")
@UISection
public class DynamicField_PositionMismatch {

    @UITextArea(position = 11)
    @UITextField(position = 10)
    public String getDescription() {
        return "";
    }

    public Class<?> getDescriptionComponentType() {
        return UITextField.class;
    }
}