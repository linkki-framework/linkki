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

import static org.linkki.tooling.apt.util.MethodNameUtils.getPropertyName;
import static org.linkki.tooling.apt.util.ModelUtils.findAttribute;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.pmo.ModelObject;
import org.linkki.tooling.apt.model.AptAspectBinding;
import org.linkki.tooling.apt.model.AptAttribute;
import org.linkki.tooling.apt.model.AptComponent;
import org.linkki.tooling.apt.model.AptComponentDeclaration;
import org.linkki.tooling.apt.model.AptModelAttribute;
import org.linkki.tooling.apt.model.AptModelObject;
import org.linkki.tooling.apt.model.AptPmo;

import edu.umd.cs.findbugs.annotations.NonNull;


public class ModelBuilder {

    private final ElementUtils elementUtils;

    public ModelBuilder(ElementUtils elementUtils) {
        this.elementUtils = elementUtils;
    }

    public ElementUtils getElementUtils() {
        return elementUtils;
    }

    /**
     * Converts a {@link TypeElement} into a {@link AptPmo}.
     * 
     * @param pmoElement the pmo type element
     * @return a pmo
     */
    public AptPmo convertPmo(TypeElement pmoElement) {
        List<ExecutableElement> allMethods = getAllMethods(pmoElement);
        List<VariableElement> allFields = getAllFields(pmoElement);

        List<AptModelObject> modelObjects = Stream.concat(getModelObjectsFromFields(allFields).stream(),
                                                          getModelObjectsFromMethods(allMethods).stream())
                .collect(Collectors.toList());

        List<AptComponent> components = getComponents(modelObjects, allMethods);

        return new AptPmo(modelObjects, components, pmoElement);
    }

    private List<ExecutableElement> getAllMethods(TypeElement pmoElement) {
        return elementUtils.getElements().getAllMembers(pmoElement).stream()
                .filter(it -> it.getKind() == ElementKind.METHOD)
                .filter(ExecutableElement.class::isInstance)
                .map(ExecutableElement.class::cast)
                .collect(Collectors.toList());
    }

