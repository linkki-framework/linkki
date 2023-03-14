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

package org.linkki.tooling.apt.validator;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.apt.model.AptComponent;
import org.linkki.tooling.apt.model.AptComponentDeclaration;
import org.linkki.tooling.apt.model.AptPmo;

/**
 * Base class for validating a method-scoped annotation. The annotation must not be {@link Repeatable
 * repeatable}.
 * 
 * @param <T> the annotation type
 */
public abstract class MethodAnnotationValidator<T extends Annotation> implements Validator {

    private final ProcessingEnvironment env;
    private final Class<T> annotationClass;

    protected MethodAnnotationValidator(ProcessingEnvironment env, Class<T> annotationClass) {
        this.env = requireNonNull(env, "env must not be null");
        this.annotationClass = requireNonNull(annotationClass, "annotationClass must not be null");

        if (annotationClass.getAnnotation(Repeatable.class) != null) {
            throw new IllegalArgumentException("annotationClass must not be repeatable");
        }
    }

    @Override
    public void validate(AptPmo pmo, Messager m) {
        for (AptComponent component : pmo.getComponents()) {
            for (AptComponentDeclaration declaration : component.getComponentDeclarations()) {
                T annotation = declaration.getElement().getAnnotation(annotationClass);

                if (annotation != null) {
                    validate(annotation, declaration.getElement());
                }
            }
        }
    }

    /**
     * Validates a method-scoped annotation.
     * <p>
     * Class fields of the annotation should not be accessed, as they can reference custom classes which
     * are not loaded when running APT.
     * 
     * @param annotation the annotation to validate
     * @param method the annotated method
     */
    public abstract void validate(T annotation, ExecutableElement method);

    protected Types types() {
        return env.getTypeUtils();
    }

    protected Elements elements() {
        return env.getElementUtils();
    }

    protected void printError(ExecutableElement method, String annotationValue, String key) {
        AnnotationMirror annotation = getAnnotationMirror(method);
        AnnotationValue value = getAnnotationValue(annotation, annotationValue);

        String message = Messages.getString(key);
        env.getMessager().printMessage(Kind.ERROR, message, method, annotation, value);
    }

    private AnnotationMirror getAnnotationMirror(ExecutableElement method) {
        TypeMirror annotationType = elements().getTypeElement(annotationClass.getCanonicalName()).asType();
        return method.getAnnotationMirrors().stream()
                .filter(mirror -> types().isSameType(annotationType, mirror.getAnnotationType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Method is not annotated with " + annotationClass.getCanonicalName()));
    }

    private AnnotationValue getAnnotationValue(AnnotationMirror annotation, String name) {
        return annotation.getElementValues().entrySet().stream()
                .filter(entry -> entry.getKey().getSimpleName().toString().equals(name))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Annotation does not have a value with the name " + name));
    }

}
