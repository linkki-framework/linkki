package org.linkki.core.ui.components;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class IntegerField extends NumberField {

    private static final long serialVersionUID = 1L;

    public IntegerField() {
        super(NumberFormat.getIntegerInstance());
        init();
    }

    public IntegerField(String format) {
        super(new DecimalFormat(format));
        init();
    }

    private void init() {
        setConverter(new IntegerFieldConverter(getFormat()));
    }

}
