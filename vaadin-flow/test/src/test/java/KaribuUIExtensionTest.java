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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serial;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.test.WithLocale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

public class KaribuUIExtensionTest {

    @Nested
    @WithLocale("en")
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
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ENGLISH);
        }
    }

    @Nested
    class RegisterExtensionWithConfig {

        private static final MyRoute myRoute = new MyRoute();
        @RegisterExtension
        static KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(conf -> conf.addRoute(MyRoute.class, () -> myRoute));

        @Test
        void testAddRoute() {
            assertThat(UI.getCurrent()).isNotNull();
            assertThat(UI.getCurrent().navigate(MyRoute.class)).contains(myRoute);
            assertThat(KaribuUtils.getRootComponent(MyRoute.class)).isEqualTo(myRoute);
        }

        @Test
        void testDefaultI18NProvider() {
            assertThat(UI.getCurrent().getTranslation("testKey")).isEmpty();
        }

        @Route
        private static class MyRoute extends VerticalLayout {
            @Serial
            private static final long serialVersionUID = 2000472994321994678L;
        }
    }

    @Nested
    class RegisterExtensionWithCustomI18NProvider {

        @RegisterExtension
        static KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(conf -> conf.setI18NProvider(getCustomI18NProvider()));

        @Test
        void testI18NProvider() {
            assertThat(UI.getCurrent().getTranslation("testKey")).isEqualTo("testValue");
        }
    }

    @WithLocale("en")
    @Nested
    class RegisterExtensionWithLocale {

        private static final RegisterExtensionWithConfig.MyRoute myRoute = new RegisterExtensionWithConfig.MyRoute();

        @RegisterExtension
        static KaribuUIExtension extension = KaribuUIExtension
                .withConfiguration(conf -> conf.addRoute(RegisterExtensionWithConfig.MyRoute.class, () -> myRoute));

        @Test
        void testLocale() {
            assertThat(UI.getCurrent().getLocale()).isEqualTo(Locale.ENGLISH);
        }
    }

    private static @NotNull I18NProvider getCustomI18NProvider() {
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
