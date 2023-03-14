package org.linkki.tooling.apt.test.suppressedWarnings.method;


import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.ReportType;

@UISection
public class Position_PositionClash {

    @SuppressWarnings("linkki")
    @UITextField(position = 10)
    public String getDescription() {
        return "";
    }

    @SuppressWarnings("linkki")
    @UIComboBox(position = 10)
    public ReportType getType() {
        return null;
    }

}