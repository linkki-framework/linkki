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

import static org.linkki.tooling.util.Constants.CAPTION;
import static org.linkki.tooling.util.Constants.LABEL;
import static org.linkki.tooling.util.Constants.POSITION;
import static org.linkki.tooling.util.ModelUtils.findAttribute;

import java.util.List;
import java.util.Optional;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

import org.linkki.tooling.util.MethodNameUtils;

import edu.umd.cs.findbugs.annotations.NonNull;


/**
 * A representation of the {@code @UI-Annotation} that declares a component.
 */
public class AptComponentDeclaration implements AptAspectSubject {

    private final List<AptAttribute> attributes;

    private final Optional<AptModelObject> modelObject;

    private final Optional<AptModelAttribute> modelAttribute;

    private final ExecutableElement element;

    private final AnnotationMirror annotationMirror;

    public AptComponentDeclaration(List<AptAttribute> attributes, Optional<AptModelObject> modelObject,
            Optional<AptModelAttribute> modelAttribute, ExecutableElement element, AnnotationMirror annotationMirror) {
        this.attributes = attributes;
        this.modelObject = modelObject;
        this.modelAttribute = modelAttribute;
        this.element = element;
        this.annotationMirror = annotationMirror;
    }

    public int getPosition() {
        return findAttribute(this.getAttributes(), POSITION)
                .map(AptAttribute::getValue)
                .map(it -> (Integer)it)
                .orElseThrow(() -> new IllegalStateException("expected position to be an int"));
    }

    public Optional<String> getLabel() {
        return findAttribute(this.getAttributes(), LABEL)
                .map(AptAttribute::getValue)
                .map(it -> (String)it);
    }

    @NonNull
    public Optional<String> getCaption() {
        return findAttribute(this.getAttributes(), CAPTION)
                .map(AptAttribute::getValue)
                .map(it -> (String)it);
    }

    public String getPropertyName() {
        return MethodNameUtils.toPropertyName(element.getSimpleName().toString());
    }

    public boolean isModelBinding() {
        return element.getReturnType().getKind() == TypeKind.VOID;
    }

    public List<AptAttribute> getAttributes() {
        return attributes;
    }

    public Optional<AptModelObject> getModelObject() {
        return modelObject;
    }

    public Optional<AptModelAttribute> getModelAttribute() {
        return modelAttribute;
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
