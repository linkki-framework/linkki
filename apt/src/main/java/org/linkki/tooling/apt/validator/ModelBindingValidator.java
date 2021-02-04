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

import static org.linkki.tooling.apt.util.SuppressedWarningsUtils.isSuppressed;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.apt.model.AptAttribute;
import org.linkki.tooling.apt.model.AptComponent;
import org.linkki.tooling.apt.model.AptComponentDeclaration;
import org.linkki.tooling.apt.model.AptModelObject;
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
        if (missingModelObjectSeverity != Kind.OTHER) {
            pmo.getComponents().stream()
                    .map(AptComponent::getComponentDeclarations)
                    .flatMap(List<AptComponentDeclaration>::stream)
                    .filter(AptComponentDeclaration::isDirectModelBinding)
                    .filter(AptComponentDeclaration::isUsingStandardModelBindingAttributes)
                    .forEach(componentDeclaration -> {
                        if (!componentDeclaration.getModelObject().isPresent()) {
                            reportMissingModelObject(messager, pmo, componentDeclaration);
                        } else {
                            checkModelAttribute(messager, pmo, componentDeclaration,
                                                componentDeclaration.getModelObject().get());
                        }
                    });
        }
    }

    private void reportMissingModelObject(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration) {
        if (missingModelObjectSeverity != Kind.OTHER && !isSuppressed(pmo.getElement(), missingModelObjectSeverity)
                && !isSuppressed(componentDeclaration.getElement(), missingModelObjectSeverity)) {
            AptAttribute modelObject = AptAttribute
                    .findByName(componentDeclaration.getAttributes(), Constants.MODEL_OBJECT)
                    .orElseThrow(() -> new IllegalStateException(
                            "expected \"" + Constants.MODEL_OBJECT + "\" to be present in annotation"));

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
    }

    private void checkModelAttribute(
            Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration,
            AptModelObject modelObject) {
        AptAttribute modelAttribute = AptAttribute
                .findByName(componentDeclaration.getAttributes(), Constants.MODEL_ATTRIBUTE)
                .orElseThrow(() -> new IllegalStateException(
                        "expected \"" + Constants.MODEL_ATTRIBUTE + "\" to be present in annotation"));

        String modelAttributeName = modelAttribute.getValue().toString();

        if (isCreateReport(implicitModelBindingSeverity, pmo, componentDeclaration) && modelAttributeName.isEmpty()) {
            reportBadStyleMissingModelAttributeValue(messager, componentDeclaration);
        }

        if (isCreateReport(missingModelAttributeSeverity, pmo, componentDeclaration)
                && !componentDeclaration.getModelAttribute().isPresent()) {
            String attributeName = modelAttributeName.isEmpty()
                    // fallback for attribute is propertyName
                    ? componentDeclaration.getPropertyName()
                    : modelAttributeName;

            String invalidAttributeMessage = Messages.format(MISSING_MODEL_ATTRIBUTE,
                                                             componentDeclaration.getAnnotationMirror(),
                                                             modelObject.getAnnotation().name(),
                                                             modelObject.getType().toString(),
                                                             attributeName);
            // no model attribute has been found
            messager.printMessage(missingModelAttributeSeverity,
                                  invalidAttributeMessage,
                                  componentDeclaration.getElement(),
                                  componentDeclaration.getAnnotationMirror(),
                                  modelAttribute.getAnnotationValue());
        }
    }

    private boolean isCreateReport(Kind severity, AptPmo pmo, AptComponentDeclaration componentDeclaration) {
        return severity != Kind.OTHER
                && !isSuppressed(pmo.getElement(), severity)
                && !isSuppressed(componentDeclaration.getElement(), severity);
    }

    private void reportBadStyleMissingModelAttributeValue(
            Messager messager,
            AptComponentDeclaration componentDeclaration) {

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
