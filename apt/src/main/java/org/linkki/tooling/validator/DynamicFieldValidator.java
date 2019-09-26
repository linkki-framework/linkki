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

import static org.linkki.tooling.util.SuppressedWarningsUtils.isSuppressed;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic.Kind;

import org.apache.commons.lang3.StringUtils;
import org.linkki.tooling.model.AptAttribute;
import org.linkki.tooling.model.AptComponent;
import org.linkki.tooling.model.AptComponentDeclaration;
import org.linkki.tooling.model.AptPmo;
import org.linkki.tooling.util.Constants;
import org.linkki.tooling.util.ElementUtils;
import org.linkki.tooling.util.ModelUtils;

/**
 * A {@link Validator} to ensure that dynamic fields have the same positions.
 * <p>
 * Additionally it ensures the presence of the method get[PropertyName]ComponentType that returns a
 * {@link Class} object.
 */
@MessageCodes({ DynamicFieldValidator.MISSING_METHOD, DynamicFieldValidator.DYNAMIC_FIELD_MISMATCH })
public class DynamicFieldValidator implements Validator {

    public static final String MISSING_METHOD = "MISSING_METHOD";
    public static final String DYNAMIC_FIELD_MISMATCH = "DYNAMIC_FIELD_MISMATCH";

    private static final String COMPONENT_TYPE_MSG_TEMPLATE = Messages.getString("MissingMethod_error")
            + Messages.getString("AnnotationInfo")
            + Messages.getString("MSG_CODE");

    private static final String POSITION_MISMATCH_MSG_TEMPLATE = Messages
            .getString("MultiComponentsDifferentPositions_error") + Messages.getString("AnnotationInfo")
            + Messages.getString("MSG_CODE");

    private final Kind missingMethodSeverity;
    private final Kind positionMismatchSeverity;
    private final ElementUtils elementUtils;

    public DynamicFieldValidator(Map<String, String> options, ElementUtils elementUtils) {
        this.elementUtils = elementUtils;
        this.missingMethodSeverity = Severity.of(options, MISSING_METHOD, Kind.ERROR);
        this.positionMismatchSeverity = Severity.of(options, DYNAMIC_FIELD_MISMATCH, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (missingMethodSeverity != Kind.OTHER) {
            if (!isSuppressed(pmo.getElement(), missingMethodSeverity)) {
                checkComponentTypeMethod(messager, pmo);
            }
        }
        if (positionMismatchSeverity != Kind.OTHER) {
            if (!isSuppressed(pmo.getElement(), positionMismatchSeverity)) {
                checkPositionMismatch(messager, pmo);
            }
        }
    }

    /**
     * Checks if required "get<propertyName>ComponentType" method is present.
     * 
     * @param messager the messager
     * @param pmo the pmo
     */
    private void checkComponentTypeMethod(Messager messager, AptPmo pmo) {
        Set<ExecutableElement> allMethods = elementUtils.getAllMethods(pmo.getElement());
        Set<String> componentTypeMethods = allMethods.stream()
                .map(it -> it.getSimpleName().toString())
                .filter(it -> it.startsWith("get") && it.endsWith("ComponentType"))
                .collect(Collectors.toSet());

        pmo.getComponents().stream()
                .filter(it -> it.isDynamicField())
                .filter(it -> !componentTypeMethods.contains(getExpectedName(it)))
                .flatMap(it -> it.getComponentDeclarations().stream())
                .filter(it -> !isSuppressed(it.getElement(), missingMethodSeverity))
                .forEach(it -> {
                    String msg = String.format(COMPONENT_TYPE_MSG_TEMPLATE,
                                               getExpectedName(it),
                                               it.getPropertyName(),
                                               it.getAnnotationMirror(),
                                               MISSING_METHOD);

                    messager.printMessage(missingMethodSeverity,
                                          msg,
                                          it.getElement(),
                                          it.getAnnotationMirror());
                });
    }

    private void checkPositionMismatch(Messager messager, AptPmo pmo) {
        pmo.getComponents().stream()
                .filter(it -> it.isDynamicField())
                .filter(it -> !hasSamePositions(it))
                .flatMap(it -> it.getComponentDeclarations().stream())
                .filter(it -> !isSuppressed(it.getElement(), positionMismatchSeverity))
                .forEach(it -> {
                    String msg = String.format(POSITION_MISMATCH_MSG_TEMPLATE,
                                               it.getPropertyName(),
                                               it.getAnnotationMirror(),
                                               DYNAMIC_FIELD_MISMATCH);

                    Optional<AptAttribute> positionAttribute = ModelUtils.findAttribute(it.getAttributes(),
                                                                                        Constants.POSITION);

                    if (positionAttribute.isPresent()) {
                        messager.printMessage(positionMismatchSeverity,
                                              msg,
                                              it.getElement(),
                                              it.getAnnotationMirror(),
                                              positionAttribute.get().getAnnotationValue());
                    } else {
                        messager.printMessage(positionMismatchSeverity,
                                              msg,
                                              it.getElement(),
                                              it.getAnnotationMirror());
                    }
                });
    }

    private boolean hasSamePositions(AptComponent component) {
        return component.getComponentDeclarations().stream()
                .mapToInt(it -> it.getPosition())
                .allMatch(it -> component.getPosition() == it);
    }


    private String getExpectedName(AptComponentDeclaration componentDeclaration) {
        return getExpectedName(componentDeclaration.getPropertyName());
    }

    private String getExpectedName(AptComponent component) {
        return getExpectedName(component.getPropertyName());
    }

    private String getExpectedName(String propertyName) {
        return "get" + StringUtils.capitalize(propertyName) + "ComponentType";
    }


}
