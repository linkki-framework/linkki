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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class TestScenario {

    private final String scenarioId;
    private final List<LinkkiTabSheet> tabSheets = new ArrayList<>();

    private TestScenario(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public <T> TestScenario testCaseWithValidation(String testCaseId, T pmo, Function<T, MessageList> validatePmo) {
        return testCase(testCaseId, () -> {
            var bindingContext = new DefaultBindingManager(() -> validatePmo.apply(pmo)).getContext(pmo.getClass());
            return VaadinUiCreator.createComponent(pmo, bindingContext);
        });
    }

    public TestScenario testCase(String testCaseId, Object... pmos) {
        Supplier<Component> componentSupplier = () -> {
            var components = Stream.of(pmos)
                    .map(o -> VaadinUiCreator.createComponent(o, new BindingContext()))
                    .toArray(Component[]::new);
            if (components.length > 1) {
                return new VerticalLayout(components);
            } else {
                return components[0];
            }
        };
        return testCase(testCaseId, componentSupplier);
    }

    public TestScenario testCase(String testCaseId, Supplier<Component> componentSupplier) {
        tabSheets.add(LinkkiTabSheet.builder(testCaseId)
                .caption(createTestCaseCaption(testCaseId, TestCatalog.getCaseTitle(scenarioId, testCaseId)))
                .content(() -> new TestCaseComponent(scenarioId, testCaseId, componentSupplier.get()))
                .build());
        return this;
    }

    public LinkkiTabSheet createTabSheet() {
        return LinkkiTabSheet.builder(scenarioId)
                .caption(createTestScenarioCaption(TestCatalog.getScenarioTitle(scenarioId)))
                .description(TestCatalog.getScenarioTitle(scenarioId))
                .content(this::createTestCaseSelector)
                .build();
    }

    private Component createTestScenarioCaption(String subtitle) {
        return createCaptionComponent(scenarioId, null, subtitle);
    }

    private Component createTestCaseCaption(String tcId, String subtitle) {
        return createCaptionComponent(scenarioId, tcId, subtitle);
    }

    private Component createCaptionComponent(String tsId, String tcId, String subtitle) {
        Anchor captionComponent = new Anchor(TestScenarioView.getLocation(tsId, tcId));
        captionComponent.addClassNames("flex", "flex-col", "items-start");

        Component titleLink = new Span(tcId == null ? tsId : tcId);
        captionComponent.add(titleLink);

        Component subtitleLink = new Span(subtitle);
        subtitleLink.getElement().getStyle().set("font-size", "70%");

        captionComponent.add(subtitleLink);

        return captionComponent;
    }

    private LinkkiTabLayout createTestCaseSelector() {
        LinkkiTabLayout layout = new LinkkiTabLayout(Orientation.VERTICAL);
        layout.getTabsComponent().setWidth("180px");
        layout.addTabSheets(tabSheets);
        layout.setId("test-case-selector");
        return layout;
    }

    public static TestScenario id(String szenarioId) {
        return new TestScenario(szenarioId);
    }

}