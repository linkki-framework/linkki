/*
 * Copyright Faktor Zehn GmbH.
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

package org.linkki.core.binding.property;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.section.annotations.ModelObject;

/**
 * Defines how to retrieve the name(s) of the presentation model object property and optionally the
 * model object and model attribute to be used to identify the bound source property.
 */
public class BoundProperty {

    private final String pmoProperty;
    private final String modelObject;
    private final String modelAttribute;

    /**
     * Creates a {@link BoundProperty} with the given PMO property, the {@link ModelObject#DEFAULT_NAME
     * default model object name} and an empty model attribute name.
     * 
     * @see #withModelObject(String)
     * @see #withModelAttribute(String)
     */
    public BoundProperty(String pmoProperty) {
        this(pmoProperty, ModelObject.DEFAULT_NAME, StringUtils.EMPTY);
    }

    private BoundProperty(String pmoProperty, String modelObject, String modelAttribute) {
        this.pmoProperty = pmoProperty;
        this.modelObject = modelObject;
        this.modelAttribute = StringUtils.isEmpty(modelAttribute) ? pmoProperty : modelAttribute;
    }

    /**
     * Creates a new {@link BoundProperty} with the same PMO property and model attribute name and the
     * given model object name.
     */
    public BoundProperty withModelObject(String newModelObject) {
        return new BoundProperty(pmoProperty, newModelObject, modelAttribute);
    }

    /**
     * Creates a new {@link BoundProperty} with the same PMO property and model object name and the
     * given model attribute name.
     */
    public BoundProperty withModelAttribute(String newModelAttribute) {
        return new BoundProperty(pmoProperty, modelObject, newModelAttribute);
    }

    /**
     * The name of the PMO's property. If the property does not exist in the PMO, the given value is
     * used as the attribute name of the bound {@link #getModelObject() model object}.
     * <p>
     * Note that for each aspect, the {@link #getPmoProperty() PMO property} is evaluated before
     * {@link #getModelAttribute()}. That means if an aspect method can be found with the defined
     * {@link #getPmoProperty() PMO property} in the PMO, that method is used. If no method can be found
     * in the PMO, the {@link #getModelAttribute() model attribute} is then used to find the method in
     * the {@link #getModelObject() model object}. If no {@link #getModelAttribute() model attribute} is
     * defined, {@link #getPmoProperty() PMO property} is used to find the method in the
     * {@link #getModelObject() model object}.
     */
    public String getPmoProperty() {
        return pmoProperty;
    }

    /**
     * The name of the {@linkplain #getModelObject() model object's} attribute. If an empty String was
     * originally set, {@link #getPmoProperty()} is used here as well.
     */
    public String getModelAttribute() {
        return modelAttribute;
    }

    /**
     * The name of the {@link ModelObject}. {@link ModelObject#DEFAULT_NAME} should be returned if no
     * other name is specified.
     */
    public String getModelObject() {
        return modelObject;
    }

}
