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

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;

/**
 * An {@link AptAspectBinding} is a representation of an aspect annotation that is applied on a UI
 * component (i.e. {@code @BindReadOnly}).
 */
public class AptAspectBinding implements AptAspectSubject {

    private final List<AptAttribute> attributes;

    private final ExecutableElement element;

    private final AnnotationMirror annotationMirror;

    public AptAspectBinding(List<AptAttribute> attributes, ExecutableElement element,
            AnnotationMirror annotationMirror) {
        this.attributes = attributes;
        this.element = element;
        this.annotationMirror = annotationMirror;
    }

    public String getName() {
        return annotationMirror.getAnnotationType().asElement().getSimpleName().toString();
    }

    public List<AptAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public ExecutableElement getElement() {
        return element;
    }

    @Override
    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }


}
