package org.linkki.tooling.apt.test.compilerOptionsTest.linkki.modelAttribute.badStyle;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.Report;

@UISection
public class ImplicitModelBinding {

    @ModelObject
    public Report getReport() {
        return null;
    }

    @UIComboBox(position = 10)
    public void type() {
        // model binding
    }

}
