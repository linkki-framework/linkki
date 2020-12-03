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

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.data.binder.HasItems;

public class UiUtil {

    private UiUtil() {
        // do not instatiate
    }

    @SuppressWarnings("rawtypes")
    public static final <T extends Enum> void fillSelectWithItems(HasItems<T> select, Class<T> enumeration) {

        T[] values = enumeration.getEnumConstants();
        fillSelectWithItems(select, Arrays.asList(values));
    }

    public static final <T> void fillSelectWithItems(HasItems<T> select, Collection<T> values) {
        select.setItems(values);
    }

    public static boolean isWidth100Pct(HasSize component) {
        return component.getWidth().equals("100%") || component.getWidth().equals("100 %");
    }

}
