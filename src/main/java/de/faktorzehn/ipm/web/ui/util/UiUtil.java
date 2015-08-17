package de.faktorzehn.ipm.web.ui.util;

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;

import de.faktorzehn.ipm.web.binding.BindingUtils;

public class UiUtil {

    @SuppressWarnings("rawtypes")
    public static final void fillSelectWithItems(AbstractSelect select, Class<? extends Enum> enumeration) {

        Object[] values = enumeration.getEnumConstants();
        fillSelectWithItems(select, Arrays.asList(values));
    }

    public static final void fillSelectWithItems(AbstractSelect select, Collection<?> values) {

        select.addItems(values);
        int maxLength = 0;
        for (Object value : values) {
            String text = BindingUtils.getTextForSelectItem(value);
            select.setItemCaption(value, text);
            if (text.length() > maxLength) {
                maxLength = text.length();
            }
        }
        int width = Math.min(400, 40 + (maxLength * 7));
        select.setWidth("" + width + "px");
    }

    public static final <T> void fillSelectWithItems(AbstractSelect select,
            Collection<T> values,
            TextProvider<T> textProvider) {

        select.addItems(values);
        int maxLength = 0;
        for (T value : values) {
            String text = textProvider.getText(value);
            select.setItemCaption(value, text);
            if (text.length() > maxLength) {
                maxLength = text.length();
            }
        }
        int width = Math.min(400, 40 + (maxLength * 7));
        select.setWidth("" + width + "px");
    }

    public static boolean isWidth100Pct(Component component) {
        return component.getWidth() == 100.0f && component.getWidthUnits() == Unit.PERCENTAGE;
    }

    public static interface TextProvider<T> {

        public String getText(T value);
    }
}
