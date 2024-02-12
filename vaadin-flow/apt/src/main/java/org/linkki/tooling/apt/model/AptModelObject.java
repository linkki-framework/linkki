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

package org.linkki.tooling.apt.model;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.linkki.core.pmo.ModelObject;
import org.linkki.tooling.apt.util.Either;

/**
 * A representation of the {@link ModelObject} annotation.
 */
public class AptModelObject {

    private final ModelObject annotation;

    private final AnnotationMirror annotationMirror;

    private final Either<VariableElement, ExecutableElement> element;

    private final TypeMirror type;

    private final List<AptModelAttribute> modelAttributes;

    public AptModelObject(ModelObject annotation, AnnotationMirror annotationMirror,
            Either<VariableElement, ExecutableElement> element, TypeMirror type,
            List<AptModelAttribute> modelAttributes) {
        this.annotation = annotation;
        this.annotationMirror = annotationMirror;
        this.element = element;
        this.type = type;
        this.modelAttributes = modelAttributes;
    }

    public Element getMember() {
        return element.isLeft() ? element.getLeft().get() : element.getRight().get();
    }

    public ModelObject getAnnotation() {
        return annotation;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public Either<VariableElement, ExecutableElement> getElement() {
        return element;
    }

    public TypeMirror getType() {
        return type;
    }

    public List<AptModelAttribute> getModelAttributes() {
        return modelAttributes;
    }

}