    private List<VariableElement> getAllFields(TypeElement pmoElement) {
        return elementUtils.getElements().getAllMembers(pmoElement).stream()
                .filter(it -> it.getKind() == ElementKind.FIELD)
                .filter(VariableElement.class::isInstance)
                .map(VariableElement.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * 
     * Extracts all the {@link AptModelObject} in that pmo.
     * 
     * @param pmoElement the {@link TypeElement} that represents the pmo.
     * @return a list of {@link AptModelObject}
     */
    private List<AptModelObject> getModelObjectsFromMethods(List<ExecutableElement> allMethods) {
        return allMethods.stream()
                .filter(it -> it.getAnnotation(ModelObject.class) != null)
                .map(this::convertModelObject)
                .collect(Collectors.toList());
    }

    /**
     * 
     * Extracts all the {@link AptModelObject} on fields in that pmo.
     * 
     * @param allMethods all fields in the pmo.
     * @return a list of {@link AptModelObject}
     */
    private List<AptModelObject> getModelObjectsFromFields(List<VariableElement> allFields) {
        return allFields.stream()
                .filter(it -> it.getAnnotation(ModelObject.class) != null)
                .map(this::convertModelObject)
                .collect(Collectors.toList());
    }

    /**
     * 
     * @param executableElement the method that is annotated with {@link ModelObject}.
     * @return an {@link AptModelObject}
     */
    private AptModelObject convertModelObject(ExecutableElement executableElement) {
        ModelObject annotation = executableElement.getAnnotation(ModelObject.class);
        AnnotationMirror annotationMirror = executableElement.getAnnotationMirrors().stream()
                .filter(it -> ((TypeElement)it.getAnnotationType().asElement()).getQualifiedName()
                        .contentEquals(ModelObject.class.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "expected to find @ModelObject annotation on " + executableElement.getSimpleName()));
        TypeMirror type = executableElement.getReturnType();
        List<AptModelAttribute> modelAttributes = convertModelAttributes(type);

        return new AptModelObject(annotation, annotationMirror, Either.right(executableElement), type, modelAttributes);
    }

    /**
     * 
     * @param variableElement the field that is annotated with {@link ModelObject}.
     * @return an {@link AptModelObject}
     */
    private AptModelObject convertModelObject(VariableElement variableElement) {
        ModelObject annotation = variableElement.getAnnotation(ModelObject.class);
        AnnotationMirror annotationMirror = variableElement.getAnnotationMirrors().stream()
                .filter(it -> ((TypeElement)it.getAnnotationType().asElement()).getQualifiedName()
                        .contentEquals(ModelObject.class.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "expected to find @ModelObject annotation on " + variableElement.getSimpleName()));

        TypeMirror type = variableElement.asType();
        List<AptModelAttribute> modelAttributes = convertModelAttributes(type);

        return new AptModelObject(annotation, annotationMirror, Either.left(variableElement), type, modelAttributes);
    }

    private List<AptModelAttribute> convertModelAttributes(TypeMirror type) {
        Set<ExecutableElement> modelObjectAttributes = elementUtils.getModelObjectAttributes(type);

        return modelObjectAttributes.stream()
                .map(method -> new AptModelAttribute(MethodNameUtils.toPropertyName(method.getSimpleName().toString()),
                        method))
                .collect(Collectors.toList());
    }

    private List<AptComponent> getComponents(
            List<AptModelObject> modelObjects,
            List<ExecutableElement> allMethods) {

        Map<String, List<ExecutableElement>> propertiesWithAspects = allMethods.stream()
                .filter(method -> hasAspect(method))
                .collect(Collectors.groupingBy(method -> MethodNameUtils.getPropertyName(method)));

        return propertiesWithAspects.entrySet()
                .stream()
                .filter(it -> hasLinkkiComponent(it.getValue()))
                .map(it -> {

                    String propertyName = it.getKey();
                    List<ExecutableElement> methodsWithAspect = it.getValue();

                    List<AptComponentDeclaration> componentDeclarations = getComponentDeclarations(modelObjects,
                                                                                                   methodsWithAspect);
                    List<AptAspectBinding> aspectBindings = getAspectBindings(methodsWithAspect);

                    return new AptComponent(propertyName, componentDeclarations, aspectBindings);
                })
                .collect(Collectors.toList());
    }

    /**
     * 
     * @param methods methods that are part of the same component.
     * @return {@code true} if there is at least one annotation on a method that is annotated with
     *         {@link LinkkiComponent}, else {@code false}.
     */
    private boolean hasLinkkiComponent(List<ExecutableElement> methods) {
        return methods.stream()
                .anyMatch(method -> method.getAnnotationMirrors().stream()
                        .anyMatch(annotation -> hasLinkkiComponent(annotation)));
    }

    private List<AptComponentDeclaration> getComponentDeclarations(
            List<AptModelObject> modelObjects,
            List<ExecutableElement> methodsWithAspect) {
        return methodsWithAspect.stream()
                .flatMap(method -> {
                    return method.getAnnotationMirrors().stream()
                            .filter(annotation -> hasLinkkiComponent(annotation))
                            .map(annotation -> convertComponentDeclaration(modelObjects, method, annotation));
                })
                .collect(Collectors.toList());
    }

    private AptComponentDeclaration convertComponentDeclaration(
            List<AptModelObject> modelObjects,
            ExecutableElement method,
            AnnotationMirror annotation) {

        @NonNull
        List<AptAttribute> attributes = getAttributes(annotation);

        @NonNull
        Optional<AptModelObject> modelObject = getModelObject(attributes, modelObjects);
        @NonNull
        Optional<AptModelAttribute> modelAttribute = getModelAttribute(attributes,
                                                                       modelObject,
                                                                       getPropertyName(method));

        return new AptComponentDeclaration(attributes, modelObject, modelAttribute, method, annotation);
    }

    private @NonNull List<AptAttribute> getAttributes(AnnotationMirror annotation) {
        List<AptAttribute> customValueAttributes = annotation.getElementValues().entrySet().stream()
                .map(attribute -> convertAttribute(attribute))
                .collect(Collectors.toList());

        Stream<AptAttribute> defaultValueAttributes = annotation.getAnnotationType().asElement().getEnclosedElements()
                .stream()
                .filter(it -> it.getKind() == ElementKind.METHOD)
                .map(it -> (ExecutableElement)it)
                .filter(it -> customValueAttributes.stream()
                        .map(AptAttribute::getElement)
                        .noneMatch(customAttribute -> it.getSimpleName().equals(customAttribute.getSimpleName())))
                .map(it -> org.apache.commons.lang3.tuple.Pair.of(it, it.getDefaultValue()))
                .filter(it -> it.getRight() != null)
                .map(it -> convertAttribute(it));

        return Stream.concat(customValueAttributes.stream(), defaultValueAttributes)
                .collect(Collectors.toList());
    }

    private AptAttribute convertAttribute(
            Entry<? extends ExecutableElement, ? extends AnnotationValue> attribute) {
        String name = attribute.getKey().getSimpleName().toString();

        ExecutableElement element = attribute.getKey();
        AnnotationValue annotationValue = attribute.getValue();

        return new AptAttribute(name, element, annotationValue);
    }

    private @NonNull Optional<AptModelObject> getModelObject(
            @NonNull List<AptAttribute> attributes,
            List<AptModelObject> modelObjects) {
        return findAttribute(attributes, Constants.MODEL_OBJECT)
                .flatMap(modelObjectAttribute -> {
                    String modelObjectName = (String)modelObjectAttribute.getValue();
                    return modelObjects.stream()
                            .filter(modelObject -> modelObject.getAnnotation().name().equals(modelObjectName))
                            .findFirst();
                });
    }

    private @NonNull Optional<AptModelAttribute> getModelAttribute(
            @NonNull List<AptAttribute> attributes,
            @NonNull Optional<AptModelObject> modelObject,
            String fallback) {
        return findAttribute(attributes, Constants.MODEL_ATTRIBUTE)
                .flatMap(modelAttributeAttribute -> modelObject.flatMap(mo -> {
                    String modelAttributeName = modelAttributeAttribute.getValue().toString();
                    String attributeName = modelAttributeName.isEmpty() ? fallback : modelAttributeName;
                    return mo.getModelAttributes().stream()
                            .filter(it -> it.getName().equals(attributeName))
                            .findFirst();
                }));
    }

    private List<AptAspectBinding> getAspectBindings(List<ExecutableElement> methodsWithAspect) {
        return methodsWithAspect.stream()
                .flatMap(method -> {
                    return method.getAnnotationMirrors().stream()
                            .filter(annotation -> !hasLinkkiComponent(annotation))
                            .filter(this::hasAspect)
                            .map(annotation -> convertAspectBinding(method, annotation));
                })
                .collect(Collectors.toList());
    }

    private AptAspectBinding convertAspectBinding(ExecutableElement method, AnnotationMirror annotation) {
        List<AptAttribute> attributes = getAttributes(annotation);

        return new AptAspectBinding(attributes, method, annotation);
    }

    private boolean hasAspect(ExecutableElement method) {
        return method.getAnnotationMirrors().stream().anyMatch(annotation -> hasAspect(annotation));
    }

    private boolean hasAspect(AnnotationMirror annotation) {
        return annotation.getAnnotationType().asElement().getAnnotationsByType(LinkkiAspect.class).length > 0;
    }

    private boolean hasLinkkiComponent(AnnotationMirror annotation) {
        return annotation.getAnnotationType().asElement().getAnnotation(LinkkiComponent.class) != null;
    }

}