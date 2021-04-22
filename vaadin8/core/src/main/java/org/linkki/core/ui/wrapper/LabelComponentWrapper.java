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

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Implementation of the {@link ComponentWrapper} with a Vaadin {@link Component} and a {@link Label}
 * component.
 */
public class LabelComponentWrapper extends VaadinComponentWrapper {

    private static final long serialVersionUID = 1L;

    @CheckForNull
    private final Label label;

    public LabelComponentWrapper(Component component) {
        this(null, component);
    }

    public LabelComponentWrapper(@CheckForNull Label label, Component component) {
        super(component, WrapperType.FIELD);
        this.label = label;
        if (this.label != null) {
            this.label.addStyleName(LinkkiTheme.COMPONENTWRAPPER_LABEL);
        }
    }

    @Override
    public void postUpdate() {
        getLabelComponent().ifPresent(l -> {
            l.setEnabled(getComponent().isEnabled());
            l.setVisible(getComponent().isVisible());
            l.setDescription(getComponent().getDescription(), ContentMode.HTML);
            updateRequiredIndicator(l);
        });
    }

    private void updateRequiredIndicator(Label existingLabel) {
        if (getComponent() instanceof HasValue) {
            if (((HasValue<?>)getComponent()).isRequiredIndicatorVisible()
                    && !((HasValue<?>)getComponent()).isReadOnly()) {
                existingLabel.addStyleName(LinkkiTheme.REQUIRED_LABEL_COMPONENT_WRAPPER);
                getComponent().addStyleName(LinkkiTheme.REQUIRED_LABEL_COMPONENT_WRAPPER);
            } else {
                existingLabel.removeStyleName(LinkkiTheme.REQUIRED_LABEL_COMPONENT_WRAPPER);
                getComponent().removeStyleName(LinkkiTheme.REQUIRED_LABEL_COMPONENT_WRAPPER);
            }
        }
    }

    @Override
    public void setLabel(String labelText) {
        if (label != null) {
            label.setValue(labelText);
        }
    }

    public Optional<Label> getLabelComponent() {
        return Optional.ofNullable(label);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(label).map(Label::getValue).orElse("<no label>") + "("
                + getComponent().getClass().getSimpleName() + ")";
    }
}
