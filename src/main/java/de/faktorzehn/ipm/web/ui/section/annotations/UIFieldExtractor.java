package de.faktorzehn.ipm.web.ui.section.annotations;

import java.lang.reflect.Method;

import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UICheckBoxAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIComboBoxAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDateFieldAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDecimalFieldAdapter;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIDoubleFieldAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UIIntegerFieldAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextAreaAdpater;
import de.faktorzehn.ipm.web.ui.section.annotations.adapters.UITextFieldAdpater;

public class UIFieldExtractor {

    public static UIFieldDefinition getUiField(Method method) {
        UIFieldDefinition uiField = null;
        if (method.getAnnotation(UITextField.class) != null) {
            uiField = new UITextFieldAdpater(method.getAnnotation(UITextField.class));
        } else if (method.getAnnotation(UIComboBox.class) != null) {
            uiField = new UIComboBoxAdpater(method.getAnnotation(UIComboBox.class));
        } else if (method.getAnnotation(UICheckBox.class) != null) {
            uiField = new UICheckBoxAdpater(method.getAnnotation(UICheckBox.class));
        } else if (method.getAnnotation(UIIntegerField.class) != null) {
            uiField = new UIIntegerFieldAdpater(method.getAnnotation(UIIntegerField.class));
        } else if (method.getAnnotation(UIDateField.class) != null) {
            uiField = new UIDateFieldAdpater(method.getAnnotation(UIDateField.class));
        } else if (method.getAnnotation(UITextArea.class) != null) {
            uiField = new UITextAreaAdpater(method.getAnnotation(UITextArea.class));
        } else if (method.getAnnotation(UIDoubleField.class) != null) {
            uiField = new UIDoubleFieldAdpater(method.getAnnotation(UIDoubleField.class));
        } else if (method.getAnnotation(UIDecimalField.class) != null) {
            uiField = new UIDecimalFieldAdapter(method.getAnnotation(UIDecimalField.class));
        }
        return uiField;
    }
}
