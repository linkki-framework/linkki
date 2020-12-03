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

package org.linkki.core.ui.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.mockito.Mockito;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;

/**
 * Utility to mock Vaadin's {@link UI}.
 */
public class MockUi {
    private MockUi() {
        // utility
    }

    /**
     * Creates a deep mock of {@link UI} and sets it as Vaadin's current UI.
     * <p>
     * <em>Remember to call {@link UI#setCurrent(UI) UI#setCurrent(null)} after your tests.</em>
     */
    public static UI mockUi() {
        UI ui = mock(UI.class, Mockito.RETURNS_DEEP_STUBS);
        UI.setCurrent(ui);
        VaadinSession session = mock(VaadinSession.class);
        when(ui.getSession()).thenReturn(session);
        when(session.hasLock()).thenReturn(true);
        return ui;
    }

    /**
     * "Sets" the locale by mocking {@link UI#getLocale()} on the current {@link UI}, which must have
     * been created by a previous call to {@link #mockUi()}.
     */
    public static void setLocale(Locale locale) {
        UI ui = UI.getCurrent();
        when(ui.getLocale()).thenReturn(locale);
    }
}