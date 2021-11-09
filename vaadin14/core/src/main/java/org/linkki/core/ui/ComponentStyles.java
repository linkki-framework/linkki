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

package org.linkki.core.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

/**
 * Utility class for common CSS adjustments that are not part of the Vaadin API. Also holds utility
 * class names.
 * <p>
 * This class is intended to be used for small generic CSS changes. For component specific CSS styles,
 * use a CSS file instead.
 */
public class ComponentStyles {

    private ComponentStyles() {
        // prevents instantiation
    }

    /**
     * Sets the overflow of the given component to "auto". This makes the component automatically scroll
     * if necessary (see https://developer.mozilla.org/en-US/docs/Web/CSS/overflow).
     * 
     * @param component {@link Component} whose overflow property should be set
     */
    public static void setOverflowAuto(Component component) {
        component.getElement().getStyle().set("overflow", "auto");
    }

    /**
     * Sets the width of all form item labels in the given {@link Component}
     * 
     * @param component the {@link Component} on which the property is set. This will affect all labels
     *            within the {@link FormLayout}.
     * @param width the width of the label as CSS property, e.g. 12em
     */
    public static void setFormItemLabelWidth(Component component, String width) {
        component.getElement().getStyle().set("--linkki-form-item-label-width", width);
    }
}
