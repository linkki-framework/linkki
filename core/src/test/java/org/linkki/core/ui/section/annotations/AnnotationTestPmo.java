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

package org.linkki.core.ui.section.annotations;

/**
 * A single column PMO for testing.
 * <p>
 * The methods {@link #value()} and {@link #staticValue()} should be overridden to add UI
 * annotations. {@link #value()} should be used for testing dynamically bound properties such as
 * {@link EnabledType}, {@link RequiredType} or {@link VisibleType}. {@link #staticValue()} should
 * be used for label and other non-dynamic aspects and should be annotated with non-default values.
 * In addition, {@link #staticValue()} should not have a setter in the model.
 */
@UISection
public abstract class AnnotationTestPmo {

    protected static final String TEST_CAPTION = "testCaption";
    protected static final String TEST_LABEL = "testLabel";

    private final Object modelObject;
    private boolean enabled;
    private boolean required;
    private boolean visible;

    /**
     * Creates a new {@link AnnotationTestPmo}.
     * 
     * @param modelObject Object that should have getter and setter for the property
     *            <code>value</code>. It should also have a getter for <code>staticValue</code>.
     *            {@link org.linkki.core.ui.section.annotations.ComponentAnnotationIntegrationTest.TestModelObject}
     *            can be used as a base class.
     */
    public AnnotationTestPmo(Object modelObject) {
        this.modelObject = modelObject;
    }

    /**
     * Should:
     * <ul>
     * <li>be annotated with a UI annotation</li>
     * <li>have no label</li>
     * <li>be dynamically enabled</li>
     * <li>be dynamically visible</li>
     * <li>be dynamically required</li>
     * </ul>
     */
    public abstract void value();

    public boolean isValueEnabled() {
        return enabled;
    }

    /**
     * Should:
     * <ul>
     * <li>be annotated with a UI annotation</li>
     * <li>have the label {@link #TEST_LABEL}</li>
     * <li>be disabled</li>
     * <li>be invisible</li>
     * <li>be required</li>
     * </ul>
     */
    public abstract void staticValue();

    public boolean isValueRequired() {
        return required;
    }

    public boolean isValueVisible() {
        return visible;
    }

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
}