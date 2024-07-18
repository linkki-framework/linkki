/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.vaadin.component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.LinkkiComponentUtil;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.TextRenderer;

public class ComponentFactory {

    /**
     * @see <a href="https://en.wikipedia.org/wiki/Non-breaking_space">Non-breaking_space</a>
     */
    public static final String NO_BREAK_SPACE = "&nbsp";
    public static final String AUTOSELECT = "autoselect";

    private ComponentFactory() {
        // prevents instantiation
    }

    /**
     * Creates a new horizontal line.
     */
    public static Component newHorizontalLine() {
        Hr component = new Hr();
        component.setWidth("100%");
        return component;
    }

    /**
     * Creates a new {@link Anchor} with undefined width.
     */
    public static LinkkiAnchor newLink(String caption) {
        LinkkiAnchor link = new LinkkiAnchor();
        link.setText(caption);
        return link;
    }

    /**
     * Creates a new {@link TextField} with an unlimited maximal character count and an undefined
     * width.
     */
    public static TextField newTextField() {
        TextField textField = new TextField();
        textField.setAutocomplete(Autocomplete.OFF);
        return textField;
    }

    /**
     * Returns a new {@link TextField} with the given max length and width.
     * <p>
     * If the given maxLength is less than or equal to 0, the maximal character count is unlimited.
     * <p>
     * If the given width is not specified and maxLength is greater than 0, the width of the field
     * is inferred by maxLength.
     */
    public static TextField newTextField(int maxLength, String width) {
        TextField field = newTextField();
        setMaxLengthAndWidth(field, maxLength, width);

        return field;
    }

    private static void setMaxLengthAndWidth(TextField field, int maxLength, String width) {
        if (maxLength > 0) {
            field.setMaxLength(maxLength);

            if (StringUtils.isEmpty(width)) {
                field.setWidth(maxLength + "em");
            } else {
                field.setWidth(width);
            }
        } else {
            field.setWidth(width);
        }
    }

    /**
     * Creates a new {@link TextField} to display numbers.
     */
    public static TextField newNumberField(int maxLength, String width, String pattern) {
        TextField field = newTextField(maxLength, width);
        field.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        field.setAllowedCharPattern(pattern);
        return field;
    }

    /**
     * Creates a new {@link TextField} to display numbers with the specified formatting pattern. The
     * pattern is converted to a regex by using it to format a test number and checking the result
     * to see which characters are allowed.
     */
    public static TextField newNumberFieldWithFormattingPattern(int maxLength, String width, String pattern) {
        if (UiFramework.getLocale() == null) {
            throw new IllegalStateException(
                    "Creating a number field with a formatting pattern requires a UI with a locale");
        }

        NumberFormat testFormatter = new DecimalFormat(pattern,
                new DecimalFormatSymbols(UiFramework.getLocale()));
        StringBuilder regexBuilder = new StringBuilder("[");
        testFormatter.format(-1234567890).chars()
                .forEach(regexBuilder::appendCodePoint);
        String enabledChars = regexBuilder.append("]").toString().replace(".", "\\.");

        return newNumberField(maxLength, width, enabledChars);
    }

    public static TextArea newTextArea() {
        TextArea textArea = new TextArea();
        LinkkiComponentUtil.preventEnterKeyPropagation(textArea);
        return textArea;
    }

    /**
     * Returns a new {@link TextArea} with the given max length, width and number of rows.
     * <p>
     * If the given maxLength is less than or equal to 0, the maximal character count is unlimited.
     * <p>
     * If the given width is an empty String and maxLength is greater than 0, the width of the field
     * is inferred by maxLength.
     * <p>
     * The height is only set if the given String is not {@link StringUtils#isEmpty(CharSequence)
     * empty}.
     */
    public static TextArea newTextArea(int maxLength, String width, String height) {
        var textArea = newTextArea();

        if (maxLength > 0) {
            textArea.setMaxLength(maxLength);
        }

        textArea.setWidth(width);

        if (!StringUtils.isEmpty(height)) {
            textArea.setHeight(height);
        }
        return textArea;
    }

    public static <T> ComboBox<T> newComboBox() {
        return new ComboBox<>();
    }

    public static <T> MultiSelectComboBox<T> newMultiSelect() {
        return new MultiSelectComboBox<>();
    }

    public static Checkbox newCheckbox() {
        return new Checkbox();
    }

    /**
     * Creates a new {@link CheckboxGroup} with the given {@link ItemCaptionProvider} in the given
     * {@link AlignmentType}.
     */
    public static CheckboxGroup<?> newCheckboxGroup(
            Supplier<Class<? extends ItemCaptionProvider<?>>> itemCaptionsProvider,
            AlignmentType alignmentType) {

        CheckboxGroup<?> checkboxes = new CheckboxGroup<>();
        checkboxes.setRenderer(new TextRenderer<>(
                ItemCaptionProvider.instantiate(itemCaptionsProvider)::getUnsafeCaption));

        if (alignmentType.equals(AlignmentType.VERTICAL)) {
            checkboxes.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        }
        return checkboxes;
    }

    /**
     * Creates a new {@link RadioButtonGroup} with the given {@link ItemCaptionProvider} in the
     * given {@link AlignmentType}.
     */
    public static RadioButtonGroup<?> newRadioButtonGroup(
            Supplier<Class<? extends ItemCaptionProvider<?>>> itemCaptionsProvider,
            AlignmentType alignmentType) {

        RadioButtonGroup<?> radioButtons = new RadioButtonGroup<>();
        radioButtons.setRenderer(new TextRenderer<>(
                ItemCaptionProvider.instantiate(itemCaptionsProvider)::getUnsafeCaption));

        if (alignmentType.equals(AlignmentType.VERTICAL)) {
            radioButtons.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        }
        return radioButtons;
    }

