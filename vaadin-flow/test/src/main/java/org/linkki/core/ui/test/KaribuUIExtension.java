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
import static org.linkki.core.ui.test.KaribuUI.setUp;
import static org.linkki.core.ui.test.KaribuUI.tearDown;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.UI;

/**
 * Extension using {@link MockVaadin} to instantiate a Vaadin {@link UI} for a test and sets its
 * {@link Locale}. For more complex setup such as adding routes and control the production mode, use
 * {@link #withConfiguration(KaribuConfiguration)}.
 * <p>
 * The extension can be used with {@link org.junit.jupiter.api.extension.ExtendWith} on the class.
 * Alternatively, one of the factory methods can be used to create an instance that can then be used
 * with {@link org.junit.jupiter.api.extension.RegisterExtension}.
 * <p>
 * If the set-up is different for each test, consider using {@link KaribuUI}.
 * 
 * @see WithLocale
 */
public class KaribuUIExtension implements BeforeEachCallback, AfterEachCallback {

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
            public void beforeEach(ExtensionContext context) {
                if (configuration.getLocale() == null) {
                    getAnnotatedLocale(context).ifPresent(configuration::setLocale);
                }
                setUp(configuration);
            }
        };
    }

    @Override
    public void afterEach(ExtensionContext context) {
        tearDown();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        var configuration = KaribuConfiguration.withDefaults();
        getAnnotatedLocale(context).ifPresent(configuration::setLocale);
        setUp(configuration);
    }

    Optional<Locale> getAnnotatedLocale(ExtensionContext context) {
        return context.getParent()
                .flatMap(ExtensionContext::getElement)
                .map(element -> element instanceof Method method ? method.getDeclaringClass() : element)
                .flatMap(annotatedElement -> findAnnotation(annotatedElement, WithLocale.class))
                .map(WithLocale::value)
                .map(Locale::forLanguageTag);
    }
}
