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

package org.linkki.core.vaadin.component.client;

import java.util.Arrays;

import org.linkki.core.vaadin.component.MultiformatDateField;
import org.linkki.core.vaadin.component.shared.MultiformatDateFieldState;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.datefield.PopupDateFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Client side connector for {@link MultiformatDateField}.
 */
@Connect(MultiformatDateField.class)
public class MultiformatDateFieldConnector extends PopupDateFieldConnector {

    private static final long serialVersionUID = 1L;

    @Override
    public MultiformatDateFieldWidget getWidget() {
        return (MultiformatDateFieldWidget)super.getWidget();
    }

    @Override
    public MultiformatDateFieldState getState() {
        return (MultiformatDateFieldState)super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (!Arrays.equals(getWidget().getAlternativeFormats(), getState().getAlternativeFormats())) {
            getWidget().setAlternativeFormats(getState().getAlternativeFormats());
        }
    }
}
