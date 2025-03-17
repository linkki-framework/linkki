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

package org.linkki.samples.playground.ts.ips;

import java.io.Serial;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.IpsModelObject;
import org.linkki.samples.playground.ips.model.IpsModelObjectChild;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class IpsLabelPage extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public IpsLabelPage(BindingContext bindingContext) {
        add(new Span("PMO with model object 'IpsModelObject'"));
        add(VaadinUiCreator.createComponent(new IpsPmo(new IpsModelObject()), bindingContext));
        add(new Span("PMO with model object 'IpsModelObjectChild' that inherits from 'IpsModelObject'"));
        add(VaadinUiCreator.createComponent(new IpsModelObjectChildPmo(new IpsModelObjectChild()), bindingContext));
        add(new Span("PMO with model object that is null"));
        add(VaadinUiCreator.createComponent(new IpsModelObjectNullPmo(), bindingContext));

        ComponentStyles.setFormItemLabelWidth(this, "150px");
    }

    /**
     * PMO with an {@link IpsModelObject} as model object.
     */
    @UISection
    private static class IpsPmo {

        // tag::IpsPmo:modelObject[]
        @ModelObject
        private final IpsModelObject modelObject;
        // end::IpsPmo:modelObject[]

        public IpsPmo(IpsModelObject modelObject) {
            this.modelObject = modelObject;
        }

        // tag::IpsPmo:modelAttribute[]
        @UITextField(position = 0, modelAttribute = IpsModelObject.PROPERTY_STRING)
        public void modelAttribute() {
            // model binding
        }
        // end::IpsPmo:modelAttribute[]
    }

    /**
     * PMO with a {@link IpsModelObjectChild child} of {@link IpsModelObject} as model object.
     */
    @UISection
    private static class IpsModelObjectChildPmo {

        @ModelObject
        private final IpsModelObjectChild modelObject;

        private IpsModelObjectChildPmo(IpsModelObjectChild modelObject) {
            this.modelObject = modelObject;
        }

        @UITextField(position = 0, modelAttribute = IpsModelObject.PROPERTY_STRING)
        public void getStringFromModelObjectChild() {
            // model binding
        }
    }

    /**
     * PMO with {@code null} as model object.
     */
    @UISection
    private static class IpsModelObjectNullPmo {

        @ModelObject
        private final IpsModelObject modelObject = null;

        @UITextField(position = 0, modelAttribute = IpsModelObject.PROPERTY_STRING)
        public void getStringWithModelObjectNull() {
            // model binding
        }
    }
}
