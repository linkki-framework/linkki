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

package org.linkki.core.vaadin;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.frontend.scanner.ClassFinder.DefaultClassFinder;
import com.vaadin.flow.server.frontend.scanner.CssData;
import com.vaadin.flow.server.frontend.scanner.FrontendDependencies;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.element.annotation.UILink;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test for import of frontend dependencies using {@link CssImport} and {@link JsModule}.
 * <p>
 * When building the frontend bundle, Vaadin uses a bytecode scanner to locate required
 * dependencies. This test verifies the scanner works with a class structure typical for
 * applications built with linkki.
 */
public class FrontendDependencyImportTest {

    @Test
    void testUILabel() {
        var imports = getImports(LinkRoute.class);

        assertThat(imports).contains("./src/linkki-text.ts", "./styles/linkki-has-icon.css");
    }

    @Route
    private static class LinkRoute {
        @SuppressWarnings("unused")
        private LinkPmo pmo;
    }

    private static class LinkPmo {
        @UILink(position = 0)
        public String getValue() {
            return "https://example.com";
        }
    }

    private static Set<String> getImports(Class<?> cls) {
        var dependencies = new FrontendDependencies(new DefaultClassFinder(Collections.singleton(cls)),
                true,
                null,
                true);

        HashSet<String> imports = new HashSet<>();
        dependencies.getModules()
                .forEach((k, v) -> imports.addAll(v));
        dependencies.getCss().entrySet().stream()
                .flatMap(e -> e.getValue().stream())
                .map(CssData::getValue)
                .forEach(imports::add);

        return imports;
    }

}
