package org.linkki.core.vaadin.component.client;

import java.util.Arrays;
import java.util.Date;

import org.linkki.core.vaadin.component.MultiformatDateField;

import com.google.gwt.i18n.client.TimeZone;
import com.vaadin.client.LocaleNotLoadedException;
import com.vaadin.client.LocaleService;
import com.vaadin.client.ui.VPopupCalendar;

/**
 * Widget for {@link MultiformatDateField}.
 */
public class MultiformatDateFieldWidget extends VPopupCalendar {

    private static final String PARSE_ERROR_CLASSNAME = "-parseerror";

    /** For internal use only. May be removed or replaced in the future. */
    private String formatStr;

    /** For internal use only. May be removed or replaced in the future. */
    private TimeZone timeZone;

    private String[] formatStrings = {};

    /**
     * Gets the date format string for the current locale.
     *
     * @return the format string
     */
    @Override
    public String getFormatString() {
        if (formatStr == null) {
            setFormatString(createFormatString());
        }
        return formatStr;
    }

    /**
     * Sets the date format string to use for the text field.
     *
     * @param formatString the format string to use, or {@code null} to force re-creating the format
     *            string from the locale the next time it is needed
     * @since 8.1
     */
    @Override
    public void setFormatString(String formatString) {
        formatStr = formatString;
    }

    public void setAlternativeFormats(String[] formatStrings) {
        this.formatStrings = Arrays.copyOf(formatStrings, formatStrings.length);
    }

    public String[] getAlternativeFormats() {
        return Arrays.copyOf(formatStrings, formatStrings.length);
    }

    /**
     * Sets the time zone for the field.
     *
     * @param timeZone the new time zone to use
     * @since 8.2
     */
    @Override
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Create a format string suitable for the widget in its current state.
     *
     * @return a date format string to use when formatting and parsing the text in the input field
     * @since 8.1
     */
    @Override
    protected String createFormatString() {
        if (isYear(getCurrentResolution())) {
            // force full year
            return "yyyy";
        }
        try {
            String frmString = LocaleService.getDateFormat(currentLocale);
            return cleanFormat(frmString);
        } catch (LocaleNotLoadedException e) {
            return null;
        }
    }

    @Override
    public void updateBufferedValues() {
        updateDate();
        bufferedDateString = text.getText();
        updateBufferedResolutions();
    }

    private void updateDate() {
        if (!text.getText().isEmpty()) {
            try {
                String enteredDate = text.getText();
                Date newDate = tryParseToDate(enteredDate);
                setDate(newDate);

                text.setValue(getDateTimeService()
                        .formatDate(newDate, getFormatString(), timeZone), false);

                removeStyleName(getStylePrimaryName() + PARSE_ERROR_CLASSNAME);
                // CSOFF: IllegalCatch
            } catch (final Exception e) {
                System.out.println(e.getMessage());
                addStyleName(getStylePrimaryName() + PARSE_ERROR_CLASSNAME);
                setDate(null);
            }
            // CSON: IllegalCatch
        } else {
            setDate(null);
            // remove possibly added invalid value indication
            removeStyleName(getStylePrimaryName() + PARSE_ERROR_CLASSNAME);
        }
    }

    private Date tryParseToDate(String dateString) {
        try {
            return getDateTimeService().parseDate(dateString, getFormatString(),
                                                  lenient);
        } catch (IllegalArgumentException e) {
            // NO-OP: try alternative formats if present
        }
        for (String fmt : formatStrings) {
            try {
                return getDateTimeService().parseDate(dateString, fmt, lenient);
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        return null;
    }
}