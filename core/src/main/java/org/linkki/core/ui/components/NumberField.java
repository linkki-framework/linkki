package org.linkki.core.ui.components;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Field to represent Numbers. The format is done by {@link java.text.NumberFormat} (and not by
 * {@code com.google.gwt.i18n.client.NumberFormat}), because we are located on the server side.
 *
 * @author Jan Ortmann
 * @author Thorsten Günther
 */
public abstract class NumberField extends TextField {

    private static final long serialVersionUID = 1L;

    public NumberField() {
        setStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
        setConversionError("Die Eingabe stellt keine gültige Zahl dar!");
    }

    @Override
    public void validate() throws InvalidValueException {
        validate(getValue());
    }
}
