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

import static java.util.Arrays.asList;
import static org.linkki.tooling.apt.util.ClassNotFoundMessageUtils.printAnnotationNotFoundWarning;
import static org.linkki.tooling.apt.util.Constants.CONTENT;
import static org.linkki.tooling.apt.util.Constants.MODEL_ATTRIBUTE;
import static org.linkki.tooling.apt.util.Constants.MODEL_OBJECT;
import static org.linkki.tooling.apt.util.SuppressedWarningsUtils.isSuppressed;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.tooling.apt.model.AptAttribute;
import org.linkki.tooling.apt.model.AptComponentDeclaration;
import org.linkki.tooling.apt.model.AptModelAttribute;
import org.linkki.tooling.apt.model.AptPmo;
import org.linkki.tooling.apt.util.ElementUtils;
import org.linkki.tooling.apt.util.ReflectionUtils;

/**
 * A Validator to ensure that when a UI-Annotation has the field content and the value is either
 * {@link AvailableValuesType#ENUM_VALUES_EXCL_NULL} or
 * {@link AvailableValuesType#ENUM_VALUES_INCL_NULL} that the type of the model attribute is either Enum
 * or Boolean.
 */
@MessageCodes(AvailableValuesTypeValidator.WRONG_CONTENT_TYPE)
public class AvailableValuesTypeValidator implements Validator {

    public static final String WRONG_CONTENT_TYPE = "WRONG_CONTENT_TYPE";
    private static final HashSet<AvailableValuesType> TYPE_SENSITIVE_AVAILABLE_VALUES_TYPES = new HashSet<>(asList(
                                                                                                                   AvailableValuesType.ENUM_VALUES_EXCL_NULL,
                                                                                                                   AvailableValuesType.ENUM_VALUES_INCL_NULL));

    private final Kind wrongContentTypeSeverity;
    private final ElementUtils elementUtils;

    public AvailableValuesTypeValidator(
            Map<String, String> options,
            ElementUtils elementUtils) {
        this.elementUtils = elementUtils;
        this.wrongContentTypeSeverity = Severity.of(options, WRONG_CONTENT_TYPE, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (wrongContentTypeSeverity == Kind.OTHER) {
            return;
        }

        if (isSuppressed(pmo.getElement(), wrongContentTypeSeverity)) {
            return;
        }

        pmo.getComponents().stream()
                .flatMap(it -> it.getComponentDeclarations().stream())
                .filter(it -> it.isDirectModelBinding())
                .filter(it -> !isSuppressed(it.getElement(), wrongContentTypeSeverity))
                .forEach(componentDeclaration -> {
                    try {
                        TypeElement annotationElement = (TypeElement)componentDeclaration.getAnnotationMirror()
                                .getAnnotationType()
                                .asElement();

                        Class<? extends Annotation> annotationType = elementUtils.getAnnotationType(annotationElement);
                        Annotation annotation = componentDeclaration.getElement().getAnnotation(annotationType);
                        Objects.requireNonNull(annotation);

                        if (hasAvailableValuesAspectDefinition(annotation)) {
                            checkAvailableValues(messager, pmo, componentDeclaration, annotation);
                        }
                    } catch (ClassNotFoundException e) {
                        printAnnotationNotFoundWarning(messager,
                                                       componentDeclaration.getElement(),
                                                       componentDeclaration.getAnnotationMirror());
                    }
                });
    }

    private boolean hasAvailableValuesAspectDefinition(Annotation annotation) {
        return AspectAnnotationReader.createAspectDefinitionsFrom(annotation).stream()
                .anyMatch(this::containsAvailableValuesAspectDefinition);
    }

    private boolean containsAvailableValuesAspectDefinition(LinkkiAspectDefinition aspectDefinition) {
        if (aspectDefinition instanceof AvailableValuesAspectDefinition<?>) {
            return true;
        } else if (aspectDefinition instanceof CompositeAspectDefinition) {
            return ((CompositeAspectDefinition)aspectDefinition).getAspectDefinitions().stream()
                    .anyMatch(this::containsAvailableValuesAspectDefinition);
        } else {
            return false;
        }
    }

    private void checkAvailableValues(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration,
            Annotation annotation) {
        ReflectionUtils.getAnnotationProperty(annotation, CONTENT)
                .filter(AvailableValuesType.class::isInstance)
                .map(AvailableValuesType.class::cast)
                .filter(TYPE_SENSITIVE_AVAILABLE_VALUES_TYPES::contains)
                .ifPresent(availableValuesType -> ReflectionUtils.getAnnotationProperty(annotation, MODEL_OBJECT)
                        .map(Object::toString)
                        .ifPresent(modelObjectName -> ReflectionUtils
                                .getAnnotationProperty(annotation, MODEL_ATTRIBUTE)
                                .map(Object::toString)
                                .ifPresent(modelAttributeName -> checkModelAttribute(messager,
                                                                                     pmo,
                                                                                     componentDeclaration,
                                                                                     availableValuesType,
                                                                                     modelObjectName,
                                                                                     modelAttributeName))));
    }

    private void checkModelAttribute(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration,
            AvailableValuesType availableValuesType,
            String modelObjectName,
            String modelAttributeName) {

        String propertyName = componentDeclaration.getPropertyName();

        // property name as fallback for model attribute name
        String attributeName = modelAttributeName.isEmpty() ? propertyName : modelAttributeName;

        pmo.getModelObjects().stream()
                .filter(it -> it.getAnnotation().name().equals(modelObjectName))
                .flatMap(it -> it.getModelAttributes().stream())
                .filter(it -> it.getName().equals(attributeName))
                .map(AptModelAttribute::getElement)
                .filter(it -> !isEnumOrBoolean(it.getReturnType()))
                .findFirst()
                .ifPresent(attributeMethod -> {
                    String message = Messages.format(WRONG_CONTENT_TYPE,
                                                     componentDeclaration.getAnnotationMirror(),
                                                     propertyName,
                                                     availableValuesType);

                    Optional<AnnotationValue> attribute = AptAttribute
                            .findByName(componentDeclaration.getAttributes(), CONTENT)
                            .map(AptAttribute::getAnnotationValue);

                    if (attribute.isPresent()) {
                        messager.printMessage(wrongContentTypeSeverity,
                                              message,
                                              componentDeclaration.getElement(),
                                              componentDeclaration.getAnnotationMirror(),
                                              attribute.get());

                    } else {
                        messager.printMessage(wrongContentTypeSeverity,
                                              message,
                                              componentDeclaration.getElement(),
                                              componentDeclaration.getAnnotationMirror());
                    }
                });
    }

    private boolean isEnumOrBoolean(TypeMirror returnType) {
        Types types = elementUtils.getTypes();
        return returnType.getKind() == TypeKind.BOOLEAN
                || types.isSameType(returnType, asType(Boolean.class))
                || types.asElement(returnType).getKind() == ElementKind.ENUM;
    }

    private TypeMirror asType(Class<?> clazz) {
        return elementUtils.getElements().getTypeElement(clazz.getCanonicalName()).asType();
    }

}
