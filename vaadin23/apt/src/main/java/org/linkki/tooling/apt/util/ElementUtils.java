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


package org.linkki.tooling.apt.util;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.apache.commons.lang3.tuple.Pair;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspects;
import org.linkki.core.pmo.ModelObject;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public final class ElementUtils {
    private final Elements elements;
    private final Types types;
    private final ClassLoader classLoader;

    public ElementUtils(
            Types typeUtils,
            Elements elementUtils,
            ClassLoader classLoader) {
        requireNonNull(typeUtils);
        requireNonNull(elementUtils);

        this.types = typeUtils;
        this.elements = elementUtils;
        this.classLoader = classLoader;
    }

    public Elements getElements() {
        return elements;
    }

    public Types getTypes() {
        return types;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Annotation> getAnnotationType(TypeElement typeElement) throws ClassNotFoundException {
        String typeName = typeElement.getQualifiedName().toString();

        try {
            return (Class<? extends Annotation>)Class.forName(typeName,
                                                              true,
                                                              classLoader);

        } catch (LinkageError | ClassNotFoundException e1) {
            try {
                return (Class<? extends Annotation>)Class.forName(typeName,
                                                                  true,
                                                                  LinkkiAnnotationProcessor.class.getClassLoader());

            } catch (LinkageError | ClassNotFoundException e2) {
                // try again using context class loader
                return (Class<? extends Annotation>)Class.forName(typeName,
                                                                  true,
                                                                  Thread.currentThread().getContextClassLoader());
            }
        }
    }

    public boolean isMethod(Element element) {
        return element.getKind() == ElementKind.METHOD;
    }

    public ExecutableElement asExecutableElement(Element element) {
        return (ExecutableElement)element;
    }

    public boolean isLinkkiAspectAnnotation(TypeElement annotation) {
        try {
            return isLinkkiAspectAnnotation(getAnnotationType(annotation));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean isLinkkiAspectAnnotation(Class<? extends Annotation> annotation) {
        return annotation.isAnnotationPresent(LinkkiAspect.class)
                || annotation.isAnnotationPresent(LinkkiAspects.class);
    }

    public AnnotationMirror getAnnotationMirror(
            Element element,
            TypeElement annotation) {
        return element.getAnnotationMirrors().stream()
                .filter(mirror -> types.isSameType(mirror.getAnnotationType(), annotation.asType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot find AnnotationMirror of type " + annotation + " on " + element));
    }

    public Set<ExecutableElement> getAllMethods(
            TypeElement classElement) {
        return elements.getAllMembers(classElement).stream()
                .filter(this::isMethod)
                .map(this::asExecutableElement)
                .collect(toSet());
    }

    public Set<Pair<ExecutableElement, TypeElement>> getUIMethods(Set<ExecutableElement> allMethods) {
        Stream<ExecutableElement> methods = allMethods.stream()
                .filter(method -> method.getAnnotationMirrors().stream()
                        .map(mirror -> mirror.getAnnotationType().asElement())
                        .filter(annotation -> annotation.getKind() == ElementKind.ANNOTATION_TYPE)
                        .map(TypeElement.class::cast)
                        .anyMatch(this::isLinkkiAspectAnnotation));

        return methods
                .flatMap(method -> method.getAnnotationMirrors().stream()
                        .map(AnnotationMirror::getAnnotationType)
                        .map(DeclaredType::asElement)
                        .filter(it -> it.getKind() == ElementKind.ANNOTATION_TYPE)
                        .map(TypeElement.class::cast)
                        .filter(this::isLinkkiAspectAnnotation)
                        .map(it -> Pair.of(method, it)))
                .collect(toSet());
    }

    public Set<ExecutableElement> getModelObjectAttributes(
            TypeMirror type) {

        Element returnElement = types.asElement(type);
        if (returnElement instanceof TypeElement) {
            return getAllMethods((TypeElement)returnElement)
                    .stream()
                    .filter(this::isGetter)
                    .collect(Collectors.toSet());
        } else if (returnElement instanceof TypeParameterElement) {
            TypeParameterElement parameter = (TypeParameterElement)returnElement;

            return parameter.getBounds().stream()
                    .map(bound -> getAllMethods((TypeElement)types.asElement(bound)))
                    .flatMap(Set::stream)
                    .filter(this::isGetter)
                    .collect(Collectors.toSet());
        } else {
            throw new IllegalStateException("Invalid return type for ModelObject");
        }
    }

    public boolean isGetter(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        return !"getClass".equals(methodName) && MethodNameUtils.isGetter(methodName);
    }

    public boolean isAbstractClassOrInterface(TypeElement classElement) {
        return classElement.getKind() == ElementKind.INTERFACE
                || classElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    public Set<ModelObjectProperty> getModelObjects(Set<ExecutableElement> allMethods) {
        return allMethods.stream()
                .filter(it -> it.getAnnotation(ModelObject.class) != null)
                .filter(it -> !(it.getReturnType() instanceof TypeElement))
                .map(it -> {
                    Set<ExecutableElement> modelObjectAttributes = getModelObjectAttributes(it.getReturnType());
                    TypeElement modelObjectTypeElement = getModelObjectTypeElement(it);

                    return new ModelObjectProperty(
                            it,
                            modelObjectAttributes,
                            modelObjectTypeElement);
                })
                .collect(toSet());
    }

    private TypeElement getModelObjectTypeElement(ExecutableElement modelObjectMethod) {
        return modelObjectMethod.getAnnotationMirrors().stream()
                .map(it -> it.getAnnotationType().asElement())
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .filter(it -> it.getSimpleName().contentEquals(ModelObject.class.getSimpleName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("more than one @ModelObject annotation"));
    }

    public Stream<TypeElement> getClassElements(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        return annotations.stream()
                .filter(this::isLinkkiAssociatedAnnotation)
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Set::stream)
                .map(Element::getEnclosingElement)
                .distinct()
                .filter(it -> it.getKind() == ElementKind.CLASS || it.getKind() == ElementKind.INTERFACE)
                .map(TypeElement.class::cast);
    }

    private boolean isLinkkiAssociatedAnnotation(TypeElement annotation) {
        return annotation.getQualifiedName().contentEquals(ModelObject.class.getName())
                || annotation.getAnnotationsByType(LinkkiAspect.class).length != 0;
    }

    public Optional<ExecutableElement> findAttribute(
            Set<ModelObjectProperty> modelObjects,
            String modelObjectName,
            String modelAttributeName) {
        return modelObjects.stream()
                .filter(it -> it.getModelObject().name().equals(modelObjectName))
                .findFirst()
                .flatMap(it -> it.getAttributes().stream()
                        .filter(attribute -> MethodNameUtils.getPropertyName(attribute)
                                .equals(modelAttributeName))
                        .findFirst());
    }

}
