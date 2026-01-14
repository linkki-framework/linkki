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

package de.faktorzehn.commons.linkki.vaadin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.error.LinkkiErrorPage;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.frontend.scanner.ClassFinder.DefaultClassFinder;
import com.vaadin.flow.server.frontend.scanner.FrontendDependencies;

import de.faktorzehn.commons.linkki.board.BoardComponent;
import de.faktorzehn.commons.linkki.board.BoardLayout;
import de.faktorzehn.commons.linkki.ui.confirm.HasBrowserConfirmation;

/**
 * Smoke test for import of frontend dependencies using {@link CssImport} and {@link JsModule}.
 * <p>
 * When building the frontend bundle, Vaadin uses a bytecode scanner to locate required
 * dependencies. This test verifies the scanner works with a class structure typical for
 * applications built with f10-commons.
 */
@SuppressWarnings("deprecation")
public class FrontendDependencyImportTest {

    @Test
    void testBoardView() {
        var imports = getImports(BoardView.class, LinkkiErrorPage.class);

        assertThat(imports)
                // imported by BoardLayout
                .contains("./src/linkki-board-layout.ts")
                // imported by BoardComponent
                .contains("./src/linkki-board-component.ts")
                // imported by HasBrowserConfirmation
                .contains("./src/confirm.js")
                // imported by ErrorPage
                .contains("./styles/error-page.css");
    }

    @Route
    private static class BoardView extends BoardLayout implements HasBrowserConfirmation {
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unused")
        public BoardView() {
            super(new SampleBoardComponent());
        }
    }

    private static class SampleBoardComponent extends BoardComponent {
        private static final long serialVersionUID = 1L;

        public SampleBoardComponent() {
            super("Hello", new Div());
        }
    }

    private static Set<String> getImports(Class<?>... cls) {
        var classes = new HashSet<>(List.of(cls));
        var dependencies = new FrontendDependencies(new DefaultClassFinder(classes),
                true,
                null,
                true);

        HashSet<String> imports = new HashSet<>();
        dependencies.getModules()
                .forEach((k, v) -> imports.addAll(v));
        dependencies.getCss().entrySet().stream()
                .flatMap(e -> e.getValue().stream())
                .map(css -> css.getValue())
                .forEach(imports::add);

        return imports;
    }

}
