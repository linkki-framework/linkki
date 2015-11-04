package de.faktorzehn.ipm.web.ui.components;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleField extends NumberField {

    private static final long serialVersionUID = 7642987884125435087L;

    public DoubleField() {
        super(NumberFormat.getNumberInstance());
        init();
    }

    public DoubleField(String format) {
        super(new DecimalFormat(format));
        init();
    }

    private void init() {
        setConverter(new DoubleFieldConverter(getFormat()));
    }
}
