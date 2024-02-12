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

package org.linkki.samples.playground.ts.layout;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class VerticalAlignmentTestComponent extends HorizontalLayout {

    private static final long serialVersionUID = -953072669422111463L;

    public VerticalAlignmentTestComponent() {
        setHeight("300px");

        add(VaadinUiCreator.createComponent(new HorizontalLayoutTopPmo(),
                                            new BindingContext("HorizontalLayoutTopPmo")));

        add(VaadinUiCreator.createComponent(new HorizontalLayoutMiddlePmo(),
                                            new BindingContext("HorizontalLayoutMiddlePmo")));

        add(VaadinUiCreator.createComponent(new HorizontalLayoutBottomPmo(),
                                            new BindingContext("HorizontalLayoutBottomPmo")));

        add(VaadinUiCreator.createComponent(new HorizontalLayoutDefaultAlignmentPmo(),
                                            new BindingContext("HorizontalLayoutDefaultAlignmentPmo")));

        getChildren()
                .forEach(c -> c.getStyle().set("background-color", "#fafafa").set("margin", "10px"));
    }

}
