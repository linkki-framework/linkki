package org.linkki.tooling.apt.test.suppressedWarnings.method;


import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class DynamicField_PositionMismatch {

    @SuppressWarnings("linkki")
    @UITextArea(position = 11, label = "description")
    @UITextField(position = 10, label = "description")
    public String getDescription() {
        return "";
    }

    public Class<?> getDescriptionComponentType() {
        return UITextField.class;
    }
}