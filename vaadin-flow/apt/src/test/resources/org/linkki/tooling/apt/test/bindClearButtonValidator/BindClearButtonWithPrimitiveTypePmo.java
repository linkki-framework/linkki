package org.linkki.tooling.apt.test.bindClearButtonValidator;

import org.linkki.core.ui.aspects.annotation.BindClearButton;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindClearButtonWithPrimitiveTypePmo {

    @BindClearButton
    @UIIntegerField(position = 0)
    public int getIntegerField() {
        return 42;
    }

}