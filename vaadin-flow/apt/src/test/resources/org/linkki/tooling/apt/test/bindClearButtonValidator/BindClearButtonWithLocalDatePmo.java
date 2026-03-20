package org.linkki.tooling.apt.test.bindClearButtonValidator;

import java.time.LocalDate;

import org.linkki.core.ui.aspects.annotation.BindClearButton;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindClearButtonWithLocalDatePmo {

    @BindClearButton
    @UIDateField(position = 10)
    public LocalDate getDateField() {
        return null;
    }

    public void setDateField(LocalDate localDate) {

    }
}