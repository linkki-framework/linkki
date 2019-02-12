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
package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.UiFramework;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.DateFormatRegistry;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;

/**
 * {@link BindingDefinition} for {@link UIDateField}.
 */
public class DateFieldBindingDefinition implements BindingDefinition {

    private final UIDateField uiDateField;
    private final DateFormatRegistry dateFormatRegistry = new DateFormatRegistry();

    public DateFieldBindingDefinition(UIDateField uiDateField) {
        this.uiDateField = requireNonNull(uiDateField, "uiDateField must not be null");
    }

    @Override
    public Component newComponent() {
        DateField dateField = ComponentFactory.newDateField();
        if (StringUtils.isNotBlank(uiDateField.dateFormat())) {
            dateField.setDateFormat(uiDateField.dateFormat());
        } else {
            Locale locale = UiFramework.getLocale();
            dateField.setDateFormat(dateFormatRegistry.getPattern(locale));
        }
        return dateField;
    }

    @Override
    public int position() {
        return uiDateField.position();
    }

    @Override
    public String label() {
        return uiDateField.label();
    }

    @Override
    public EnabledType enabled() {
        return uiDateField.enabled();
    }

    @Override
    public RequiredType required() {
        return uiDateField.required();
    }

    @Override
    public VisibleType visible() {
        return uiDateField.visible();
    }

    @Override
    public String modelAttribute() {
        return uiDateField.modelAttribute();
    }

    @Override
    public String modelObject() {
        return uiDateField.modelObject();
    }
}