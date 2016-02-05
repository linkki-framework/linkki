package org.linkki.core.ui.util;

import static org.linkki.core.ui.application.ApplicationStyles.HORIZONTAL_SPACER;

import java.util.Collection;
import java.util.Locale;

import org.linkki.core.ButtonPmo;
import org.linkki.core.ui.components.DoubleField;
import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.components.SubsetChooser;
import org.linkki.core.ui.converters.LocalDateToDateConverter;

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
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ComponentFactory {

    /**
     * @see <a href="http://de.wikipedia.org/wiki/Gesch%C3%BCtztes_Leerzeichen">http://de.wikipedia.
     *      org/wiki/Geschütztes_Leerzeichen</a>
     */
    public static final String NO_BREAK_SPACE = "&nbsp";

    private ComponentFactory() {
    }

    public static Label addHorizontalSpacer(AbstractLayout layout) {
        Label spacer = createSpacer();
        layout.addComponent(spacer);
        return spacer;
    }

    public static Label addHorizontalFixedSizeSpacer(AbstractOrderedLayout parent, int px) {
        Label spacer = createSpacer();
        spacer.setWidth("" + px + "px");
        parent.addComponent(spacer);
        parent.setExpandRatio(spacer, 0);
        return spacer;
    }

    private static Label createSpacer() {
        Label spacer = new Label(NO_BREAK_SPACE, ContentMode.HTML);
        spacer.addStyleName(HORIZONTAL_SPACER);
        return spacer;
    }

    /**
     * Erzeugt einen neuen Label der soviel Platz einnimmt, wie er zur Anzeige benötigt
     * (width=undefined).
     */
    public static final Label sizedLabel(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        l.setWidthUndefined();
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    /**
     * Erzeugt einen neuen Label der soviel Platz einnimmt, wie er zur Anzeige benötigt
     * (width=undefined).
     */
    public static final Label newLabelWidth100(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    /**
     * Erzeugt einen neuen Label der soviel Platz einnimmt, wie er zur Anzeige benötigt
     * (width=undefined).
     */
    public static final Label sizedLabel(Layout parent, String caption, ContentMode mode) {
        Label l = new Label(caption, mode);
        l.setWidthUndefined();
        parent.addComponent(l);
        return l;
    }

    /**
     * Erzeugt einen neuen Label der soviel Platz einnimmt, wie er zur Anzeige benötigt
     * (width=undefined).
     */
    public static final Label newLabelWidthUndefined(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        l.setWidthUndefined();
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    public static Label labelIcon(AbstractOrderedLayout parent, FontIcon icon) {
        Label l = sizedLabel(parent, "");
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
        field.setConverter(new LocalDateToDateConverter());
        return field;
    }

    public static IntegerField newIntegerField(Locale locale) {
        return new IntegerField(locale);
    }

    public static DoubleField newDoubleField(Locale locale) {
        return new DoubleField(locale);
    }

    /**
     * Creates a button for the given PMO.
     * 
     * @deprecated This method creates a button and installs a click listener for the callback on
     *             {@link ButtonPmo#onClick()}. This is not the recommended way to handle click
     *             events. Prefer using a binding context! If you cannot use a binding context call
     *             {@link #newButton(Resource, Collection)} and create the listener on your own.
     */
    @Deprecated
    public static Button newButton(ButtonPmo buttonPmo) {
        Button button = new Button(buttonPmo.getButtonIcon());
        button.setTabIndex(-1);
        buttonPmo.getStyleNames().forEach(style -> button.addStyleName(style));
        button.addClickListener(e -> buttonPmo.onClick());
        return button;
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

    static class YesNoToBooleanConverter implements Converter<Object, Boolean> {

        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 1L;

        @SuppressFBWarnings("NP_BOOLEAN_RETURN_NULL")
        @Override
        public Boolean convertToModel(Object value, Class<? extends Boolean> targetType, Locale locale)
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
        public Object convertToPresentation(Boolean value, Class<? extends Object> targetType, Locale locale)
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
        public Class<Object> getPresentationType() {
            return Object.class;
        }
    }

}
