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

package org.linkki.tooling.validator;

import static java.util.Objects.requireNonNull;
import static org.linkki.tooling.util.SuppressedWarningsUtils.isSuppressed;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.linkki.core.pmo.ModelObject;
import org.linkki.tooling.model.AptAspectSubject;
import org.linkki.tooling.model.AptComponentDeclaration;
import org.linkki.tooling.model.AptModelObject;
import org.linkki.tooling.model.AptPmo;
import org.linkki.tooling.util.ClassNotFoundMessageUtils;
import org.linkki.tooling.util.DynamicAspectMethodName;
import org.linkki.tooling.util.DynamicMethodUtils;
import org.linkki.tooling.util.Either;
import org.linkki.tooling.util.ElementUtils;
import org.linkki.util.Optionals;

/**
 * A {@link Validator} that ensures that methods annotated with linkki annotations are public so that
 * linkki can call them at runtime.
 */
@MessageCodes(PublicModifierValidator.NON_PUBLIC_METHOD)
public class PublicModifierValidator implements Validator {

    public static final String NON_PUBLIC_METHOD = "NON_PUBLIC_METHOD";

    private final Kind nonPulbicMethodSeverity;

    private final ElementUtils elementUtils;

    public PublicModifierValidator(Map<String, String> options, ElementUtils elementUtils) {
        this.elementUtils = elementUtils;
        nonPulbicMethodSeverity = Severity.of(options, NON_PUBLIC_METHOD, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (nonPulbicMethodSeverity == Kind.OTHER) {
            return;
        }

        if (isSuppressed(pmo.getElement(), nonPulbicMethodSeverity)) {
            return;
        }

        Stream<ExecutableElement> modelObjectAndComponentMethods = Stream
                .concat(getModelObjectMethods(pmo),
                        getComponentMethods(pmo));
        printMessages(messager, modelObjectAndComponentMethods);

        Set<ExecutableElement> allMethods = elementUtils.getAllMethods(pmo.getElement());

        Stream<ExecutableElement> aspectMethods = pmo.getComponents().stream()
                .flatMap(it -> Stream.concat(it.getAspectBindings().stream(), it.getComponentDeclarations().stream()))
                .flatMap(it -> getAspectMethods(messager, allMethods, it));

        printMessages(messager, aspectMethods);

        Stream<ExecutableElement> componentTypeMethods = allMethods.stream()
                .filter(it -> {
                    String methodName = it.getSimpleName().toString();
                    return methodName.startsWith("get") && methodName.endsWith("ComponentType");
                });

        printMessages(messager, componentTypeMethods);
    }

    /**
     * Gets the {@link ExecutableElement ExecutableElements} that are annotated with a UI-Annotation.
     * 
     * @param pmo the pmo
     * @return a {@link Stream} of {@link ExecutableElement ExecutableElements}
     */
    private Stream<ExecutableElement> getComponentMethods(AptPmo pmo) {
        return pmo.getComponents().stream()
                .flatMap(c -> c.getComponentDeclarations().stream())
                .map(AptComponentDeclaration::getElement);
    }

    /**
     * Gets the {@link ExecutableElement ExecutableElements} that are annotated with
     * {@link ModelObject @ModelObject}.
     * 
     * @param pmo the pmo
     * @return a {@link Stream} of {@link ExecutableElement ExecutableElements}
     */
    private Stream<ExecutableElement> getModelObjectMethods(AptPmo pmo) {
        return pmo.getModelObjects().stream()
                .map(AptModelObject::getElement)
                .filter(Either::isRight)
                .map(Either::getRight)
                .flatMap(Optionals::stream);
    }

    private Stream<ExecutableElement> getAspectMethods(
            Messager messager,
            Set<ExecutableElement> allMethods,
            AptAspectSubject aspectSubject) {
        TypeElement annotationElement = (TypeElement)aspectSubject.getAnnotationMirror()
                .getAnnotationType()
                .asElement();

        try {
            Class<? extends Annotation> annotationType = elementUtils.getAnnotationType(annotationElement);
            Annotation annotation = aspectSubject.getElement().getAnnotation(annotationType);

            requireNonNull(annotation, "annotation was null");

            Set<String> expectedMethods = DynamicMethodUtils
                    .getExpectedMethods(aspectSubject.getElement().getSimpleName().toString(),
                                        annotation)
                    .stream()
                    .map(DynamicAspectMethodName::getExpectedMethodName)
                    .collect(Collectors.toSet());

            return allMethods.stream()
                    .filter(it -> expectedMethods.contains(it.getSimpleName().toString()));


        } catch (ClassNotFoundException e) {
            ClassNotFoundMessageUtils.printAnnotationNotFoundWarning(messager,
                                                                     aspectSubject.getElement(),
                                                                     aspectSubject.getAnnotationMirror());
            return Stream.empty();
        }
    }

    private void printMessages(Messager messager, Stream<ExecutableElement> methods) {
        methods.distinct()
                .filter(it -> !isSuppressed(it, nonPulbicMethodSeverity))
                .filter(it -> !it.getModifiers().contains(Modifier.PUBLIC))
                .forEach(method -> printMethodNotPulic(messager, method));
    }

    private void printMethodNotPulic(Messager messager, ExecutableElement method) {
        String msg = String.format(Messages.getString(NON_PUBLIC_METHOD), method.getSimpleName(), NON_PUBLIC_METHOD);
        messager.printMessage(nonPulbicMethodSeverity, msg, method);
    }
}
