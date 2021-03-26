/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.samples.playground.layouts;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.page.AbstractPage;

public class LayoutsPage extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;

    public LayoutsPage() {
        bindingContext = new BindingContext(getClass().getName());
        init();
    }

    @Override
    public final void createContent() {
        add(VaadinUiCreator.createComponent(new HorizontalLayoutPmo(), getBindingContext()));
        add(VaadinUiCreator.createComponent(new VerticalLayoutPmo(), getBindingContext()));
        add(VaadinUiCreator.createComponent(new FormLayoutPmo(), getBindingContext()));
        add(VaadinUiCreator.createComponent(new CssLayoutPmo(), getBindingContext()));
    }

    /**
     * Ignored by {@link #getBindingContext()} to make sure that everything works without a
     * {@link BindingManager}.
     */
    @Override
    protected BindingManager getBindingManager() {
        return new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
    }

    @Override
    protected BindingContext getBindingContext() {
        return bindingContext;
    }
}