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

package org.linkki.samples.playground.ts.components.tablayout;

import java.io.Serial;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TabOverviewLayout extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public TabOverviewLayout() {
        setId("tab-overview-layout");
        setSpacing(false);
        getThemeList().add("spacing-l");

        add(addDemoComponent(
                "horizontal-layout-tab",
                "Horizontal Layout",
                "A LinkkiTabLayout with horizontal orientation.",
                "The tab selectors are placed above of the content",
                new HorizontalTabLayoutComponent()));

        add(addDemoComponent(
                "vertical-layout-tab",
                "Vertical Layout",
                "A LinkkiTabLayout with vertical orientation.",
                "The tab selectors are placed on the left of the content",
                new VerticalTabLayoutComponent()));

        add(addDemoComponent(
                "tab-visibility",
                "Tab Visibility",
                "The visibility of the tabs can be set using the checkboxes. To apply the settings, the update button has to be pressed..",
                """
                        Tabs set invisible are hidden
                        Tabs set to visible are shown
                        If the current tab is set invisible, the first visible tab should be selected
                        When all tabs are invisible, all content should be invisible""",
                new TabLayoutVisibilityComponent()));

        add(addDemoComponent(
                "after-tab-selected-observer",
                "AfterTabSelectedOberserver",
                "Component with AfterTabSelectedObserver should update the content upon tab selection.",
                """
                        Change the value in the sheet without observer and switch to the tab with observer. The value should be updated.
                        Change the value in the sheet with observer and switch to the tab without observer. The value should not be updated there.""",
                new AfterTabSelectedComponent()));
    }

    private Component addDemoComponent(String id, String title, String descriptionText, String itemText, Component demoComponent) {
        VerticalLayout demoComponentContainer = new VerticalLayout();

        demoComponentContainer.setId(id);
        demoComponentContainer.add(new H3(title));

        demoComponentContainer.getElement().setAttribute("part", "description");
        demoComponentContainer.add(new Span(descriptionText));

        UnorderedList aspectsList = new UnorderedList();
        Arrays.asList(itemText//
                        .split("\n"))
                .stream()//
                .map(StringUtils::trim)//
                .filter(StringUtils::isNotEmpty)//
                .forEach(a -> aspectsList.add(new ListItem(a)));
        if (aspectsList.getChildren().findAny().isPresent()) {
            demoComponentContainer.add(aspectsList);
        }
        demoComponentContainer.add(demoComponent);
        return demoComponentContainer;
    }
}
