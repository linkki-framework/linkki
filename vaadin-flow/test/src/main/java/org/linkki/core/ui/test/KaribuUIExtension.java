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

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockVaadinServlet;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.DefaultI18NProvider;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;

/**
 * Extension using {@link MockVaadin} to instantiate a Vaadin {@link UI} for a test and sets its
 * {@link Locale}. For more complex setup such as adding routes and control the production mode, use
 * {@link #withConfiguration(KaribuConfiguration)}.
 * <p>
 * The extension can be used with {@link org.junit.jupiter.api.extension.ExtendWith} on the class.
 * Alternatively, one of the factory methods can be used to create an instance that can then be used
 * with {@link org.junit.jupiter.api.extension.RegisterExtension}.
 * 
 * @see WithLocale
 */
public class KaribuUIExtension implements BeforeEachCallback, AfterEachCallback {

    public static final String PROPERTY_PRODUCTION_MODE = "vaadin.productionMode";

    protected UI setUpUI() {
        return setUpUI(MockedUI::new, new MockVaadinServlet());
    }

    protected UI setUpUI(Supplier<UI> uiFactory, VaadinServlet servlet) {
        MockVaadin.setup(uiFactory::get, servlet);
        var ui = UI.getCurrent();
        ui.setLocale(Locale.ROOT);
        return ui;
    }

    /**
     * Configures a {@link KaribuUIExtension} with custom configuration that can be used to add
     * routes and beans.
     */
    public static KaribuUIExtension withConfiguration(Consumer<KaribuConfiguration> configurator) {
        var configuration = KaribuConfiguration.withDefaults();
        configurator.accept(configuration);
        return withConfiguration(configuration);
    }

    public static KaribuUIExtension withConfiguration(KaribuConfiguration configuration) {
        return new KaribuUIExtension() {
            @Override
            public UI setUpUI() {
                if (configuration.productionMode) {
                    System.setProperty(PROPERTY_PRODUCTION_MODE, "true");
                }
                var routes = new Routes(configuration.routes, configuration.errorRoutes, true);
                var ui = setUpUI(MockedUI::new,
                                 new CustomInstantiatorMockServlet(routes, configuration.instances));
                ui.setLocale(configuration.locale);
                return ui;
            }
        };
    }

    @Override
    public void afterEach(ExtensionContext context) {
        MockVaadin.tearDown();
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
        System.clearProperty(PROPERTY_PRODUCTION_MODE);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var ui = setUpUI();
        getAnnotatedLocale(context).ifPresent(ui::setLocale);
    }

    private Optional<Locale> getAnnotatedLocale(ExtensionContext context) {
        return context.getParent()
                .map(ExtensionContext::getElement)
                .flatMap(annotatedElement -> findAnnotation(annotatedElement, WithLocale.class))
                .map(WithLocale::value)
                .map(Locale::forLanguageTag);
    }

    /**
     * Configuration for {@link KaribuUIExtension}.
     */
    public static class KaribuConfiguration {

        private final Set<Class<? extends Component>> routes = new HashSet<>();
        private final Set<Class<? extends HasErrorParameter<?>>> errorRoutes = new HashSet<>();
        private final Map<ClassKey, Supplier<?>> instances = new HashMap<>();
        private boolean productionMode = false;
        private Locale locale = Locale.ROOT;

        /**
         * Creates a new {@link KaribuConfiguration} with defaults. Use {@link #withDefaults()}
         * instead for chaining multiple configurations.
         */
        public KaribuConfiguration() {
            instances.put(new ClassKey(I18NProvider.class),
                          () -> new DefaultI18NProvider(List.of(Locale.ENGLISH, Locale.GERMAN)));
        }

        /**
         * Creates a new {@link KaribuConfiguration} with defaults.
         */
        public static KaribuConfiguration withDefaults() {
            return new KaribuConfiguration();
        }

        /**
         * Adds the given routes. The given classes are instantiated with the default constructor.
         * To define instances for the views, see {@link #addRoute(Class, Supplier)}.
         */
        @SafeVarargs
        public final KaribuConfiguration addRoutes(Class<? extends Component>... routes) {
            this.routes.addAll(Set.of(routes));
            return this;
        }

        /**
         * Adds the given error routes. The given classes are instantiated with the default
         * constructor. To define instances for the views, see {@link #addRoute(Class, Supplier)}.
         */
        @SafeVarargs
        public final KaribuConfiguration addErrorRoutes(Class<? extends HasErrorParameter<?>>... errorRoutes) {
            this.errorRoutes.addAll(Set.of(errorRoutes));
            return this;
        }

        /**
         * Adds a route. When Vaadin navigates to the given component class, the supplier is called
         * to provide the content. To create routes using the default constructor, see
         * {@link #addRoutes(Class[])}.
         *
         * @apiNote The supplier may create a component of a different type than the route
         *          component. This allows using very simple components, instead of having to work
         *          with a subclass of a more complex route.
         * @see #addRoutes(Class[])
         */
        public KaribuConfiguration addRoute(Class<? extends Component> route, Supplier<? extends Component> supplier) {
            this.routes.add(route);
            instances.put(new ClassKey(route), supplier);
            return this;
        }

        /**
         * Adds a supplier for instances that should be used for injections in Vaadin e.g. route
         * components.
         *
         * @see com.vaadin.flow.di.Instantiator#getOrCreate(Class)
         */
        public <T> KaribuConfiguration addInstance(Class<T> type, Supplier<? extends T> value) {
            instances.put(new ClassKey(type), value);
            return this;
        }

        /**
         * Adds a {@link I18NProvider} to the instances that should be used for injections in
         * Vaadin.
         */
        public KaribuConfiguration setI18NProvider(I18NProvider provider) {
            return addInstance(I18NProvider.class, () -> provider);
        }

        public KaribuConfiguration setProductionMode(boolean productionMode) {
            this.productionMode = productionMode;
            return this;
        }

        public KaribuConfiguration setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public record ClassKey(Class<?> clazz) implements Comparable<ClassKey> {

            @Override
            public int compareTo(@NotNull ClassKey o) {
                return Comparator.comparing((ClassKey key) -> key.clazz.getCanonicalName()).compare(this, o);
            }
        }
    }

}
