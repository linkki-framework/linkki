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

package org.linkki.framework.ui.application;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serial;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;

@ExtendWith(KaribuUIExtension.class)
class ApplicationLayoutTest {

    @Test
    void testApplicationLayout() {
        var applicationLayout = new ApplicationLayout(new MinimalApplicationConfig()) {
            @Serial
            private static final long serialVersionUID = 1L;
        };

        var children = applicationLayout.getChildren().toList();
        assertThat(children)
                .hasExactlyElementsOfTypes(ApplicationHeader.class, VerticalLayout.class);
    }

    @Test
    void testApplicationLayout_WithFooter() {
        var applicationLayout = new ApplicationLayout(new ApplicationConfigWithFooter()) {
            @Serial
            private static final long serialVersionUID = 1L;
        };

        var children = applicationLayout.getChildren().toList();
        assertThat(children)
                .hasExactlyElementsOfTypes(ApplicationHeader.class, VerticalLayout.class, ApplicationFooter.class);
    }

    @Test
    void testApplicationLayout_WithConfig() {
        class TestApplicationConfig implements ApplicationConfig {

            @Override
            public ApplicationInfo getApplicationInfo() {
                return new ExampleApplicationInfo();
            }

            @Override
            public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
                return Sequence.of(new ApplicationMenuItemDefinition("name1", "id1", ""),
                                   new ApplicationMenuItemDefinition("name2", "id2", ""));
            }

            @Override
            public LinkkiConverterRegistry getConverterRegistry() {
                return LinkkiConverterRegistry.DEFAULT.with(new CustomConverter());
            }

            @Override
            public ErrorHandler getErrorHandler() {
                return e -> ConfirmationDialog.open("", "custom");
            }

            @Override
            public List<String> getDefaultVariants() {
                return List.of("variant1", "variant2");
            }
        }
        var config = new TestApplicationConfig();
        var applicationLayout = new ApplicationLayout(config) {
            @Serial
            private static final long serialVersionUID = -6579711401926185518L;
        };

        _click(_get(applicationLayout, MenuItem.class, spec -> spec.withId("appmenu-help")));
        _click(_get(applicationLayout, MenuItem.class, spec -> spec.withId("appmenu-info")));

        _assertOne(Dialog.class);
        var dialogContent = KaribuUtils.getComponentTree(_get(Dialog.class));
        assertThat(dialogContent)
                .contains(config.getApplicationInfo().getApplicationName())
                .contains(config.getApplicationInfo().getApplicationVersion())
                .contains(config.getApplicationInfo().getApplicationDescription());
        KaribuUtils.Dialogs.clickOkButton();

        _assertOne(applicationLayout, MenuItem.class, spec -> spec.withId("id1"));
        _assertOne(applicationLayout, MenuItem.class, spec -> spec.withId("id2"));
        assertThat(UI.getCurrent().getElement().getThemeList())
                .containsAnyElementsOf(config.getDefaultVariants());
        assertThat(VaadinSession.getCurrent().getAttribute(LinkkiConverterRegistry.class)
                .findConverter(String.class, ApplicationInfo.class))
                        .isInstanceOf(CustomConverter.class);
        VaadinSession.getCurrent().getErrorHandler().error(new ErrorEvent(new Throwable()));
        _assertOne(Dialog.class);
        var errorDialogContent = KaribuUtils.getComponentTree(_get(Dialog.class));
        assertThat(errorDialogContent).contains("custom");
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

    private static class CustomConverter implements Converter<String, ApplicationInfo> {

        @Serial
        private static final long serialVersionUID = -1638606736620583162L;

        @Override
        public Result<ApplicationInfo> convertToModel(String s, ValueContext valueContext) {
            return null;
        }

        @Override
        public String convertToPresentation(ApplicationInfo o, ValueContext valueContext) {
            return "";
        }
    }
}
