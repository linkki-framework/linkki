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

package org.linkki.samples.playground.ts.dialogs;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.framework.ui.dialogs.OkCancelDialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

@UICssLayout
public class OkCancelDialogOverflowPmo {

    public static final String SHOW_DIALOG_BUTTON_ID = "openDialog";

    @UIButton(position = 10, caption = "Open Dialog")
    public void openDialog() {
        OkCancelDialog.builder("Overflow Dialog")
                .content(createOverflowingComponent())
                .build()
                .open();
    }

    private Component createOverflowingComponent() {
        Div div = new Div();
        div.setWidth("200px");
        div.setHeight("150vh");
        div.getStyle().setBackground("#eee");
        div.getStyle().set("flex-shrink", "0");
        return div;
    }
}
