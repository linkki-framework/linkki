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


package org.linkki.tooling.util;

import java.util.Objects;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.linkki.core.pmo.ModelObject;

public class ModelObjectProperty {
    private final ModelObject modelObject;
    private final ExecutableElement method;
    private final Set<ExecutableElement> attributes;
    private final TypeElement modelObjectTypeElement;

    public ModelObjectProperty(
            ExecutableElement method,
            Set<ExecutableElement> attributes,
            TypeElement modelObjectTypeElement) {
        if (attributes.isEmpty()) {
            throw new IllegalArgumentException("no attributes found");
        }
        this.modelObject = method.getAnnotation(ModelObject.class);
        this.method = method;
        this.attributes = attributes;
        this.modelObjectTypeElement = modelObjectTypeElement;
    }

    public ModelObject getModelObject() {
        return modelObject;
    }

    public ExecutableElement getMethod() {
        return method;
    }

    public Set<ExecutableElement> getAttributes() {
        return attributes;
    }

    public TypeElement getModelObjectTypeElement() {
        return modelObjectTypeElement;
    }

    @Override
    public String toString() {
        return "ModelObjectProperty [modelObject=" + modelObject.name()
                + ", method=" + method + ", attributes=" + attributes + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, method, modelObject);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ModelObjectProperty other = (ModelObjectProperty)obj;
        return attributes.equals(other.attributes) && method.equals(other.method)
                && modelObject.equals(other.modelObject);
    }

}