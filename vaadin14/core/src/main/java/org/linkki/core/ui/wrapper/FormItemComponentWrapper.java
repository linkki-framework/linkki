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

import java.util.Optional;

import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.section.FormLayoutSection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Implementation of the {@link ComponentWrapper} with a Vaadin {@link Component} and a {@link Label}
 * component. These are merged to a {@link FormItem} in a {@link FormLayoutSection}.
 */
public class FormItemComponentWrapper extends VaadinComponentWrapper {

    private static final long serialVersionUID = 1L;

    @CheckForNull
    private final Span label;

    public FormItemComponentWrapper(Component component) {
        this(null, component);
    }

    public FormItemComponentWrapper(@CheckForNull Span label, Component component) {
        super(component, WrapperType.FIELD);
        this.label = label;
        if (this.label != null) {
            this.label.addClassName(LinkkiTheme.COMPONENTWRAPPER_LABEL);
        }
    }

    @Override
    public void postUpdate() {
        getLabelComponent().ifPresent(l -> {
            l.setEnabled(((HasEnabled)getComponent()).isEnabled());
            l.setVisible(getComponent().isVisible());
        });
    }


    @Override
    public void setLabel(String labelText) {
        if (label != null) {
            label.setText(labelText);
        }
    }

    public Optional<Span> getLabelComponent() {
        return Optional.ofNullable(label);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(label).map(Span::getText).orElse("<no label>") + "("
                + getComponent().getClass().getSimpleName() + ")";
    }
}
