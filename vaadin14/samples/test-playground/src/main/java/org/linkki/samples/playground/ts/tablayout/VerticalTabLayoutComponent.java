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

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class VerticalTabLayoutComponent extends Div {

    private static final long serialVersionUID = 1L;

    public VerticalTabLayoutComponent() {
        LinkkiTabLayout verticalSheet = new LinkkiTabLayout(Orientation.VERTICAL);

        verticalSheet.addTabSheet(LinkkiTabSheet.builder("tab1")
                .caption("Horizontal")
                .description("Horizontal tab with string caption")
                .content(() -> createSpan("content1", "This is a vertical LinkkiTabLayout."))
                .build());
        verticalSheet.addTabSheet(LinkkiTabSheet.builder("tab2")
                .caption(VaadinIcon.PLUS.create())
                .description("Horizontal tab with component caption")
                .content(() -> createSpan("content2", "Captions can contain icons and other components."))
                .build());

        add(verticalSheet);
    }

    private Span createSpan(String id, String text) {
        Span span = new Span(text);
        span.setId(id);
        return span;
    }

}
