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

import java.io.Serial;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockInstantiator;
import com.github.mvysny.kaributesting.v10.mock.MockService;
import com.github.mvysny.kaributesting.v10.mock.MockVaadinServlet;
import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinServletService;

/**
 * Servlet to configure routes for {@link KaribuUIExtension}
 */
public class CustomInstantiatorMockServlet extends MockVaadinServlet {

    private static final long serialVersionUID = 1L;

    private final transient Map<ClassKey, Supplier<?>> instances;

    public CustomInstantiatorMockServlet(Routes routes, Map<ClassKey, Supplier<?>> instances) {
        super(routes);
        this.instances = instances;
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) {
        VaadinServletService service = new MockService(this, deploymentConfiguration, getUiFactory()) {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public Instantiator getInstantiator() {
                return new MockInstantiator(super.getInstantiator()) {
                    @Serial
                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public <T> T getOrCreate(Class<T> p0) {
                        return Optional.ofNullable(instances.get(new ClassKey(p0)))
                                .map(supplier -> ((T)supplier.get()))
                                .orElseGet(() -> super.getOrCreate(p0));
                    }
                };
            }
        };
        try {
            service.init();
        } catch (ServiceException e) {
            throw new IllegalStateException(e);
        }
        getRoutes().register(service.getContext());
        return service;
    }

    public record ClassKey(Class<?> clazz) implements Comparable<ClassKey> {

        @Override
        public int compareTo(@NotNull ClassKey o) {
            return Comparator.comparing((ClassKey key) -> key.clazz.getCanonicalName()).compare(this, o);
        }
    }
}