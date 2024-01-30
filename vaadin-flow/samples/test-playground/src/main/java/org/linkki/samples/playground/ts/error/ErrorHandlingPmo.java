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

package org.linkki.samples.playground.ts.error;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.UI;

@UISection
public class ErrorHandlingPmo {

    @UIButton(position = 10, label = "Manually throw navigation exception", caption = "Navigate to a Non-Existent View")
    public void navigateToUnknownView() {
        UI.getCurrent().navigate("<manually navigated to unknown view>");
    }

    @UIButton(position = 20, label = "Manually throw general RuntimeException",
            caption = "Open Custom Error Dialog")
    public void showExceptionDialogWithoutExceptionDetails() {
        throw new RuntimeException("IMPORTANT: This message should not shown in production mode");
    }
}
