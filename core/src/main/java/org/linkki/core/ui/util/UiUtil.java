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
package org.linkki.core.ui.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

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
        if (ui != null) {
            Locale locale = ui.getLocale();
            if (locale != null) {
                return locale;
            }
        }
        return Locale.GERMAN;
    }

    public static interface TextProvider<T> {
        public String getText(T value);
    }

}
