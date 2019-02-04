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

package org.linkki.core.ui;

import java.util.Locale;
import java.util.stream.Stream;

import org.linkki.core.ui.components.ComponentWrapperFactory;
import org.linkki.util.StreamUtil;

public class TestUiFramework implements UiFrameworkExtension {

    private Locale uiLocale = Locale.GERMAN;
    private TestComponentWrapperFactory componentWrapperFactory = TestComponentWrapperFactory.INSTANCE;

    public static TestUiFramework get() {
        return (TestUiFramework)UiFramework.get();
    }

    @Override
    public Locale getLocale() {
        return uiLocale;
    }

    public void setUiLocale(Locale uiLocale) {
        this.uiLocale = uiLocale;
    }

    @Override
    public ComponentWrapperFactory getComponentWrapperFactory() {
        return componentWrapperFactory;
    }

    public void setComponentWrapperFactory(TestComponentWrapperFactory componentWrapperFactory) {
        this.componentWrapperFactory = componentWrapperFactory;
    }

    @Override
    public Stream<?> getChildComponents(Object uiComponent) {
        if (uiComponent instanceof Iterable<?>) {
            return StreamUtil.stream((Iterable<?>)uiComponent);
        } else {
            return Stream.empty();
        }
    }
}
