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

import static org.linkki.tooling.apt.util.AnnotationMirrorUtils.findPositionAttributeValue;
import static org.linkki.tooling.apt.util.SuppressedWarningsUtils.isSuppressed;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationValue;
import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.apt.model.AptComponent;
import org.linkki.tooling.apt.model.AptPmo;

/**
 * This {@link Validator} ensures that there are no components with the same position in the PMO.
 */
@MessageCodes({ PositionValidator.POSITION_CLASH })
public class PositionValidator implements Validator {

    public static final String POSITION_CLASH = "POSITION_CLASH";

    private final Kind positionClashSeverity;

    public PositionValidator(Map<String, String> options) {
        this.positionClashSeverity = Severity.of(options, POSITION_CLASH, Kind.ERROR);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (positionClashSeverity == Kind.OTHER) {
            return;
        }

        if (isSuppressed(pmo.getElement(), positionClashSeverity)) {
            return;
        }

        pmo.getComponents().stream()
                .collect(Collectors.groupingBy(AptComponent::getPosition))
                .values()
                .stream()
                .filter(it -> it.size() > 1)
                .forEach(samePositionComponents -> print(messager, samePositionComponents));
    }

    private void print(
            Messager messager,
            List<AptComponent> collidingComponents) {
        collidingComponents.forEach(component -> {
            String collidingPropertyNames = getCollidingPropertyNames(collidingComponents, component);

            component.getComponentDeclarations()
                    .stream()
                    .filter(it -> !isSuppressed(it.getElement(), positionClashSeverity))
                    .forEach(componentDeclaration -> {
                        String message = Messages.format(POSITION_CLASH,
                                                         componentDeclaration.getAnnotationMirror(),
                                                         component.getPosition(),
                                                         collidingPropertyNames);

                        AnnotationValue positionValue = findPositionAttributeValue(componentDeclaration
                                .getAnnotationMirror());

                        messager.printMessage(positionClashSeverity,
                                              message,
                                              componentDeclaration.getElement(),
                                              componentDeclaration.getAnnotationMirror(),
                                              positionValue);
                    });
        });
    }

    /**
     * Gets the property name of all colliding components except the other component.
     * 
     * @param collidingComponents all colliding components
     * @param component the component to omit
     * @return a string that lists property names.
     */
    private String getCollidingPropertyNames(
            List<AptComponent> collidingComponents,
            AptComponent component) {
        int index = collidingComponents.indexOf(component);

        return IntStream.range(0, collidingComponents.size())
                .filter(it -> it != index)
                .mapToObj(collidingComponents::get)
                .map(AptComponent::getPropertyName)
                .collect(Collectors.joining(", "));
    }
}