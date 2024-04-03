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

package org.linkki.samples.playground.ts.linkkipage;

import java.io.Serial;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H4;

import edu.umd.cs.findbugs.annotations.NonNull;

public class AbstractPageComponent {

    private AbstractPageComponent() {
        throw new IllegalStateException("Utility class");
    }

    public static Component create() {
        var pmo = new MinimalPmo();
        var bindingContext = new BindingContext();
        var page = new MinimalPage();

        page.add(new H4("add"));
        // can't use the same component twice, it will be filtered out
        page.add(VaadinUiCreator.createComponent(pmo, bindingContext),
                 VaadinUiCreator.createComponent(pmo, bindingContext));

        page.add(new H4("addHorizontally"));
        // can't use the same component twice, it will be filtered out
        page.addHorizontally(VaadinUiCreator.createComponent(pmo, bindingContext),
                             VaadinUiCreator.createComponent(pmo, bindingContext));

        page.add(new H4("addSections"));
        page.addSections(pmo, pmo);

        page.add(new H4("addSection x 2"));
        page.addSection(pmo);
        page.addSection(pmo);

        return page;
    }

    @UISection(caption = "Section")
    public static class MinimalPmo {

        @UILabel(position = 0, label = "label")
        public String getText() {
            return "Text";
        }
    }

    private static class MinimalPage extends AbstractPage {

        @Serial
        private static final long serialVersionUID = 1L;
        private final BindingManager bindingManager;

        public MinimalPage() {
            bindingManager = new DefaultBindingManager();
        }

        @Override
        public void createContent() {
            // does nothing
        }

        @Override
        public void addSections(@NonNull Object... pmos) {
            super.addSections(pmos);
        }

        @Override
        public LinkkiSection addSection(Object pmo) {
            return super.addSection(pmo);
        }

        @Override
        protected BindingManager getBindingManager() {
            return bindingManager;
        }

    }
}
