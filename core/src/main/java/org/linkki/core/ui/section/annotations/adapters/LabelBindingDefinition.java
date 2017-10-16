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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIFieldDefinition;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelBindingDefinition implements UIFieldDefinition {

    private UILabel labelAnnotation;

    public LabelBindingDefinition(UILabel labelAnnotation) {
        this.labelAnnotation = requireNonNull(labelAnnotation, "labelAnnotation must not be null");
    }

    @Override
    public EnabledType enabled() {
        return EnabledType.ENABLED;
    }

    @Override
    public VisibleType visible() {
        return labelAnnotation.visible();
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public Component newComponent() {
        Label label = new Label();
        label.setContentMode(labelAnnotation.htmlContent() ? ContentMode.HTML : ContentMode.TEXT);
        for (String styleName : labelAnnotation.styleNames()) {
            label.addStyleName(styleName);
        }
        return label;
    }

    @Override
    public int position() {
        return labelAnnotation.position();
    }

    @Override
    public String label() {
        return labelAnnotation.label();
    }

    @Override
    public boolean showLabel() {
        return StringUtils.isNotBlank(labelAnnotation.label());
    }

    @Override
    public String modelObject() {
        return labelAnnotation.modelObject();
    }

    @Override
    public String modelAttribute() {
        return labelAnnotation.modelAttribute();
    }

}
