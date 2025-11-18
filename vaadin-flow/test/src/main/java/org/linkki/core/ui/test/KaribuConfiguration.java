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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.linkki.core.ui.test.CustomInstantiatorMockServlet.ClassKey;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.i18n.DefaultI18NProvider;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.HasErrorParameter;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Configuration for {@link KaribuUIExtension}.
 */
public class KaribuConfiguration {

    private final Set<Class<? extends Component>> routes = new HashSet<>();
    private final Set<Class<? extends HasErrorParameter<?>>> errorRoutes = new HashSet<>();
    private final Map<ClassKey, Supplier<?>> instances = new HashMap<>();
    private boolean productionMode = false;
    @CheckForNull
    private Locale locale;

    /**
     * Creates a new {@link KaribuConfiguration} with defaults. Use {@link #withDefaults()} instead
     * for chaining multiple configurations.
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
     * Adds the given routes. The given classes are instantiated with the default constructor. To
     * define instances for the views, see {@link #addRoute(Class, Supplier)}.
     */
    @SafeVarargs
    public final KaribuConfiguration addRoutes(Class<? extends Component>... routes) {
        this.routes.addAll(Set.of(routes));
        return this;
    }

    public Set<Class<? extends HasErrorParameter<?>>> getErrorRoutes() {
        return errorRoutes;
    }

    /**
     * Adds the given error routes. The given classes are instantiated with the default constructor.
     * To define instances for the views, see {@link #addRoute(Class, Supplier)}.
     */
    @SafeVarargs
    public final KaribuConfiguration addErrorRoutes(Class<? extends HasErrorParameter<?>>... errorRoutes) {
        this.errorRoutes.addAll(Set.of(errorRoutes));
        return this;
    }

    public Set<Class<? extends Component>> getRoutes() {
        return routes;
    }

    /**
     * Adds a route. When Vaadin navigates to the given component class, the supplier is called to
     * provide the content. To create routes using the default constructor, see
     * {@link #addRoutes(Class[])}.
     *
     * @apiNote The supplier may create a component of a different type than the route component.
     *          This allows using very simple components, instead of having to work with a subclass
     *          of a more complex route.
     * @see #addRoutes(Class[])
     */
    public KaribuConfiguration addRoute(Class<? extends Component> route, Supplier<? extends Component> supplier) {
        this.routes.add(route);
        instances.put(new ClassKey(route), supplier);
        return this;
    }

    public Map<ClassKey, Supplier<?>> getInstances() {
        return instances;
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
     * Adds a {@link I18NProvider} to the instances that should be used for injections in Vaadin.
     */
    public KaribuConfiguration setI18NProvider(I18NProvider provider) {
        return addInstance(I18NProvider.class, () -> provider);
    }

    public boolean isProductionMode() {
        return productionMode;
    }

    public KaribuConfiguration setProductionMode(boolean productionMode) {
        this.productionMode = productionMode;
        return this;
    }

    @CheckForNull
    public Locale getLocale() {
        return locale;
    }

    public KaribuConfiguration setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }
}
