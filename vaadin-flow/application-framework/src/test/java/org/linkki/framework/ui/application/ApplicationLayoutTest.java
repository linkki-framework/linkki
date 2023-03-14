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

package org.linkki.framework.ui.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.isA;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.Sequence;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

class ApplicationLayoutTest {

    @BeforeEach
    void initCurrentUi() {
        MockVaadin.setup();
    }

    @AfterEach
    void cleanUpCurrentUi() {
        MockVaadin.tearDown();
    }

    @Test
    void testApplicationLayout() {
        ApplicationLayout applicationLayout = new ApplicationLayout(new MinimalApplicationConfig()) {
            private static final long serialVersionUID = 1L;
        };

        List<Component> children = applicationLayout.getChildren().collect(Collectors.toList());
        assertThat(children, contains(isA(ApplicationHeader.class), isA(VerticalLayout.class)));
    }

    @Test
    void testApplicationLayout_WithFooter() {
        ApplicationLayout applicationLayout = new ApplicationLayout(new ApplicationConfigWithFooter()) {
            private static final long serialVersionUID = 1L;
        };

        List<Component> children = applicationLayout.getChildren().collect(Collectors.toList());
        assertThat(children,
                   contains(isA(ApplicationHeader.class), isA(VerticalLayout.class), isA(ApplicationFooter.class)));
    }

    private static class MinimalApplicationConfig implements ApplicationConfig {

        @Override
        public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
            return Sequence.empty();
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return new ExampleApplicationInfo();
        }
    }

    private static class ApplicationConfigWithFooter implements ApplicationConfig {

        @Override
        public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
            return Sequence.empty();
        }

        @Override
        public Optional<ApplicationFooterDefinition> getFooterDefinition() {
            return Optional.of(ApplicationFooter::new);
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return new ExampleApplicationInfo();
        }
    }

    static class ExampleApplicationInfo implements ApplicationInfo {

        @Override
        public String getApplicationName() {
            return "name";
        }

        @Override
        public String getApplicationVersion() {
            return "version";
        }

        @Override
        public String getApplicationDescription() {
            return "description";
        }

        @Override
        public String getCopyright() {
            return "";
        }
    }
}
