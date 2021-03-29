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

package org.linkki.core.vaadin.component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.linkki.core.vaadin.component.shared.MultiformatDateFieldState;

import com.vaadin.ui.DateField;

/**
 * An extension to existing Vaadin {@link DateField} which supports multiple formats as input format.
 * The default date format is always used to render the date after input.
 * <p>
 * By setting any date format per default the date format without separators ('.', '-' or '/') is set as
 * additional format. That means for example for a date format {@code dd.MM.yyyy} an input using the
 * format {@code ddMMyyyy} is also valid.
 * <p>
 * Additionally a second date format for two-digit-year is added whenever the date format specifies
 * four-digit-year.
 * <p>
 * <em>Example:</em> a date format {@code dd.MM.yyyy} will support the following entries:
 * <ul>
 * <li>01.01.2020</li>
 * <li>01.01.20</li>
 * <li>01012020</li>
 * <li>010120</li>
 * </ul>
 * 
 * Every one of these inputs will be formatted and displayed as '01.01.2020'.
 */
public class MultiformatDateField extends DateField {

    private static final long serialVersionUID = 1L;

    public MultiformatDateField() {
        super();
    }

    /**
     * Constructs a new {@code MultiformatDateField} with the given caption and initial text contents.
     *
     * @param caption the caption <code>String</code> for the editor.
     * @param value the LocalDate value.
     */
    public MultiformatDateField(String caption, LocalDate value) {
        super(caption, value);
    }

    /**
     * Constructs an empty {@code MultiformatDateField} with caption.
     *
     * @param caption the caption of the datefield.
     */
    public MultiformatDateField(String caption) {
        super(caption);
    }

    /**
     * Constructs a new {@code MultiformatDateField} with a value change listener.
     * <p>
     * The listener is called when the value of this {@code MultiformatDateField} is changed either by
     * the user or programmatically.
     *
     * @param valueChangeListener the value change listener, not {@code null}
     */
    public MultiformatDateField(
            ValueChangeListener<LocalDate> valueChangeListener) {
        super();
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Constructs a new {@code MultiformatDateField} with the given caption and a value change listener.
     * <p>
     * The listener is called when the value of this {@code MultiformatDateField} is changed either by
     * the user or programmatically.
     *
     * @param caption the caption for the field
     * @param valueChangeListener the value change listener, not {@code null}
     */
    public MultiformatDateField(String caption,
            ValueChangeListener<LocalDate> valueChangeListener) {
        this(valueChangeListener);
        setCaption(caption);
    }

    /**
     * Constructs a new {@code MultiformatDateField} with the given caption, initial text contents and a
     * value change listener.
     * <p>
     * The listener is called when the value of this {@code MultiformatDateField} is changed either by
     * the user or programmatically.
     *
     * @param caption the caption for the field
     * @param value the value for the field, not {@code null}
     * @param valueChangeListener the value change listener, not {@code null}
     */
    public MultiformatDateField(String caption, LocalDate value,
            ValueChangeListener<LocalDate> valueChangeListener) {
        this(caption, value);
        addValueChangeListener(valueChangeListener);
    }


    @Override
    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        List<String> alternativeFormats = new ArrayList<>(3);

        String formatWithoutSymbols = dateFormat.replaceAll("[^A-Za-z]", "");
        alternativeFormats.add(formatWithoutSymbols);

        String twoDigitFormatWithoutSymbols = formatWithoutSymbols.replaceAll("y+", "yy").replaceAll("M+", "MM")
                .replaceAll("d+", "dd");
        alternativeFormats.add(twoDigitFormatWithoutSymbols);

        if (dateFormat.contains("yyyy")) {
            String twoDigitYearFormat = dateFormat.replaceAll("yyyy", "yy");
            alternativeFormats.add(twoDigitYearFormat);
        } else {
            String fourDigitYearFormatWithoutSymbols = twoDigitFormatWithoutSymbols.replace("yy", "yyyy");
            alternativeFormats.add(fourDigitYearFormatWithoutSymbols);
        }

        setAlternativeFormats(alternativeFormats);
    }

    /**
     * Set alternative input formats accepted by the component. The alternative formats are applied in
     * the order they are assigned in.
     *
     * Set an empty list to remove all alternative formats.
     *
     * @param formats List of dateformat strings, not {@code null}
     */
    public void setAlternativeFormats(List<String> formats) {
        getState().setAlternativeFormats(formats.toArray(new String[formats.size()]));
    }

    public void addAlternativeFormat(String formatString) {
        String format = Objects.toString(formatString, "").trim();

        if (!format.isEmpty() && !getAlternativeFormat().contains(format)) {
            List<String> formats = new ArrayList<>(getAlternativeFormat());
            formats.add(format);
            setAlternativeFormats(formats);
        }
    }

    public List<String> getAlternativeFormat() {
        return Arrays.asList(getState().getAlternativeFormats());
    }

    @Override
    protected MultiformatDateFieldState getState() {
        return (MultiformatDateFieldState)super.getState();
    }
}
