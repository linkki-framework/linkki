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

package org.linkki.samples.playground.bugs.lin4803;

import org.linkki.framework.ui.dialogs.OkCancelDialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DialogMinWidthBug extends VerticalLayout {

    public static final String LIN_4803 = "LIN-4803";
    public static final String CAPTION = LIN_4803 + " :: Dialog min-width not applied";

    private static final long serialVersionUID = 1L;

    public DialogMinWidthBug() {
        add(new H4(CAPTION));
        add(new Div(
                """
                        Dialogs should have a min-width of 400px, but since the migration to Vaadin 25 \
                        the overlay div no longer gets this style applied. \
                        The dialog below should be at least 400px wide."""));

        add(new Button("Open Dialog", e -> openDialog()));
    }

    private void openDialog() {
        OkCancelDialog.builder("Small Dialog")
                .content(new Span("Short"))
                .build()
                .open();
    }
}
