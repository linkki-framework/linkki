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

package org.linkki.core.ui.element.annotation;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.ComponentAnnotationIntegrationTest.TestModelObject;
import org.linkki.core.ui.layout.annotation.UISection;

/**
 * A single column PMO for testing.
 * <p>
 * The methods {@link #dynamic()} and {@link #staticValue()} should be overridden to add UI annotations.
 */
@UISection
public abstract class AnnotationTestPmo {

    public static final String PROPERTY_DYNAMIC = "dynamic";
    public static final String PROPERTY_STATIC = "staticValue";

    protected static final String TEST_CAPTION = "testCaption";
    protected static final String TEST_LABEL = "testLabel";
    protected static final String TEST_TOOLTIP = "testTooltip";
    protected static final String DEFAULT_TOOLTIP = "defaultTooltip";

    private final Object modelObject;
    private boolean enabled;
    private boolean required;
    private boolean visible;
    private String tooltip = DEFAULT_TOOLTIP;

    /**
     * Creates a new {@link AnnotationTestPmo}.
     * 
     * @param modelObject Object that should have getter and setter for the property <code>value</code>.
     *            It should also have a getter for <code>staticValue</code>.
     *            {@link org.linkki.core.ui.element.annotation.ComponentAnnotationIntegrationTest.TestModelObject}
     *            can be used as a base class.
     */
    public AnnotationTestPmo(Object modelObject) {
        this.modelObject = modelObject;
    }

    /**
     * Should:
     * <ul>
     * <li>be annotated with a UI annotation</li>
     * <li>have a lower position that {@link #staticValue()}</li>
     * <li>have no label</li>
     * <li>be dynamically enabled</li>
     * <li>be dynamically visible</li>
     * <li>be dynamically required</li>
     * <li>have a dynamic tool tip</li>
     * <li>use {@link TestModelObject#PROPERTY_VALUE} as model attribute</li>
     * </ul>
     */
    public abstract void dynamic();

    public boolean isDynamicEnabled() {
        return enabled;
    }

    public boolean isDynamicRequired() {
        return required;
    }

    public boolean isDynamicVisible() {
        return visible;
    }

    public String getDynamicTooltip() {
        return tooltip;
    }

    /**
     * Should:
     * <ul>
     * <li>be annotated with a UI annotation</li>
     * <li>have the label {@link #TEST_LABEL}</li>
     * <li>be disabled</li>
     * <li>be invisible</li>
     * <li>be required</li>
     * <li>have the {@link #TEST_TOOLTIP} as tooltip text</li>
     * </ul>
     */
    public abstract void staticValue();

    @ModelObject
    public Object getModelObject() {
        return modelObject;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}