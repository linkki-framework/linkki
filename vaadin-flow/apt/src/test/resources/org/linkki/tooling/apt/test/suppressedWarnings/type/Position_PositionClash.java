package org.linkki.tooling.apt.test.suppressedWarnings.type;


import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.ReportType;

@SuppressWarnings("linkki")
@UISection
public class Position_PositionClash {

    @UITextField(position = 10)
    public String getDescription() {
        return "";
    }

    @UIComboBox(position = 10)
    public ReportType getType() {
        return null;
    }

}