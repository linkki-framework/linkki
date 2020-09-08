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

import static org.linkki.core.defaults.style.LinkkiTheme.HORIZONTAL_SPACER;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.pmo.ButtonPmo;

import com.vaadin.server.FontIcon;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

public class ComponentFactory {

    /**
     * @see <a href="https://en.wikipedia.org/wiki/Non-breaking_space">Non-breaking_space</a>
     */
    public static final String NO_BREAK_SPACE = "&nbsp";

    private ComponentFactory() {
        // prevents instantiation
    }

    /**
     * Creates a non-breaking space that keeps a horizontal space.
     * <p>
     * If you just want to skip one cell in a grid layout do not use this method but simply use
     * {@link GridLayout#space()}.
     * 
     * @param layout the layout where the spacer should be added.
     * @return the Label that is used as spacer component
     * 
     * @deprecated never used from linkki internal and will be removed in next version - spacer is bad
     *             style, better style your margin correctly
     */
    @Deprecated
    public static Label addHorizontalSpacer(AbstractLayout layout) {
        if (layout instanceof AbstractOrderedLayout) {
            return addHorizontalFixedSizeSpacer((AbstractOrderedLayout)layout, -1);
        } else {
            Label spacer = horizontalSpacer();
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
     * 
     * @deprecated never used from linkki internal and will be removed in next version - spacer is bad
     *             style, better style your margin correctly
     */
    @Deprecated
    public static Label addHorizontalFixedSizeSpacer(AbstractOrderedLayout layout, int px) {
        Label spacer = horizontalSpacer();
        if (px > 0) {
            spacer.setWidth("" + px + "px");
        }
        layout.addComponent(spacer);
        layout.setExpandRatio(spacer, 0);
        return spacer;
    }

    private static Label horizontalSpacer() {
        Label spacer = new Label(NO_BREAK_SPACE, ContentMode.HTML);
        spacer.addStyleName(HORIZONTAL_SPACER);
        return spacer;
    }

    /**
     * @deprecated use {@link #newLabelFullWidth(String)} and
     *             {@link Layout#addComponent(com.vaadin.ui.Component)} directly.
     */
    @Deprecated
    public static final Label newLabelWidth100(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        l.setWidth("100%");
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    /**
     * @deprecated use {@link #newLabelUndefinedWidth(String, ContentMode)} and
     *             {@link Layout#addComponent(com.vaadin.ui.Component)} directly.
     */
    @Deprecated
    public static final Label sizedLabel(Layout parent, String caption, ContentMode mode) {
        Label l = new Label(caption, mode);
        l.setWidthUndefined();
        parent.addComponent(l);
        return l;
    }

    /**
     * Creates a new label with undefined width in the given parent.
     * 
     * @deprecated use {@link #newLabelUndefinedWidth(String)} and
     *             {@link Layout#addComponent(com.vaadin.ui.Component)} directly.
     */
    @Deprecated
    public static final Label sizedLabel(AbstractOrderedLayout parent, String caption) {
        Label l = new Label(caption);
        l.setWidthUndefined();
        parent.addComponent(l);
        parent.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        return l;
    }

    /**
     * @deprecated use {@link #newLabelUndefinedWidth(String)} and
     *             {@link Layout#addComponent(com.vaadin.ui.Component)} directly.
     */
    @Deprecated
    public static final Label newLabelWidthUndefined(AbstractOrderedLayout parent, String caption) {
        Label label = newLabelUndefinedWidth(caption);
        parent.addComponent(label);
        parent.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        return label;
    }

    /**
     * @deprecated use {@link #newLabelIcon(FontIcon)} and add to parent using
     *             {@link Layout#addComponent(com.vaadin.ui.Component)} instead.
     */
    @Deprecated
    public static Label labelIcon(AbstractOrderedLayout parent, FontIcon icon) {
        Label l = sizedLabel(parent, icon.getHtml());
        l.setContentMode(ContentMode.HTML);
        return l;
    }

    /**
     * @deprecated use {@link #newLabelUndefinedWidth(String, ContentMode)} with {@link #NO_BREAK_SPACE}
     *             and {@link ContentMode#HTML}.
     */
    @Deprecated
    public static final Label newEmptyLabel(AbstractOrderedLayout layout) {
        Label l = newLabelUndefinedWidth(NO_BREAK_SPACE, ContentMode.HTML);
        layout.addComponent(l);
        return l;
    }

    /**
     * Creates a new horizontal line.
     */
    public static Label newHorizontalLine() {
        return newLabelFullWidth("<hr/>", ContentMode.HTML);
    }

    /**
     * Creates a new {@link Label} that only displays an icon.
     */
    public static Label newLabelIcon(FontIcon icon) {
        return newLabelUndefinedWidth(icon.getHtml(), ContentMode.HTML);
    }

    /**
     * Creates a new {@link Label} with full width.
     */
    public static Label newLabelFullWidth(String caption, ContentMode contentMode) {
        Label label = new Label(caption, contentMode);
        label.setWidth("100%");
        return label;
    }

    /**
     * Creates a new text mode {@link Label} with full width.
     */
    public static Label newLabelFullWidth(String caption) {
        Label label = new Label(caption, ContentMode.TEXT);
        label.setWidth("100%");
        return label;
    }

    /**
     * Creates a new {@link Label} with undefined width.
     */
    public static Label newLabelUndefinedWidth(String caption, ContentMode contentMode) {
        Label label = new Label(caption, contentMode);
        return label;
    }

    /**
     * Creates a new {@link Label} with undefined width.
     */
    public static Label newLabelUndefinedWidth(String caption) {
        Label label = new Label(caption, ContentMode.TEXT);
        return label;
    }

    /**
     * Creates a new {@link Link} with full width.
     */
    public static Link newLinkFullWidth(String caption) {
        Link link = new Link();
        link.setWidth("100%");
        link.setCaption(caption);
        return link;
    }

    /**
     * @deprecated the name of the method contains a typo. Use {@link #newTextField()} instead. Will be
     *             removed in the next version.
     */
    @Deprecated
    public static TextField newTextfield() {
        return newTextField();
    }

    public static TextField newTextField() {
        TextField tf = new TextField();
        return tf;
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

    private static void setMaxLengthAndWidth(AbstractTextField field, int maxLength, String width) {
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
     * The number of rows is only set if the given number is greater than 0.
     */
    public static TextArea newTextArea(int maxLength, String width, int rows) {
        TextArea textArea = new TextArea();

        if (maxLength > 0) {
            textArea.setMaxLength(maxLength);
        }

        textArea.setWidth(width);

        if (rows > 0) {
            textArea.setRows(rows);
        }
        return textArea;
    }

    /**
     * @deprecated use {@link #newTextfield()} and {@link TextField#setReadOnly(boolean)} instead
     */
    @Deprecated
    public static TextField newReadOnlyTextFieldFixedWidth(String value) {
        return newReadOnlyTextField(value, value.length());
    }

    /**
     * @deprecated use {@link #newTextfield()} and {@link TextField#setReadOnly(boolean)} instead
     */
    @Deprecated
    public static TextField newReadOnlyTextField(String value, int columns) {
        TextField field = newReadOnlyTextField(value);
        field.setWidth(columns + "em");
        return field;
    }

    /**
     * @deprecated use {@link #newTextfield()} and {@link TextField#setReadOnly(boolean)} instead
     */
    @Deprecated
    public static TextField newReadOnlyTextField100PctWidth(String value) {
        TextField field = newReadOnlyTextField(value);
        field.setWidth("100%");
        return field;
    }

    /**
     * @deprecated use {@link #newTextfield()} and {@link TextField#setReadOnly(boolean)} instead
     */
    @Deprecated
    public static TextField newReadOnlyTextField(String value) {
        TextField field = newTextfield();
        field.setValue(value);
        field.setReadOnly(true);
        return field;
    }

    public static <T> ComboBox<T> newComboBox() {
        ComboBox<T> linkkiComboBox = new ComboBox<>();
        return linkkiComboBox;
    }

    public static CheckBox newCheckBox() {
        return new CheckBox();
    }

    public static Button newButton() {
        return new Button();
    }

    public static DateField newDateField() {
        MultiformatDateField field = new MultiformatDateField();
        field.setRangeStart(LocalDate.ofYearDay(0, 1));
        field.setRangeEnd(LocalDate.ofYearDay(9999, 1));
        return field;
    }

    /**
     * @deprecated use {@link #newTextfield()} instead
     */
    @Deprecated
    public static TextField newIntegerField(@SuppressWarnings("unused") Locale locale) {
        return new TextField();
    }

    /**
     * @deprecated use {@link #newTextfield()} instead
     */
    @Deprecated
    public static TextField newDoubleField(@SuppressWarnings("unused") Locale locale) {
        return new TextField();
    }

    /**
     * Creates a button for the given PMO.
     * 
     * @deprecated This method creates a button and installs a click listener for the callback on
     *             {@link ButtonPmo#onClick()}. This is not the recommended way to handle click events.
     *             Prefer using a binding context! If you cannot use a binding context call
     *             {@link #newButton(Resource, Collection)} and create the listener on your own.
     */
    @Deprecated
    public static Button newButton(ButtonPmo buttonPmo) {
        Button button = new Button((Resource)buttonPmo.getButtonIcon());
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

    public static <T> TwinColSelect<T> newTwinColSelect() {
        return new TwinColSelect<>();
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
