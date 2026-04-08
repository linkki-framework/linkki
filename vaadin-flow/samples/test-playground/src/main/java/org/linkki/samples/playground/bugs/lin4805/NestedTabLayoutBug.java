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

package org.linkki.samples.playground.bugs.lin4805;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class NestedTabLayoutBug extends VerticalLayout {

    public static final String LIN_4805 = "LIN-4805";
    public static final String CAPTION = LIN_4805 + " :: Nested tab layout padding";

    public static final String NESTED_TAB_ID = "nested-tab-1";

    private static final long serialVersionUID = 1L;

    public NestedTabLayoutBug() {
        add(new H4(CAPTION));
        add(new Div("""
                A horizontal LinkkiTabLayout nested inside a vertical one should not receive the \
                vertical tab padding."""));

        var horizontalTabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);
        horizontalTabLayout.addTabSheets(
                                         LinkkiTabSheet.builder(NESTED_TAB_ID)
                                                 .caption("Nested Tab 1")
                                                 .content(() -> new Div("Content of nested tab 1"))
                                                 .build(),
                                         LinkkiTabSheet.builder("nested-tab-2")
                                                 .caption("Nested Tab 2")
                                                 .content(() -> new Div("Content of nested tab 2"))
                                                 .build());

        add(horizontalTabLayout);
        setSizeFull();
    }
}
