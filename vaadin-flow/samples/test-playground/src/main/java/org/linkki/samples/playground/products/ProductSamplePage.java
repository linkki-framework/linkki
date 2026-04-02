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

package org.linkki.samples.playground.products;

import static java.util.Objects.requireNonNull;

import java.io.Serial;
import java.util.stream.Stream;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.page.AbstractPage;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProductSamplePage extends AbstractPage {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager;

    public ProductSamplePage(BindingManager bindingManager, Object... sectionPmos) {
        this.bindingManager = requireNonNull(bindingManager);
        var contentLayout = new VerticalLayout();
        Stream.of(sectionPmos)
                .map(pmo -> VaadinUiCreator.createComponent(pmo, getBindingContext()))
                .forEach(contentLayout::add);

        setSizeFull();
        setPadding(false);
        add(contentLayout);
    }

    @Override
    public void createContent() {
        // content is directly created in constructor
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }
}
