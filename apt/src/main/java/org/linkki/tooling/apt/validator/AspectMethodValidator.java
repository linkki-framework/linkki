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
import static org.linkki.tooling.apt.util.SuppressedWarningsUtils.isSuppressed;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.tooling.apt.model.AptAspectSubject;
import org.linkki.tooling.apt.model.AptPmo;
import org.linkki.tooling.apt.util.ClassNotFoundMessageUtils;
import org.linkki.tooling.apt.util.DynamicAspectMethodName;
import org.linkki.tooling.apt.util.DynamicMethodUtils;
import org.linkki.tooling.apt.util.ElementUtils;
import org.linkki.tooling.apt.util.MethodNameUtils;

/**
 * A {@link Validator} to ensure the presence of methods that are required on runtime because
 * {@link LinkkiAspectDefinition LinkkiAspectDefinitions} of a UI-Annotation demand them.
 *
 * E.g. by setting the property enabled of a UI-Annotation to {@link EnabledType#DYNAMIC} a method with
 * the name is[PropertyName]Enabled() is required.
 */
@MessageCodes({ AspectMethodValidator.MISSING_METHOD_ABSTRACT_TYPE, AspectMethodValidator.MISSING_METHOD })
public class AspectMethodValidator implements Validator {
    public static final String MISSING_METHOD = "MISSING_METHOD";
    public static final String MISSING_METHOD_ABSTRACT_TYPE = "MISSING_METHOD_ABSTRACT_TYPE";


    private final Kind missingAspectMethodSeverity;
    private final Kind missingAspectMethodAbstractTypeSeverity;

    private final ElementUtils elementUtils;

    public AspectMethodValidator(Map<String, String> options, ElementUtils elementUtils) {
        this.elementUtils = elementUtils;
        this.missingAspectMethodSeverity = Severity.of(options, MISSING_METHOD, Kind.ERROR);
        this.missingAspectMethodAbstractTypeSeverity = Severity.of(options, MISSING_METHOD_ABSTRACT_TYPE, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (pmo.isAbstractType()) {
            if (missingAspectMethodAbstractTypeSeverity == Kind.OTHER) {
                return;
            }

            if (isSuppressed(pmo.getElement(), missingAspectMethodAbstractTypeSeverity)) {
                return;
            }

            printMessages(messager,
                          pmo,
                          missingAspectMethodAbstractTypeSeverity,
                          MISSING_METHOD_ABSTRACT_TYPE);
        } else {
            if (missingAspectMethodSeverity == Kind.OTHER) {
                return;
            }

            if (isSuppressed(pmo.getElement(), missingAspectMethodSeverity)) {
                return;
            }

            printMessages(messager,
                          pmo,
                          missingAspectMethodSeverity,
                          MISSING_METHOD);
        }
    }

    private void printMessages(
            Messager messager,
            AptPmo pmo,
            Kind kind,
            String messageCode) {
        pmo.getComponents().stream()
                .flatMap(component -> Stream.concat(component.getAspectBindings().stream(),
                                                    component.getComponentDeclarations().stream()))
                .filter(aspectSubject -> !isSuppressed(aspectSubject.getElement(), kind))
                .forEach(aspectSubject -> {

                    TypeElement annotationElement = (TypeElement)aspectSubject.getAnnotationMirror()
                            .getAnnotationType()
                            .asElement();

                    try {
                        checkAspects(messager,
                                     elementUtils.getAllMethods(pmo.getElement()),
                                     aspectSubject,
                                     elementUtils.getAnnotationType(annotationElement),
                                     kind,
                                     messageCode);
                    } catch (ClassNotFoundException e) {
                        ClassNotFoundMessageUtils.printAnnotationNotFoundWarning(messager,
                                                                                 aspectSubject.getElement(),
                                                                                 aspectSubject.getAnnotationMirror());
                    }
                });
    }

    private void checkAspects(
            Messager messager,
            Set<ExecutableElement> allMethods,
            AptAspectSubject aspectSubject,
            Class<? extends Annotation> annotationType,
            Kind kind,
            String messageCode) {

        Annotation annotation = requireNonNull(aspectSubject.getElement().getAnnotation(annotationType),
                                               "annotation was null");

        List<LinkkiAspectDefinition> aspectDefinitions = AspectAnnotationReader.createAspectDefinitionsFrom(annotation);
        Set<DynamicAspectMethodName> expectedMethods = DynamicMethodUtils.getExpectedMethods(aspectSubject.getElement(),
                                                                                             aspectDefinitions,
                                                                                             messager);

        expectedMethods.stream()
                .filter(expectedMethod -> isMissing(allMethods, expectedMethod))
                .forEach(it -> {
                    String propertyName = MethodNameUtils.getPropertyName(aspectSubject.getElement());

                    String message = Messages.format(messageCode,
                                                     aspectSubject.getAnnotationMirror(),
                                                     it.getExpectedMethodName(),
                                                     propertyName);

                    messager.printMessage(kind,
                                          message,
                                          aspectSubject.getElement(),
                                          aspectSubject.getAnnotationMirror());
                });
    }

    private boolean isMissing(Set<ExecutableElement> allMethods, DynamicAspectMethodName expectedMethod) {
        String expectedMethodName = expectedMethod.getExpectedMethodName();
        return allMethods.stream()
                .map(ExecutableElement::getSimpleName)
                .map(Object::toString)
                .noneMatch(it -> it.equals(expectedMethodName));
    }

}
