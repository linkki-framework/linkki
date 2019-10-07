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

package org.linkki.tooling.apt.model;

import java.util.List;

import javax.lang.model.element.TypeElement;


/**
 * A representation of the presentation model object (PMO).
 */
public class AptPmo {

    private final List<AptModelObject> modelObjects;
    private final List<AptComponent> components;
    private final TypeElement element;

    public AptPmo(List<AptModelObject> modelObjects, List<AptComponent> components, TypeElement element) {
        this.modelObjects = modelObjects;
        this.components = components;
        this.element = element;
    }

    public List<AptModelObject> getModelObjects() {
        return modelObjects;
    }

    public List<AptComponent> getComponents() {
        return components;
    }

    public TypeElement getElement() {
        return element;
    }

    public static class AptModelBinding {
        private final AptModelObject modelObject;
        private final AptModelAttribute modelAttribute;
        private final AptComponentDeclaration componentDeclaration;

        public AptModelBinding(AptModelObject modelObject, AptModelAttribute modelAttribute,
                AptComponentDeclaration componentDeclaration) {
            this.modelObject = modelObject;
            this.modelAttribute = modelAttribute;
            this.componentDeclaration = componentDeclaration;
        }

        public AptModelObject getModelObject() {
            return modelObject;
        }

        public AptModelAttribute getModelAttribute() {
            return modelAttribute;
        }

        public AptComponentDeclaration getComponentDeclaration() {
            return componentDeclaration;
        }

    }
}
