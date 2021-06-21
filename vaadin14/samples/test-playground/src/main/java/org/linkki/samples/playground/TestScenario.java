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

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class TestScenario {

    private String scenarioId;
    private List<LinkkiTabSheet> tabSheets = new ArrayList<>();

    private TestScenario(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public TestScenario testCase(String testCaseId, Object pmo) {
        tabSheets.add(LinkkiTabSheet.builder(testCaseId)
                .content(new TestCaseComponent(scenarioId, testCaseId, pmo))
                .build());
        return this;
    }

    public TestScenario testCase(String testCaseId, Component component) {
        tabSheets.add(LinkkiTabSheet.builder(testCaseId)
                .content(new TestCaseComponent(scenarioId, testCaseId, component))
                .build());
        return this;
    }

    public LinkkiTabSheet createTabSheet() {
        LinkkiTabLayout content = new LinkkiTabLayout(Orientation.HORIZONTAL);
        content.addTabSheets(tabSheets);
        content.setId("test-case-selector");

        VerticalLayout captionComponent = new VerticalLayout();
        captionComponent.setPadding(false);

        Label titleLabel = new Label(scenarioId);
        captionComponent.add(titleLabel);

        Label subtitleLabel = new Label(TestCatalog.getScenarioTitle(scenarioId));
        subtitleLabel.getStyle().set("font-size", "70%");
        subtitleLabel.getStyle().set("margin", "0");

        captionComponent.add(subtitleLabel);

        return LinkkiTabSheet.builder(scenarioId)
                .caption(captionComponent)
                .description(TestCatalog.getScenarioTitle(scenarioId))
                .content(content)
                .build();
    }

    public static TestScenario id(String szenarioId) {
        return new TestScenario(szenarioId);
    }
}