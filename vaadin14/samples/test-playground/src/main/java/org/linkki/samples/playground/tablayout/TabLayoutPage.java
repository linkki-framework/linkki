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

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class TabLayoutPage extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public static final String HORIZONTAL_TAB_1 = "horizontalTab1";
    public static final String HORIZONTAL_TAB_2 = "horizontalTab2";
    public static final String HORIZONTAL_CONTENT_1 = "horizontalContent1";
    public static final String HORIZONTAL_CONTENT_2 = "horizontalContent2";

    public static final String VERTICAL_TAB_1 = "verticalTab1";
    public static final String VERTICAL_TAB_2 = "verticalTab2";

    public TabLayoutPage() {
        setPadding(false);
        LinkkiTabLayout horizontalSheet = new LinkkiTabLayout(Orientation.HORIZONTAL);

        Span horizontalContent1 = new Span("This is a horizontal LinkkiTabLayout.");
        horizontalContent1.setId(HORIZONTAL_CONTENT_1);
        horizontalSheet.addTab(HORIZONTAL_TAB_1, "Horizontal", horizontalContent1);
        Span horizontalContent2 = new Span("Captions can contain icons and other components.");
        horizontalContent2.setId(HORIZONTAL_CONTENT_2);
        horizontalSheet.addTab(HORIZONTAL_TAB_2, VaadinIcon.PLUS.create(), horizontalContent2);

        LinkkiTabLayout verticalSheet = new LinkkiTabLayout(Orientation.VERTICAL);
        verticalSheet.addTab(VERTICAL_TAB_1, "Vertical", new Span("This is a vertical LinkkiTabLayout."));
        verticalSheet.addTab(VERTICAL_TAB_2, VaadinIcon.PICTURE.create(),
                             new Image("https://linkki-framework.org/images/linkki.png", "linkki"));

        add(horizontalSheet);
        add(verticalSheet);
    }

}
