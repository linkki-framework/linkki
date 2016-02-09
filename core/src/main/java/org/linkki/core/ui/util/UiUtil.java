package org.linkki.core.ui.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public class UiUtil {

    private UiUtil() {
        // do not instatiate
    }

    @SuppressWarnings("rawtypes")
    public static final void fillSelectWithItems(AbstractSelect select, Class<? extends Enum> enumeration) {

        Object[] values = enumeration.getEnumConstants();
        fillSelectWithItems(select, Arrays.asList(values));
    }

    public static final void fillSelectWithItems(AbstractSelect select, Collection<?> values) {
        select.addItems(values);
    }

    public static final <T> void fillSelectWithItems(AbstractSelect select,
            Collection<T> values,
            TextProvider<T> textProvider) {
        fillSelectWithItems(select, values);
        for (T value : values) {
            String text = textProvider.getText(value);
            select.setItemCaption(value, text);
        }
    }

    public static boolean isWidth100Pct(Component component) {
        return component.getWidth() == 100.0f && component.getWidthUnits() == Unit.PERCENTAGE;
    }

    /** Returns the locale for the current UI. */
    public static Locale getUiLocale() {
        UI ui = UI.getCurrent();
        return Optional.ofNullable(ui).map(UI::getLocale).orElse(Locale.GERMAN);
    }

    public static interface TextProvider<T> {
        public String getText(T value);
    }

}
