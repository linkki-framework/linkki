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

package org.linkki.samples.playground;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Simple wrapper for a test section containing a title, a description, a list of aspects that are
 * covered by this section and the content with the ui elements to be tested.
 */
@Tag("testcase-section")
public class TestCaseSection extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public TestCaseSection(String testId, Component content) {
        setId(testId);
        setPadding(false);

        add(new H3(TestCatalog.getTitle(testId)));

        VerticalLayout description = new VerticalLayout();
        description.getStyle().set("background-color", "rgba(200,200,200,0.2)");

        description.add(new Span(TestCatalog.getDescription(testId)));

        UnorderedList aspectsList = new UnorderedList();
        Arrays.asList(TestCatalog.getAspects(testId).split("\n"))
                .stream()//
                .map(StringUtils::trim)//
                .filter(StringUtils::isNotEmpty)//
                .forEach(a -> aspectsList.add(new ListItem(a)));
        if (aspectsList.getChildren().count() > 0) {
            description.add(aspectsList);
        }

        add(description);

        VerticalLayout contentWrapper = new VerticalLayout(content);
        contentWrapper.setId("content-wrapper");
        add(contentWrapper);
    }
}