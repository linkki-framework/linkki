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

package org.linkki.samples.playground.tablayout;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class TabLayoutPage extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public static final String HORIZONTAL_TAB_1_ID = "horizontalTab1";
    public static final String HORIZONTAL_TAB_2_ID = "horizontalTab2";
    public static final String HORIZONTAL_CONTENT_1_ID = "horizontalContent1";
    public static final String HORIZONTAL_CONTENT_2_ID = "horizontalContent2";

    public static final String VERTICAL_TAB_1_ID = "verticalTab1";
    public static final String VERTICAL_TAB_2_ID = "verticalTab2";

    public TabLayoutPage() {
        setPadding(false);
        LinkkiTabLayout horizontalSheet = new LinkkiTabLayout(Orientation.HORIZONTAL);

        Span horizontalContent1 = new Span("This is a horizontal LinkkiTabLayout.");
        horizontalContent1.setId(HORIZONTAL_CONTENT_1_ID);
        horizontalSheet
                .addTab(new LinkkiTabSheet(HORIZONTAL_TAB_1_ID, "Horizontal", "Horizontal tab with string caption",
                        () -> horizontalContent1));
        Span horizontalContent2 = new Span("Captions can contain icons and other components.");
        horizontalContent2.setId(HORIZONTAL_CONTENT_2_ID);
        horizontalSheet.addTab(new LinkkiTabSheet(HORIZONTAL_TAB_2_ID, VaadinIcon.PLUS.create(),
                "Horizontal tab with component caption", () -> horizontalContent2));

        LinkkiTabLayout verticalSheet = new LinkkiTabLayout(Orientation.VERTICAL);
        verticalSheet.addTab(new LinkkiTabSheet(VERTICAL_TAB_1_ID, "Vertical", "Vertical tab with string caption",
                () -> new Span("This is a vertical LinkkiTabLayout.")));
        verticalSheet.addTab(new LinkkiTabSheet(VERTICAL_TAB_2_ID, VaadinIcon.PICTURE.create(),
                "Vertical tab with component caption",
                () -> new Image("https://linkki-framework.org/images/linkki.png", "linkki logo")));

        add(horizontalSheet);
        add(verticalSheet);
    }

}
