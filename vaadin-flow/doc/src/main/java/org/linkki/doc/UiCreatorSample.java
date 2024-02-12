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

package org.linkki.doc;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.doc.ExampleTablePmo.ExampleRowPmo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

public class UiCreatorSample {

    @SuppressWarnings("checkstyle:unusedlocalvariable")
    public void createLayoutFromPmo() {
        BindingContext bindingContext = new BindingContext();
        // tag::createComponent[]
        Component component = VaadinUiCreator.createComponent(new PartnerSectionPmo(new Partner()), bindingContext);
        // end::createComponent[]
    }

    @SuppressWarnings("checkstyle:unusedlocalvariable")
    public void createTableFromPmo() {
        BindingContext bindingContext = new BindingContext();
        List<Partner> partners = Arrays.asList(new Partner());
        // tag::createTable[]
        Grid<ExampleRowPmo> grid = GridComponentCreator.createGrid(new ExampleTablePmo(partners), bindingContext);
        // end::createTable[]
    }

    public void createElements() {
        // tag::createElements[]
        BindingContext bindingContext = new BindingContext();
        Div div = new Div();
        UiCreator
                .createUiElements(new PartnerSectionPmo(new Partner()), bindingContext,
                                  c -> new LabelComponentWrapper((Component)c, WrapperType.FIELD))
                .map(LabelComponentWrapper::getComponent)
                .forEach(div::add);
        // end::createElements[]
    }
}
