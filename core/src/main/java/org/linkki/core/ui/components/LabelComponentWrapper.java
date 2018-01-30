/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.ui.components;

import java.util.Optional;

import javax.annotation.Nullable;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Implementation of the {@link ComponentWrapper} with a Vaadin {@link Component} and a
 * {@link Label} component.
 */
public class LabelComponentWrapper implements ComponentWrapper {

    @Nullable
    private final Label label;
    private final Component component;

    public LabelComponentWrapper(@Nullable Label label, Component component) {
        this.label = label;
        this.component = component;
    }

    @Override
    public void setLabel(String labelText) {
        if (label != null) {
            label.setValue(labelText);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (label != null) {
            label.setEnabled(enabled);
        }
        getComponent().setEnabled(enabled);
    }

    @Override
    public void setVisible(boolean visible) {
        if (label != null) {
            label.setVisible(visible);
        }
        getComponent().setVisible(visible);
    }

    @Override
    public void setTooltip(String text) {
        ((AbstractComponent)getComponent()).setDescription(text);
        getLabelComponent().ifPresent(l -> l.setDescription(text));
    }

    public Optional<Label> getLabelComponent() {
        return Optional.ofNullable(label);
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public String toString() {
        return "component=" + component + ", label=" + label;
    }
}
