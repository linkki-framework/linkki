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

package org.linkki.samples.playground.ts;

import java.io.Serial;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Simple wrapper for a test section containing a title, a description, a list of aspects that are
 * covered by this section and the content with the UI elements to be tested.
 */
@Tag("test-case-component")
@CssImport(value = "./styles/test-case-component.css")
public class TestCaseComponent extends Div {

    @Serial
    private static final long serialVersionUID = 1L;

    public TestCaseComponent(String sceneId, String testCaseId, Object pmo) {
        this(getTestId(sceneId, testCaseId),
                VaadinUiCreator.createComponent(pmo, new BindingContext(pmo.getClass().getSimpleName())));
    }

    public TestCaseComponent(String sceneId, String testCaseId, Component component) {
        this(getTestId(sceneId, testCaseId), component);
    }

    public TestCaseComponent(String testId, Component content) {
        setId(testId);

        add(new Span(TestCatalog.getCaseTitle(testId)));

        VerticalLayout description = new VerticalLayout();
        description.setSpacing(false);
        description.getElement().setAttribute("part", "description");
        description.add(new Span(TestCatalog.getCaseDescription(testId)));

        UnorderedList aspectsList = new UnorderedList();
        Arrays.asList(TestCatalog.getCaseItems(testId)//
                .split("\n"))
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

    public static String getTestId(String sceneId, String testCaseId) {
        return sceneId + "." + testCaseId;
    }
}