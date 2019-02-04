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
package org.linkki.core.vaadin7;

import java.util.Locale;
import java.util.stream.Stream;

import org.linkki.core.ui.UiFrameworkExtension;
import org.linkki.core.ui.components.ComponentWrapperFactory;
import org.linkki.core.vaadin7.components.Vaadin7ComponentWrapperFactory;
import org.linkki.util.StreamUtil;

import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

public class Vaadin7 implements UiFrameworkExtension {

    @Override
    public Locale getLocale() {
        UI ui = UI.getCurrent();
        if (ui != null) {
            Locale locale = ui.getLocale();
            if (locale != null) {
                return locale;
            }
        }
        return Locale.GERMAN;
    }

    @Override
    public ComponentWrapperFactory getComponentWrapperFactory() {
        return Vaadin7ComponentWrapperFactory.INSTANCE;
    }

    @Override
    public Stream<?> getChildComponents(Object uiComponent) {
        if (uiComponent instanceof HasComponents) {
            return StreamUtil.stream((HasComponents)uiComponent);
        }
        return Stream.empty();
    }

}
