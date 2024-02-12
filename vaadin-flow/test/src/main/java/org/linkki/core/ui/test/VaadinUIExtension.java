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

package org.linkki.core.ui.test;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Extension that sets a Vaadin {@link UI} before each test and removing it afterwards.
 * <p>
 * The UI locale can be changed using {@code UI.getCurrent().setLocale(...)}.
 * 
 * @deprecated Use {@link KaribuUIExtension} instead
 * 
 */
@Deprecated(since = "2.4")
public class VaadinUIExtension implements BeforeEachCallback, AfterEachCallback {

    // needs to be a field due to weak reference
    @Nullable
    private UI ui;

    @Override
    public void beforeEach(ExtensionContext context) {
        this.ui = new UI();
        UI.setCurrent(ui);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        UI.setCurrent(null);
        // some tests may set a session
        VaadinSession.setCurrent(null);
    }

}
