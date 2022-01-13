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

package org.linkki.samples.playground.ts.tablayout;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class TabLayoutVisibilityComponent extends Div {

    private static final long serialVersionUID = 1L;


    public TabLayoutVisibilityComponent() {
        Checkbox visible1 = new Checkbox("Tab 1 visible", true);
        add(visible1);
        Checkbox visible2 = new Checkbox("Tab 2 visible", true);
        add(visible2);
        Checkbox visible3 = new Checkbox("Tab 3 visible", true);
        add(visible3);
        Button updateButton = new Button("Update");
        add(updateButton);

        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.VERTICAL);
        tabLayout.getStyle().set("margin-top", "25px");

        tabLayout.addTabSheet(LinkkiTabSheet.builder("tab1")
                .caption("Tab 1")
                .content(() -> createSpan("content1", "Tab 1"))
                .visibleWhen(visible1::getValue)
                .build());
        tabLayout.addTabSheet(LinkkiTabSheet.builder("tab2")
                .caption("Tab 2")
                .content(() -> createSpan("content2", "Tab 2"))
                .visibleWhen(visible2::getValue)
                .build());
        tabLayout.addTabSheet(LinkkiTabSheet.builder("tab3")
                .caption("Tab 3")
                .content(() -> createSpan("content3", "Tab 3"))
                .visibleWhen(visible3::getValue)
                .build());

        add(tabLayout);
        updateButton.addClickListener(event -> tabLayout.updateSheetVisibility());
    }

    private Span createSpan(String id, String text) {
        Span span = new Span(text);
        span.setId(id);
        return span;
    }
}
