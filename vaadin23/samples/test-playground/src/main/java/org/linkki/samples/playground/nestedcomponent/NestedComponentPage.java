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

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class NestedComponentPage extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public NestedComponentPage() {
        BindingContext bindingContext = new BindingContext(getClass().getName());
        add(VaadinUiCreator.createComponent(new NestedComponentPmo(), bindingContext));
    }


}