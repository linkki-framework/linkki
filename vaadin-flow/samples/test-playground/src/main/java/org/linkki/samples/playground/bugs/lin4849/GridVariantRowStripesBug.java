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

package org.linkki.samples.playground.bugs.lin4849;

import java.util.stream.IntStream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GridVariantRowStripesBug extends VerticalLayout {

    public static final String LIN_4849 = "LIN-4849";
    public static final String CAPTION = LIN_4849 + " :: Row-Stripes in Grid";

    private static final long serialVersionUID = 1L;

    public GridVariantRowStripesBug() {
        add(new H4(CAPTION));
        add(new Div("""
                GridVariant "row-stripes" did not result in alternating row colors.
                """));

        add(table());
    }

    private Component table() {
        var personTablePmo = new NumberTablePmo();
        return VaadinUiCreator.createComponent(personTablePmo, new BindingContext());
    }

    @BindVariantNames({ "row-stripes" })
    @UISection(caption = "Numbers")
    static class NumberTablePmo extends SimpleTablePmo<Number, NumberRowPmo> {

        protected NumberTablePmo() {
            super(IntStream.range(0, 10).mapToObj(Number::new).toList());
        }

        @Override
        protected NumberRowPmo createRow(Number modelObject) {
            return new NumberRowPmo(modelObject);
        }

    }

    static class NumberRowPmo {
        private final Number number;

        NumberRowPmo(Number number) {
            this.number = number;
        }

        @UILabel(position = 10, label = "Number")
        public int getNumber() {
            return number.number;
        }
    }

    record Number(int number) {
    }
}