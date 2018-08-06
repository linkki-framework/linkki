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
package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UITextArea;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;

public class TextAreaBindingDefinition implements BindingDefinition {

    private final UITextArea uiTextArea;

    public TextAreaBindingDefinition(UITextArea uiTextArea) {
        this.uiTextArea = requireNonNull(uiTextArea, "uiTextArea must not be null");
    }

    @Override
    public Component newComponent() {
        TextArea area = ComponentFactory.newTextArea();
        if (uiTextArea.columns() > 0) {
            area.setColumns(uiTextArea.columns());
        } else {
            area.setWidth("100%");
        }
        if (uiTextArea.maxLength() > 0) {
            area.setMaxLength(uiTextArea.maxLength());
        }
        if (uiTextArea.rows() > 0) {
            area.setRows(uiTextArea.rows());
        }
        return area;
    }

    @Override
    public int position() {
        return uiTextArea.position();
    }

    @Override
    public String label() {
        return uiTextArea.label();
    }

    @Override
    public EnabledType enabled() {
        return uiTextArea.enabled();
    }

    @Override
    public RequiredType required() {
        return uiTextArea.required();
    }

    @Override
    public VisibleType visible() {
        return uiTextArea.visible();
    }

    @Override
    public String modelAttribute() {
        return uiTextArea.modelAttribute();
    }

    @Override
    public boolean showLabel() {
        return !uiTextArea.noLabel();
    }

    @Override
    public String modelObject() {
        return uiTextArea.modelObject();
    }
}