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

package org.linkki.tooling.model;

import javax.lang.model.element.ExecutableElement;

import org.linkki.core.binding.descriptor.property.BoundProperty;


/**
 * An {@link AptModelAttribute} is a property of a model class, which may be used as
 * {@link BoundProperty#getModelAttribute() model attribute in a bound property} (e.g. "foo" for the
 * model method "getFoo()").
 */
public class AptModelAttribute {
    /**
     * Shortened name of the property according to java bean property conventions.
     */
    private final String name;

    private final ExecutableElement element;

    public AptModelAttribute(String name, ExecutableElement element) {
        this.name = name;
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public ExecutableElement getElement() {
        return element;
    }

}
