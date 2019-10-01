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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.model.AptModelObject;
import org.linkki.tooling.model.AptPmo;
import org.linkki.tooling.util.Either;
import org.linkki.tooling.util.SuppressedWarningsUtils;
import org.linkki.util.Optionals;

/**
 * A {@link Validator} to ensure that there are no two model objects with the same name.
 */
@MessageCodes(ModelObjectValidator.MODEL_OBJECT_CLASH)
public class ModelObjectValidator implements Validator {

    public static final String MODEL_OBJECT_CLASH = "MODEL_OBJECT_CLASH";

    private final Kind modelObjectClashSeverity;

    public ModelObjectValidator(Map<String, String> options) {
        this.modelObjectClashSeverity = Severity.of(options, MODEL_OBJECT_CLASH, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {

        if (modelObjectClashSeverity == Kind.OTHER) {
            return;
        }

        if (SuppressedWarningsUtils.isSuppressed(pmo.getElement(), modelObjectClashSeverity)) {
            return;
        }

        pmo.getModelObjects().stream()
                .collect(Collectors.groupingBy(it -> it.getAnnotation().name()))
                .values()
                .stream()
                .filter(it -> it.size() > 1)
                .forEach(collidingModelObjects -> print(messager, collidingModelObjects));
    }

    private void print(
            Messager messager,
            List<AptModelObject> collidingModelObjects) {
        collidingModelObjects
                .stream()
                .filter(it -> !isSuppressed(it.getElement(), modelObjectClashSeverity))
                .forEach(current -> {
                    Element member = current.getMember();
                    AnnotationMirror currentAnnotationMirror = current.getAnnotationMirror();
                    String name = current.getAnnotation().name();
                    String modelObjetNameValuePair = getModelObjetNameValuePair(name);
                    Optional<? extends AnnotationValue> annotationValue = currentAnnotationMirror
                            .getElementValues()
                            .values()
                            .stream()
                            .findFirst();
                    collidingModelObjects.stream()
                            .filter(o -> o != current)
                            .forEach(otherModelObject -> {
                                String otherMember = otherModelObject.getMember().getSimpleName().toString();

                                String message = Messages.format(MODEL_OBJECT_CLASH,
                                                                 currentAnnotationMirror,
                                                                 modelObjetNameValuePair,
                                                                 otherMember);
                                Optionals.ifPresentOrElse(annotationValue,
                                                          it -> messager.printMessage(modelObjectClashSeverity,
                                                                                      message,
                                                                                      member,
                                                                                      currentAnnotationMirror,
                                                                                      it),
                                                          () -> messager.printMessage(modelObjectClashSeverity,
                                                                                      message,
                                                                                      member,
                                                                                      currentAnnotationMirror));
                            });
                });
    }

    private boolean isSuppressed(
            Either<? extends VariableElement, ? extends ExecutableElement> element,
            @SuppressWarnings("unused") Kind kind) {
        return SuppressedWarningsUtils.isSuppressed(Either.get(element));
    }

    private String getModelObjetNameValuePair(String name) {
        return new StringBuilder()
                .append("name ")
                .append('"').append(name).append('"')
                .toString();
    }
}
