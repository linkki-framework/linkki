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

package org.linkki.samples.customlayout;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.section.Sections;
import org.linkki.core.defaults.ui.element.UiElementCreator;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

// tag::FormLayoutCreator-class[]
public class FormLayoutCreator {

    public static FormLayout create(Object pmo, BindingContext bindingContext) {
        FormLayout formLayout = new FormLayout();
        formLayout.setId(Sections.getSectionId(pmo));
        UiElementCreator
                .createUiElements(pmo, bindingContext,
                                  c -> new CaptionComponentWrapper("", (Component)c,
                                          WrapperType.FIELD))
                .forEach(cw -> formLayout.addComponent(cw.getComponent()));
        return formLayout;
    }

}
// end::FormLayoutCreator-class[]