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

package org.linkki.core.ui.wrapper;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.base.LabelComponentFormItem;
import org.linkki.core.vaadin.component.section.BaseSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Label;

/**
 * Implementation of the {@link ComponentWrapper} with a Vaadin {@link Component} and a {@link Label}
 * component. These are merged to a {@link FormItem} in a {@link BaseSection}.
 */
public class FormItemComponentWrapper extends VaadinComponentWrapper {

    private static final long serialVersionUID = 1L;

    private final LabelComponentFormItem formItem;

    public FormItemComponentWrapper(LabelComponentFormItem formItem) {
        super(formItem.getComponent(), WrapperType.FIELD);
        this.formItem = formItem;
        formItem.getLabel().addClassName(LinkkiTheme.COMPONENTWRAPPER_LABEL);
    }

    @Override
    public void setLabel(String labelText) {
        formItem.setLabel(labelText);
    }

    public Label getLabelComponent() {
        return formItem.getLabel();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        formItem.setVisible(visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        formItem.setEnabled(enabled);
    }
}
