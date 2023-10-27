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

package org.linkki.samples.playground.application.custom;

import java.io.Serial;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.samples.playground.customlayout.pmo.HotelSearchPmo;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = CustomView.NAME, layout = CustomApplicationLayout.class)
public class CustomView extends VerticalLayout {

    public static final String NAME = "custom-layout";

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomView() {
        // tag::CustomLayoutAnnotation[]
        add(VaadinUiCreator.createComponent(new HotelSearchPmo(), new BindingContext()));
        // end::CustomLayoutAnnotation[]
    }

}
