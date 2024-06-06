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

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.io.Serial;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinSession;

/**
 * Extension using {@link MockVaadin} to instantiate a Vaadin {@link UI} for a test and sets its
 * {@link Locale}. To set up routes, use {@link #withConfiguration(Consumer)}.
 */
public class KaribuUIExtension implements BeforeEachCallback, AfterEachCallback {

    protected UI setUpUI() {
        MockVaadin.setup();
        return UI.getCurrent();
    }

    /**
     * Configures a {@link KaribuUIExtension} with custom {@link Routes}
     */
    public static KaribuUIExtension withConfiguration(Consumer<KaribuConfiguration> configurator) {
        return new KaribuUIExtension() {

            @Override
            protected UI setUpUI() {
                var configuration = new KaribuConfiguration();

                configuration.setI18NProvider(getDummyI18NProvider());

                configurator.accept(configuration);

                var routes = new Routes(configuration.routeComponents, Collections.emptySet(), true);
                MockVaadin.setup(MockedUI::new, new CustomInstantiatorMockServlet(routes, configuration.instances));
                return UI.getCurrent();
            }
        };
    }

    @Override
    public void afterEach(ExtensionContext context) {
        UI.setCurrent(null);
        // some tests may set a session
        VaadinSession.setCurrent(null);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var ui = setUpUI();
        ui.setLocale(getLocale(context));
    }

    private Locale getLocale(ExtensionContext context) {
        return context.getParent().map(ExtensionContext::getElement)
                .flatMap(annotatedElement -> findAnnotation(annotatedElement, WithLocale.class))
                .map(WithLocale::value)
                .map(Locale::forLanguageTag)
                .orElse(Locale.ROOT);
    }

    private static @NotNull I18NProvider getDummyI18NProvider() {
        return new I18NProvider() {
            @Serial
            private static final long serialVersionUID = -7750056021083896353L;

            @Override
            public List<Locale> getProvidedLocales() {
                return List.of();
            }

            @Override
            public String getTranslation(String s, Locale locale, Object... objects) {
                return "";
            }
        };
    }

    /**
     * Configures a mapping between route components and instances which should be injected into
     * Vaadin
     */
    public static class KaribuConfiguration {

        private final Set<Class<? extends Component>> routeComponents = new HashSet<>();
        private final Map<ClassKey, Supplier<?>> instances = new HashMap<>();

        /**
         * Adds a route. When Vaadin navigates to the given component class, the supplier is called
         * to provide the content.
         *
         * @apiNote The supplier may create a component of a different type than the route
         *          component. This allows using very simple components, instead of having to work
         *          with a subclass of a more complex route.
         */
        public void addRoute(Class<? extends Component> route, Supplier<? extends Component> supplier) {
            routeComponents.add(route);
            instances.put(new ClassKey(route), supplier);
        }

        /**
         * Adds a supplier for instances that should be used for injections in Vaadin e.g. route
         * components.
         *
         * @see com.vaadin.flow.di.Instantiator#getOrCreate(Class)
         */
        public <T> void addInstance(Class<T> type, Supplier<? extends T> value) {
            instances.put(new ClassKey(type), value);
        }

        /**
         * Adds a {@link I18NProvider} to the instances that should be used for injections in
         * Vaadin.
         */
        public void setI18NProvider(I18NProvider provider) {
            addInstance(I18NProvider.class, () -> provider);
        }

        public record ClassKey(Class<?> clazz) implements Comparable<ClassKey> {

            @Override
            public int compareTo(@NotNull ClassKey o) {
                return Comparator.comparing((ClassKey key) -> key.clazz.getCanonicalName()).compare(this, o);
            }
        }
    }

}
