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

package org.linkki.samples.playground.formsection;

import java.util.function.BooleanSupplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.AllUiElementsUiFormSectionPmo;
import org.linkki.util.handler.Handler;

public class FormSectionPage extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;

    public FormSectionPage(BooleanSupplier readOnlySupplier) {
        bindingContext = new BindingContext(getClass().getName(),
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly(readOnlySupplier)), Handler.NOP_HANDLER);
        init();
    }

    @Override
    public void createContent() {
        addSection(new AllUiElementsUiFormSectionPmo());
    }

    @Override
    protected BindingManager getBindingManager() {
        return new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
    }

    @Override
    protected BindingContext getBindingContext() {
        return bindingContext;
    }

}