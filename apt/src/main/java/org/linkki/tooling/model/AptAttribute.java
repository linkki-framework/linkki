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

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;


/**
 * A name value pair representing an annotation's attribute.
 * <p>
 * For example: {@code @Foo(bar = "buzz")}
 * <p>
 * The attribute would consist of name = "bar" and value = "buzz".
 */
public class AptAttribute {

    private final String name;

    private final ExecutableElement element;

    private final AnnotationValue annotationValue;

    public AptAttribute(String name, ExecutableElement element, AnnotationValue annotationValue) {
        this.name = name;
        this.element = element;
        this.annotationValue = annotationValue;
    }

    public Object getValue() {
        return annotationValue.getValue();
    }

    public String getName() {
        return name;
    }

    public ExecutableElement getElement() {
        return element;
    }

    public AnnotationValue getAnnotationValue() {
        return annotationValue;
    }

}
