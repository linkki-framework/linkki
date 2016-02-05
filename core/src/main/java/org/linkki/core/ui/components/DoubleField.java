package org.linkki.core.ui.components;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DoubleField extends NumberField {

    private static final long serialVersionUID = 7642987884125435087L;

    public DoubleField(Locale locale) {
        super(NumberFormat.getNumberInstance(locale));
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
