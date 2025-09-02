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
package org.linkki.core.ui.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.KaribuUIExtension.KaribuConfiguration.withDefaults;
import static org.mockito.Mockito.mock;

import java.io.Serial;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.function.Consumers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.DefaultI18NProvider;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.internal.LocaleUtil;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.HttpStatusCode;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

import edu.umd.cs.findbugs.annotations.NonNull;

public class KaribuUIExtensionTest {

    @Nested
    @ExtendWith(KaribuUIExtension.class)
    class AnnotationBased {

        /**
         * Tests that {@link AfterEachCallback} in {@link KaribuUIExtension} nulls the current
         * {@link UI} and {@link VaadinSession}.
         */
        @AfterAll
        public static void afterAll() {
            assertThat(UI.getCurrent()).isNull();
            assertThat(VaadinSession.getCurrent()).isNull();
        }

        @Test
        void testSetupUi() {
            assertThat(UI.getCurrent()).isNotNull();
        }

        @Test
        void testDefaultLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ROOT);
        }

        @Test
        void testDefaultI18NProvider() {
            assertThat(LocaleUtil.getI18NProvider()).isNotNull();
        }

        @Test
        void testProductionMode_DefaultFalse() {
            assertThat(VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode()).isFalse();
        }
    }

    @Nested
    class RegisterExtensionWithConfiguration {

        private static final MyView myView = new MyView();

        @RegisterExtension
        KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(withDefaults()
                        .addRoute(MyView.class, () -> myView)
                        .addRoutes(ViewWithParentLayout.class,
                                   ViewWithException.class)
                        .addErrorRoutes(ErrorPage.class)
                        .addInstance(ParentLayout.class, ParentLayout::new)
                        // This should overwrite the previous instance
                        .addInstance(ParentLayout.class, OtherParentLayout::new));

        @Test
        void testAddRoute() {
            assertThat(UI.getCurrent()).isNotNull();
            assertThat(UI.getCurrent().navigate(MyView.class)).contains(myView);
            assertThat(UI.getCurrent().getCurrentView()).isEqualTo(myView);
        }

        @Test
        void testAddRouteAddInstance_ParentLayout() {
            assertThat(UI.getCurrent().navigate(ViewWithParentLayout.class)).isNotEmpty();

            assertThat(UI.getCurrent().getActiveRouterTargetsChain())
                    .hasExactlyElementsOfTypes(ViewWithParentLayout.class, OtherParentLayout.class);
        }

        @Test
        void testAddErrorRoute() {
            UI.getCurrent().navigate(ViewWithException.class);

            assertThat(UI.getCurrent().getActiveRouterTargetsChain())
                    .hasExactlyElementsOfTypes(ErrorPage.class, OtherParentLayout.class);
        }

        @Test
        void testDefaultI18NProvider() {
            assertThat(LocaleUtil.getI18NProvider()).isNotNull();
        }

        @Test
        void testDefaultLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ROOT);
        }

        @Route
        private static class MyView extends VerticalLayout {
            @Serial
            private static final long serialVersionUID = 2000472994321994678L;
        }

        /* Needs to be public for reflection */
        @Route(value = "other", layout = ParentLayout.class)
        public static class ViewWithParentLayout extends VerticalLayout {
            @Serial
            private static final long serialVersionUID = 2000472994321994678L;
        }

        /* Needs to be public for reflection */
        @Route("exception")
        public static class ViewWithException extends Div implements BeforeEnterObserver {

            @Serial
            private static final long serialVersionUID = -474349692876258316L;

            @Override
            public void beforeEnter(BeforeEnterEvent event) {
                throw new IllegalStateException("");
            }
        }

        /* Needs to be public for reflection */
        @com.vaadin.flow.router.ParentLayout(ParentLayout.class)
        public static class ErrorPage extends Div implements HasErrorParameter<IllegalStateException> {

            @Serial
            private static final long serialVersionUID = -5310748287407745377L;

            @Override
            public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<IllegalStateException> parameter) {
                return HttpStatusCode.INTERNAL_SERVER_ERROR.getCode();
            }
        }

        /* Needs to be public for reflection */
        public static class ParentLayout extends Div implements RouterLayout {

            @Serial
            private static final long serialVersionUID = -187801250183503292L;
        }

        private static class OtherParentLayout extends ParentLayout {

            @Serial
            private static final long serialVersionUID = -187801250183503292L;
        }
    }

    @Nested
    class RegisterExtensionWithCustomI18NProvider {

        @RegisterExtension
        KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(withDefaults()
                        .setI18NProvider(getCustomI18NProvider()));

        @Test
        void testI18NProvider() {
            assertThat(UI.getCurrent().getTranslation("testKey")).isEqualTo("testValue");
        }
    }

    @Nested
    class RegisterExtensionWithConfigurationLocale {

        @RegisterExtension
        KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(withDefaults()
                        .setI18NProvider(new DefaultI18NProvider(
                                List.of(Locale.ENGLISH, Locale.GERMAN, Locale.CHINESE)))
                        .setLocale(Locale.CHINESE));

        @Test
        void testLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.CHINESE);
        }
    }

    @Nested
    @WithLocale
    class WithLocaleRegisterExtensionDefaultValue {

        @RegisterExtension
        KaribuUIExtension extension = KaribuUIExtension.withConfiguration(Consumers.nop());

        @Test
        void testLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.GERMAN);
        }
    }

    @Nested
    @WithLocale("en")
    class WithLocaleRegisterExtension {

        @RegisterExtension
        KaribuUIExtension extension = KaribuUIExtension.withConfiguration(Consumers.nop());

        @Test
        void testLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ENGLISH);
        }
    }

    @Nested
    @WithLocale("en")
    @ExtendWith(KaribuUIExtension.class)
    class WithLocaleAnnotationBased {

        @Test
        void testLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ENGLISH);
        }

        @ValueSource(booleans = { true })
        @ParameterizedTest
        void testLocaleWithParameterizedTest() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ENGLISH);
        }
    }

    @ValueSource(booleans = { true, false })
    @ParameterizedTest
    void testSetUpUI_ProductionMode(boolean productionMode) {
        var extension = KaribuUIExtension.withConfiguration(withDefaults().setProductionMode(productionMode));

        extension.setUpUI();

        assertThat(VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode())
                .isEqualTo(productionMode);
        extension.afterEach(mock());
    }

    private static @NonNull I18NProvider getCustomI18NProvider() {
        return new I18NProvider() {
            @Serial
            private static final long serialVersionUID = 1892694711393253904L;

            @Override
            public List<Locale> getProvidedLocales() {
                return List.of();
            }

            @Override
            public String getTranslation(String key, Locale locale, Object... objects) {
                if (key.equals("testKey")) {
                    return "testValue";
                }
                return "";
            }
        };
    }
}
