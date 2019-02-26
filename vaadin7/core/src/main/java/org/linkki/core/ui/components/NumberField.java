/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
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
