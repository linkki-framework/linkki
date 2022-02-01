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
import java.util.function.Supplier;

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
                .caption(createCaptionComponent(testCaseId, TestCatalog.getCaseTitle(scenarioId, testCaseId)))
                .content(() -> new TestCaseComponent(scenarioId, testCaseId, pmo))
                .build());
        return this;
    }

    public TestScenario testCase(String testCaseId, Component component) {
        tabSheets.add(LinkkiTabSheet.builder(testCaseId)
                .caption(createCaptionComponent(testCaseId, TestCatalog.getCaseTitle(scenarioId, testCaseId)))
                .content(() -> new TestCaseComponent(scenarioId, testCaseId, component))
                .build());
        return this;
    }

    public TestScenario testCase(String testCaseId, Supplier<Component> componentSupplier) {
        return testCase(testCaseId, componentSupplier.get());
    }

    public LinkkiTabSheet createTabSheet() {
        return LinkkiTabSheet.builder(scenarioId)
                .caption(createCaptionComponent(scenarioId, TestCatalog.getScenarioTitle(scenarioId)))
                .description(TestCatalog.getScenarioTitle(scenarioId))
                .content(this::createTestCaseSelector)
                .build();
    }

    private Component createCaptionComponent(String caption, String subtitle) {
        VerticalLayout captionComponent = new VerticalLayout();
        captionComponent.setPadding(false);
        captionComponent.setSpacing(false);

        Label titleLabel = new Label(caption);
        captionComponent.add(titleLabel);

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyle().set("font-size", "70%");
        subtitleLabel.getStyle().set("margin", "0");

        captionComponent.add(subtitleLabel);

        return captionComponent;
    }

    private LinkkiTabLayout createTestCaseSelector() {
        LinkkiTabLayout layout = new LinkkiTabLayout(Orientation.HORIZONTAL);
        layout.addTabSheets(tabSheets);
        layout.setId("test-case-selector");
        return layout;
    }

    public static TestScenario id(String szenarioId) {
        return new TestScenario(szenarioId);
    }
}