    public static Button newButton() {
        return new Button();
    }

    /**
     * Creates a new default {@link DatePicker} with {@link DatePicker#setAutoOpen(boolean)} set to
     * <code>false</code> and autoselect feature to <code>true</code>
     */
    public static DatePicker newDateField() {
        return newDateField(false, true);
    }

    /**
     * Creates a {@link DatePicker} with the given options
     *
     * @param autoOpen If <code>true</code>, the dropdown will open when the field is clicked.
     * @param autoselect If <code>true</code>, the date value will be selected when the field is
     *            focused.
     */
    public static DatePicker newDateField(boolean autoOpen, boolean autoselect) {
        if (UI.getCurrent() == null || UI.getCurrent().getLocale() == null) {
            throw new IllegalStateException("Creating a date field requires a UI with locale");
        }
        DatePicker field = new DatePicker();
        field.setI18n(DatePickerI18nCreator.createI18n(UI.getCurrent().getLocale()));
        // there is no year zero https://en.wikipedia.org/wiki/Year_zero
        // DatePicker gets confused with year numbers below 1000 anyway
        field.setMin(LocalDate.ofYearDay(1000, 1));
        field.setMax(LocalDate.ofYearDay(9999, 365));
        field.setAutoOpen(autoOpen);
        field.getElement().setProperty(AUTOSELECT, autoselect);
        return field;
    }

    /**
     * Creates a new default {@link DateTimePicker} with the given step,
     * {@link DateTimePicker#setAutoOpen(boolean)} set to <code>false</code> and the autoselect
     * feature to <code>true</code>
     *
     * @param step the time interval, in minutes, between the items displayed in the time picker
     *            overlay
     */
    public static DateTimePicker newDateTimeField(long step) {
        return newDateTimeField(step, false, true);
    }

    /**
     * Creates a {@link DateTimePicker} with the given options
     *
     * @param step The time interval, in minutes, between the items displayed in the time picker
     *            overlay
     * @param autoOpen If <code>true</code>, the dropdown will open when the field is clicked.
     * @param autoselect If <code>true</code>, the date value will be selected when the field is
     *            focused.
     */
    public static DateTimePicker newDateTimeField(long step, boolean autoOpen, boolean autoselect) {
        if (UI.getCurrent() == null || UI.getCurrent().getLocale() == null) {
            throw new IllegalStateException("Creating a datetime field requires a UI with locale");
        }
        DateTimePicker field = new DateTimePicker();
        field.setDatePickerI18n(DatePickerI18nCreator.createI18n(UI.getCurrent().getLocale()));
        // there is no year zero https://en.wikipedia.org/wiki/Year_zero
        // DateTimePicker gets confused with year numbers below 1000 anyway
        field.setMin(LocalDateTime.of(LocalDate.ofYearDay(1000, 1), LocalTime.of(0, 0)));
        field.setMax(LocalDateTime.of(LocalDate.ofYearDay(9999, 365), LocalTime.of(23, 59, 59)));
        field.setStep(Duration.ofMinutes(step));
        field.setAutoOpen(autoOpen);
        field.getElement().setProperty(AUTOSELECT, autoselect);
        return field;
    }

    /**
     * Creates a new default {@link TimePicker} with the given step,
     * {@link TimePicker#setAutoOpen(boolean)} set to <code>false</code> and the autoselect feature
     * to <code>true</code>.
     *
     * @param step the {@link Duration time interval} between the items displayed in the time picker
     *            overlay
     * @param precision the precision of the interval (e.g., minutes or seconds)
     */
    public static TimePicker newTimeField(long step, ChronoUnit precision) {
        return newTimeField(step, precision, false, true);
    }

    /**
     * Creates a {@link TimePicker} with the given options.
     *
     * @param step The {@link Duration time interval} between the items displayed in the time picker
     *            overlay
     * @param precision the precision of the interval (e.g., minutes or seconds)
     * @param autoOpen If <code>true</code>, the dropdown will open when the field is clicked.
     * @param autoselect If <code>true</code>, the time value will be selected when the field is
     *            focused.
     */
    public static TimePicker newTimeField(long step, ChronoUnit precision, boolean autoOpen, boolean autoselect) {
        if (UI.getCurrent() == null || UI.getCurrent().getLocale() == null) {
            throw new IllegalStateException("Creating a datetime field requires a UI with locale");
        }
        var field = new TimePicker();
        field.setMin(LocalTime.of(0, 0));
        field.setMax(LocalTime.of(23, 59, 59));
        field.setStep(getStepDuration(step, precision));
        field.setAutoOpen(autoOpen);
        field.getElement().setProperty(AUTOSELECT, autoselect);
        return field;
    }

    public static Button newButton(Icon icon, Collection<String> styleNames) {
        Button button = new Button(icon);
        button.setTabIndex(-1);
        styleNames.forEach(button::addClassName);
        return button;
    }

    /**
     * Creates a plain {@link VerticalLayout} without spacing or margin.
     */
    public static VerticalLayout newPlainVerticalLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setMargin(false);
        return layout;
    }

    /**
     * Converts the step and precision to a {@link Duration}.
     *
     * @param step the step size
     * @param precision the precision of the step size
     *
     * @return the Duration based on the step and precision
     */
    private static Duration getStepDuration(long step, ChronoUnit precision) {
        return switch (precision) {
            case SECONDS -> Duration.ofSeconds(step);
            case MINUTES -> Duration.ofMinutes(step);
            default -> throw new IllegalArgumentException("Unsupported ChronoUnit for step size");
        };
    }
}
