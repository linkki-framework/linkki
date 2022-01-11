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

package org.linkki.samples.playground.ts.components;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.uicreation.UiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DynamicComponentPage extends VerticalLayout {

    private static final long serialVersionUID = -5021831853838393995L;
    private Component component;
    private DynamicComponentPmo pmo;

    public DynamicComponentPage() {
        pmo = new DynamicComponentPmo(this::initComponent);
        initComponent();
    }

    private void initComponent() {
        // remove old component
        if (component != null) {
            remove(component);
        }
        // create new one and add it
        component = (Component)(UiCreator.createComponent(pmo, new BindingContext("DynamicComponentPage"))
                .getComponent());
        add(component);
    }

}
