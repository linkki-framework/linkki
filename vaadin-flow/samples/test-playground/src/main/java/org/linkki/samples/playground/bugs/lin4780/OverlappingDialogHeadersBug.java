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

package org.linkki.samples.playground.bugs.lin4780;

import java.io.Serial;

import org.linkki.framework.ui.dialogs.OkCancelDialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OverlappingDialogHeadersBug extends VerticalLayout {

    public static final String CAPTION = "LIN-4780";
    private static final String CLOSE_BUTTON_TEXT = "Close";

    @Serial
    private static final long serialVersionUID = 1L;

    public OverlappingDialogHeadersBug() {
        add(new H4(CAPTION));
        add(new Div(
                """
                        Dialogs should be rendered on top of each other and must not intersect.
                        This was an issue with Vaadin 25 and resolved with 25.0.7"""));

        add(new Button("Open Dialog", e -> openDialog()));
        add(new Button("Open OkCancelDialog", e -> openOkCancelDialog()));
    }

    private void openDialog() {
        var dialog = new Dialog();
        dialog.getHeader().add(new Span("Header"));
        dialog.add(new Span("Content Dialog 1"));
        dialog.add(new VerticalLayout(
                openNestedDialogButton(),
                openNestedOkCancelDialogButton()));
        dialog.getFooter().add(new Button(CLOSE_BUTTON_TEXT, e3 -> dialog.close()));
        dialog.open();
    }

    private void openOkCancelDialog() {
        OkCancelDialog.builder("Caption")
                .content(new Span("Content Dialog 1"),
                         new VerticalLayout(
                                 openNestedDialogButton(),
                                 openNestedOkCancelDialogButton()))
                .build().open();
    }

    private Button openNestedDialogButton() {
        var nestedDialog = new Dialog();
        nestedDialog.getHeader().add(new Span("Nested Dialog"));
        nestedDialog.add(new Span("Content nested Dialog"));
        nestedDialog.getFooter().add(new Button(CLOSE_BUTTON_TEXT,
                e3 -> nestedDialog.close()));
        nestedDialog.setHeight("90%");
        nestedDialog.setWidth("90%");
        return new Button("Open nested Dialog", e -> nestedDialog.open());
    }

    private Button openNestedOkCancelDialogButton() {
        return new Button("Open nested OkCancelDialog",
                e4 -> OkCancelDialog.builder("Nested OkCancelDialog")
                        .content(new Span(
                                "Content nested OkCancelDialog"))
                        .build()
                        .open()
                        .setSize("90%", "90%"));
    }
}
