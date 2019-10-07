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

import static org.linkki.tooling.apt.util.ModelUtils.findAttribute;
import static org.linkki.tooling.apt.util.SuppressedWarningsUtils.isSuppressed;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.apt.model.AptAttribute;
import org.linkki.tooling.apt.model.AptComponentDeclaration;
import org.linkki.tooling.apt.model.AptPmo;
import org.linkki.tooling.apt.util.Constants;

/**
 * A {@link Validator} to ensure when a direct model binding is used that there actually is a property
 * of the configured name on the referenced model object.
 */
@MessageCodes({ ModelBindingValidator.MISSING_MODEL_OBJECT, ModelBindingValidator.IMPLICIT_MODEL_BINDING,
        ModelBindingValidator.MISSING_MODEL_ATTRIBUTE })
public class ModelBindingValidator implements Validator {

    public static final String IMPLICIT_MODEL_BINDING = "IMPLICIT_MODEL_BINDING";
    public static final String MISSING_MODEL_ATTRIBUTE = "MISSING_MODEL_ATTRIBUTE";
    public static final String MISSING_MODEL_OBJECT = "MISSING_MODEL_OBJECT";

    private Kind missingModelObjectSeverity;
    private Kind missingModelAttributeSeverity;
    private Kind implicitModelBindingSeverity;

    public ModelBindingValidator(Map<String, String> options) {
        missingModelObjectSeverity = Severity.of(options, MISSING_MODEL_OBJECT, Kind.ERROR);
        missingModelAttributeSeverity = Severity.of(options, MISSING_MODEL_ATTRIBUTE, Kind.ERROR);
        implicitModelBindingSeverity = Severity.of(options, IMPLICIT_MODEL_BINDING, Kind.WARNING);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        pmo.getComponents().stream()
                .flatMap(it -> it.getComponentDeclarations().stream())
                .filter(it -> it.isModelBinding())
                .filter(it -> missingModelObjectSeverity != Kind.OTHER)
                .filter(it -> !isSuppressed(it.getElement(), missingModelObjectSeverity))
                .filter(it -> hasModelBindingAttributes(it))
                .forEach(componentDeclaration -> {
                    if (!componentDeclaration.getModelObject().isPresent()) {
                        // no model object has been found
                        if (missingModelObjectSeverity == Kind.OTHER) {
                            return;
                        }

                        if (isSuppressed(pmo.getElement(), missingModelObjectSeverity)) {
                            return;
                        }

                        if (isSuppressed(componentDeclaration.getElement(), missingModelObjectSeverity)) {
                            return;
                        }
                        checkModelObject(messager, componentDeclaration);
                    } else {

                        if (missingModelAttributeSeverity == Kind.OTHER) {
                            return;
                        }

                        if (isSuppressed(pmo.getElement(), missingModelAttributeSeverity)) {
                            return;
                        }

                        if (isSuppressed(componentDeclaration.getElement(), missingModelAttributeSeverity)) {
                            return;
                        }

                        checkModelAttribute(messager, pmo, componentDeclaration);
                    }
                });
    }

    private boolean hasModelBindingAttributes(AptComponentDeclaration componentDeclaration) {
        return hasModelObject(componentDeclaration) && hasModelAttribute(componentDeclaration);
    }

    private boolean hasModelObject(AptComponentDeclaration componentDeclaration) {
        return findModelObjectAttribute(componentDeclaration).isPresent();
    }

    private Optional<AptAttribute> findModelObjectAttribute(AptComponentDeclaration componentDeclaration) {
        return findAttribute(componentDeclaration.getAttributes(),
                             Constants.MODEL_OBJECT);
    }

    private boolean hasModelAttribute(AptComponentDeclaration componentDeclaration) {
        return findModelAttributeAttribute(componentDeclaration).isPresent();
    }

    private Optional<AptAttribute> findModelAttributeAttribute(AptComponentDeclaration componentDeclaration) {
        return findAttribute(componentDeclaration.getAttributes(),
                             Constants.MODEL_ATTRIBUTE);
    }

    private void checkModelObject(
            Messager messager,
            AptComponentDeclaration componentDeclaration) {

        AptAttribute modelObject = findModelObjectAttribute(componentDeclaration)
                .orElseThrow(() -> new IllegalStateException(
                        "expected \"modelObject\" to be present in annotation"));

        String modelObjectName = modelObject.getValue().toString();

        String message = Messages.format(MISSING_MODEL_OBJECT,
                                         componentDeclaration.getAnnotationMirror(),
                                         modelObjectName);

        messager.printMessage(missingModelObjectSeverity,
                              message,
                              componentDeclaration.getElement(),
                              componentDeclaration.getAnnotationMirror(),
                              modelObject.getAnnotationValue());

    }

    private void checkModelAttribute(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration) {

        AptAttribute modelAttribute = findAttribute(componentDeclaration.getAttributes(),
                                                    Constants.MODEL_ATTRIBUTE)
                                                            .orElseThrow(() -> new IllegalStateException(
                                                                    "expected \"" + Constants.MODEL_ATTRIBUTE
                                                                            + "\" to be present in annotation"));

        Function<String, String> invalidAttributeMessage = attributeName -> Messages.format(MISSING_MODEL_ATTRIBUTE,
                                                                                            componentDeclaration
                                                                                                    .getAnnotationMirror(),
                                                                                            componentDeclaration
                                                                                                    .getModelObject()
                                                                                                    .get()
                                                                                                    .getAnnotation()
                                                                                                    .name(),
                                                                                            componentDeclaration
                                                                                                    .getModelObject()
                                                                                                    .get().getType()
                                                                                                    .toString(),
                                                                                            attributeName);

        String modelAttributeName = modelAttribute.getValue().toString();
        String attributeName = modelAttributeName.isEmpty()
                // fallback for attribute is propertyName
                ? componentDeclaration.getPropertyName()
                : modelAttributeName;

        if (modelAttributeName.isEmpty()) {
            checkBadStyle(messager, pmo, componentDeclaration);
        }

        if (!componentDeclaration.getModelAttribute().isPresent()) {
            // no model attribute has been found
            messager.printMessage(missingModelAttributeSeverity,
                                  invalidAttributeMessage.apply(attributeName),
                                  componentDeclaration.getElement(),
                                  componentDeclaration.getAnnotationMirror(),
                                  modelAttribute.getAnnotationValue());
        }

    }

    private void checkBadStyle(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration) {
        if (implicitModelBindingSeverity != Kind.OTHER
                && !isSuppressed(pmo.getElement(), implicitModelBindingSeverity)
                && !isSuppressed(componentDeclaration.getElement(), implicitModelBindingSeverity)) {

            String propertyName = componentDeclaration.getPropertyName();
            String message = Messages.format(IMPLICIT_MODEL_BINDING,
                                             componentDeclaration.getAnnotationMirror(),
                                             propertyName,
                                             propertyName);

            messager.printMessage(implicitModelBindingSeverity,
                                  message,
                                  componentDeclaration.getElement(),
                                  componentDeclaration.getAnnotationMirror());
        }
    }
}
