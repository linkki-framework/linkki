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

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.mock.MockedUI;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

/**
 * Utility methods for testing with Vaadin UI including set-up and tear-down.
 * <p>
 * If the test set-up is same for each test, consider using {@link KaribuUIExtension} for set-up
 * which makes sure that teardown is called after each.
 *
 * @since 2.9.0
 */
public class KaribuUI {

    private static final String PROPERTY_PRODUCTION_MODE = "vaadin.productionMode";

    private KaribuUI() {
        // utility class
    }

    /**
     * Set up the Vaadin UI using {@link MockVaadin}.
     * <p>
     * ATTENTION: Make sure to clean up after each test using {@link #tearDown()}.
     */
    public static UI setUp(KaribuConfiguration configuration) {
        Supplier<UI> uiSupplier = () -> {
            var ui = new MockedUI();
            ui.setLocale(Optional.ofNullable(configuration.getLocale()).orElse(Locale.ROOT));
            return ui;
        };
        if (configuration.isProductionMode()) {
            // Must be set before creating the UI
            System.setProperty(PROPERTY_PRODUCTION_MODE, "true");
        }
        var routes = new Routes(configuration.getRoutes(), configuration.getErrorRoutes(), true);
        MockVaadin.setup(uiSupplier::get, new CustomInstantiatorMockServlet(routes, configuration.getInstances()));
        return UI.getCurrent();
    }

    /**
     * Cleans up the UI.
     */
    public static void tearDown() {
        MockVaadin.tearDown();
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
        System.clearProperty(PROPERTY_PRODUCTION_MODE);
    }
}
