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

package org.linkki.samples.playground.nestedcomponent;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

public class NestedComponentPage extends AbstractPage implements SidebarSheetDefinition {

    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);

    public NestedComponentPage() {
        init();
    }

    @Override
    public final void createContent() {
        add(VaadinUiCreator.createComponent(new NestedComponentPmo(), getBindingContext()));
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

    @Override
    public String getSidebarSheetName() {
        return "Nested Components";
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.FILE_TREE;
    }

    @Override
    public String getSidebarSheetId() {
        return "nestedcomponent";
    }

}