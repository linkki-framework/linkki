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
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;
import org.linkki.core.vaadin.component.base.LinkkiDatePicker;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class ComponentFactory {

    /**
     * @see <a href="https://en.wikipedia.org/wiki/Non-breaking_space">Non-breaking_space</a>
     */
    public static final String NO_BREAK_SPACE = "&nbsp";

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
     * Creates a new {@link TextField} with an unlimited maximal character count and an undefined width.
     */
    public static TextField newTextField() {
        return new TextField();
    }

    /**
     * Returns a new {@link TextField} with the given max length and width.
     * <p>
     * If the given maxLength is less than or equal to 0, the maximal character count is unlimited.
     * <p>
     * If the given width is not specified and maxLength is greater than 0, the width of the field is
     * inferred by maxLength.
     */
    public static TextField newTextField(int maxLength, String width) {
        TextField field = new TextField();
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
        field.getElement().setProperty("_enabledCharPattern", pattern);
        return field;
    }

    public static TextArea newTextArea() {
        return new TextArea();
    }

    /**
     * Returns a new {@link TextArea} with the given max length, width and number of rows.
     * <p>
     * If the given maxLength is less than or equal to 0, the maximal character count is unlimited.
     * <p>
     * If the given width is an empty String and maxLength is greater than 0, the width of the field is
     * inferred by maxLength.
     * <p>
     * The height is only set if the given String is not {@link StringUtils#isEmpty(CharSequence)
     * empty}.
     */
    public static TextArea newTextArea(int maxLength, String width, String height) {
        TextArea textArea = new TextArea();

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
        ComboBox<T> linkkiComboBox = new ComboBox<>();
        return linkkiComboBox;
    }

    public static Checkbox newCheckbox() {
        return new Checkbox();
    }

    public static Button newButton() {
        return new Button();
    }

    public static LinkkiDatePicker newDateField() {
        if (UI.getCurrent() == null || UI.getCurrent().getLocale() == null) {
            throw new IllegalStateException("Creating a date field requires a UI with locale");
        }
        LinkkiDatePicker field = new LinkkiDatePicker();
        // there is no year zero https://en.wikipedia.org/wiki/Year_zero
        // DatePicker gets confused with year numbers below 1000 anyway
        field.setMin(LocalDate.ofYearDay(1000, 1));
        field.setMax(LocalDate.ofYearDay(9999, 365));
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


}
