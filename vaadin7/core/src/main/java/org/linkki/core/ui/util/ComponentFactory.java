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
package org.linkki.core.ui.util;

import static org.linkki.core.defaults.style.LinkkiTheme.HORIZONTAL_SPACER;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;

import org.linkki.core.ui.components.DoubleField;
import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.components.SubsetChooser;

import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontIcon;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ComponentFactory {

    /**
     * @see <a href="https://en.wikipedia.org/wiki/Non-breaking_space">Non-breaking_space</a>
     */
    public static final String NO_BREAK_SPACE = "&nbsp";

    private ComponentFactory() {
    }

    /**
     * Creates a non-breaking space that keeps a horizontal space.
     * <p>
     * If you just want to skip one cell in a grid layout do not use this method but simply use
     * {@link GridLayout#space()}.
     * 
     * @param layout the layout where the spacer should be added.
     * @return the Label that is used as spacer component
     */
    public static Label addHorizontalSpacer(AbstractLayout layout) {
        if (layout instanceof AbstractOrderedLayout) {
            return addHorizontalFixedSizeSpacer((AbstractOrderedLayout)layout, -1);
        } else {
            Label spacer = createSpacer();
            layout.addComponent(spacer);
            return spacer;

        }
    }

    /**
     * Creates a non-breaking space that keeps a horizontal space. The width of the spacer is specified
     * in px.
     * 
     * @param layout the layout where the spacer should be added.
     * @return the Label that is used as spacer component
     */
    public static Label addHorizontalFixedSizeSpacer(AbstractOrderedLayout layout, int px) {
        Label spacer = createSpacer();
        if (px > 0) {
            spacer.setWidth("" + px + "px");
        }
        layout.addComponent(spacer);
        layout.setExpandRatio(spacer, 0);
        return spacer;
    }

    private static Label createSpacer() {
        Label spacer = new Label(NO_BREAK_SPACE, ContentMode.HTML);
        spacer.addStyleName(HORIZONTAL_SPACER);
        return spacer;
    }

    /**
     * Creates a new {@link Label} taking as much space as is required to display it (width=undefined).
     * 
     * @deprecated for removal since 2019-01-10 because it duplicated
     *             {@link #newLabelWidthUndefined(AbstractOrderedLayout, String)}
     */
    @Deprecated
    public static final Label sizedLabel(AbstractOrderedLayout parent, String caption) {
        return newLabelWidthUndefined(parent, caption);
    }

    /**
     * Creates a new {@link Label} taking all available space (width=100%).
     */
    public static final Label newLabelWidth100(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    /**
     * Creates a new {@link Label} taking as much space as is required to display it (width=undefined)
     * using the given {@link ContentMode}.
     * 
     * @deprecated for removal since 2019-01-10 to unify method names. Use
     *             {@link #newLabelWidthUndefined(Layout, String, ContentMode)} instead.
     */
    @Deprecated
    public static final Label sizedLabel(Layout parent, String caption, ContentMode mode) {
        return newLabelWidthUndefined(parent, caption, mode);
    }

    /**
     * Creates a new {@link Label} taking as much space as is required to display it (width=undefined)
     * using the given {@link ContentMode}.
     */
    public static final Label newLabelWidthUndefined(Layout parent, String caption, ContentMode mode) {
        Label l = new Label(caption, mode);
        l.setWidthUndefined();
        parent.addComponent(l);
        return l;
    }

    /**
     * Creates a new {@link Label} taking as much space as is required to display it (width=undefined).
     */
    public static final Label newLabelWidthUndefined(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        l.setWidthUndefined();
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    public static Label labelIcon(AbstractOrderedLayout parent, FontIcon icon) {
        Label l = newLabelWidthUndefined(parent, "");
        l.setContentMode(ContentMode.HTML);
        l.setValue(icon.getHtml());
        return l;
    }

    public static final Label newEmptyLabel(AbstractOrderedLayout layout) {
        Label l = new Label(NO_BREAK_SPACE, ContentMode.HTML);
        layout.addComponent(l);
        return l;
    }

    public static TextField newTextfield() {
        TextField tf = new TextField();
        tf.setNullRepresentation("");
        tf.setNullSettingAllowed(true);
        return tf;
    }

    public static TextArea newTextArea() {
        TextArea ta = new TextArea();
        ta.setNullRepresentation("");
        ta.setNullSettingAllowed(true);
        return ta;
    }

    public static TextField newReadOnlyTextFieldFixedWidth(String value) {
        return newReadOnlyTextField(value, value.length());
    }

    public static TextField newReadOnlyTextField(String value, int columns) {
        TextField field = newReadOnlyTextField(value);
        field.setColumns(columns);
        return field;
    }

    public static TextField newReadOnlyTextField100PctWidth(String value) {
        TextField field = newReadOnlyTextField(value);
        field.setWidth("100%");
        return field;
    }

    public static TextField newReadOnlyTextField(String value) {
        TextField field = newTextfield();
        field.setValue(value);
        field.setReadOnly(true);
        return field;
    }

    public static LinkkiComboBox newComboBox() {
        LinkkiComboBox linkkiComboBox = new LinkkiComboBox();
        linkkiComboBox.setFilteringMode(FilteringMode.CONTAINS);
        return linkkiComboBox;
    }

    public static CheckBox newCheckBox() {
        return new CheckBox();
    }

    public static Button newButton() {
        return new Button();
    }

    public static DateField newDateField() {
        DateField field = new DateField();
        field.setValidationVisible(true);
        field.addValidator(new DateValidator());
        // converter is not set here but found automatically in
        // com.vaadin.ui.AbstractField.setPropertyDataSource(Property) from the
        // org.linkki.core.ui.converters.LinkkiConverterFactory
        return field;
    }

    public static IntegerField newIntegerField(Locale locale) {
        return new IntegerField(locale);
    }

    public static DoubleField newDoubleField(Locale locale) {
        return new DoubleField(locale);
    }

    public static Button newButton(Resource icon, Collection<String> styleNames) {
        Button button = new Button(icon);
        button.setTabIndex(-1);
        styleNames.forEach(style -> button.addStyleName(style));
        return button;
    }

    public static TwinColSelect newTwinColSelect() {
        return new TwinColSelect();
    }

    public static SubsetChooser newSubsetChooser() {
        return new SubsetChooser();
    }

    /**
     * Manual validation as DateField's setRangeStart() setRangeEnd() methods don't work properly. As
     * the field uses converters, the converted value type (LocalDate) is expected and not Date.
     */
    private static final class DateValidator implements Validator {
        private static final long serialVersionUID = 1L;

        @Override
        public void validate(@CheckForNull Object value) throws InvalidValueException {
            if (value instanceof LocalDate) {
                validateDate((LocalDate)value);
            }
        }

        private void validateDate(LocalDate date) {
            if (date.getYear() < 0 || date.getYear() > 9999) {
                throw new InvalidValueException("Jahreszahlen müssen zwischen 0 und 9999 liegen.");
            }
        }
    }

    static class YesNoToBooleanConverter implements Converter<String, Boolean> {

        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 1L;

        @SuppressFBWarnings("NP_BOOLEAN_RETURN_NULL")
        @Override
        @CheckForNull
        public Boolean convertToModel(@CheckForNull String value,
                @CheckForNull Class<? extends Boolean> targetType,
                @CheckForNull Locale locale)
                throws ConversionException {
            if (value == null) {
                return null;
            }
            if ("Ja".equals(value)) {
                return Boolean.TRUE;
            }
            if ("Nein".equals(value)) {
                return Boolean.FALSE;
            }
            throw new ConversionException();
        }

        @Override
        @CheckForNull
        public String convertToPresentation(@CheckForNull Boolean value,
                @CheckForNull Class<? extends String> targetType,
                @CheckForNull Locale locale)
                throws ConversionException {
            if (value == null) {
                return null;
            } else if (value) {
                return "Ja";
            } else {
                return "Nein";
            }
        }

        @Override
        public Class<Boolean> getModelType() {
            return Boolean.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
    }

}
