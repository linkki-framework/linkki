/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.tooling.apt.validator;

import static org.linkki.tooling.apt.util.SuppressedWarningsUtils.isSuppressed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.pmo.ModelObject;
import org.linkki.tooling.apt.model.AptComponent;
import org.linkki.tooling.apt.model.AptComponentDeclaration;
import org.linkki.tooling.apt.model.AptModelAttribute;
import org.linkki.tooling.apt.model.AptModelObject;
import org.linkki.tooling.apt.model.AptPmo;
import org.linkki.util.BeanUtils;

/**
 * A {@link Validator} to mark problems with the bound properties, for example when using the same
 * method name for a PMO getter method as for a model object getter method but not defining a setter
 * in the PMO.
 */
@MessageCodes({ BoundPropertyValidator.SETTER_ONLY_IN_MODEL_OBJECT })
public class BoundPropertyValidator implements Validator {

    public static final String SETTER_ONLY_IN_MODEL_OBJECT = "SETTER_ONLY_IN_MODEL_OBJECT";

    private final Types types;
    private final Kind setterOnlyInModelObjectSeverity;

    public BoundPropertyValidator(Map<String, String> options, Types types) {
        this.types = types;
        setterOnlyInModelObjectSeverity = Severity.of(options, SETTER_ONLY_IN_MODEL_OBJECT, Kind.OTHER);
    }

    @Override
    public void validate(AptPmo pmo, Messager messager) {
        if (setterOnlyInModelObjectSeverity != Kind.OTHER) {
            Set<String> propertiesWithSetterInPmo = findSettableProperties(pmo.getElement());
            Map<AptModelObject, Set<String>> propertiesWithSetterByModelObject = new HashMap<>();

            pmo.getComponents().stream()
                    .map(AptComponent::getComponentDeclarations)
                    .flatMap(List<AptComponentDeclaration>::stream)
                    .filter(it -> it.getModelObject().isPresent())
                    .filter(it -> !it.isDirectModelBinding())
                    .filter(AptComponentDeclaration::isUsingStandardModelBindingAttributes)
                    .forEach(componentDeclaration -> {
                        String propertyName = componentDeclaration.getPropertyName();
                        if (!propertiesWithSetterInPmo.contains(propertyName)) {
                            String modelAttributeName = componentDeclaration.getModelAttribute()
                                    .map(AptModelAttribute::getName)
                                    .orElse(propertyName);
                            componentDeclaration.getModelObject().ifPresent(mo -> {
                                if (propertiesWithSetterByModelObject
                                        .computeIfAbsent(mo, m -> findSettableProperties(types.asElement(m.getType())))
                                        .contains(modelAttributeName)) {
                                    reportMissingSetter(messager, pmo, componentDeclaration, modelAttributeName);
                                }
                            });
                        }
                    });
        }
    }

    private void reportMissingSetter(Messager messager,
            AptPmo pmo,
            AptComponentDeclaration componentDeclaration,
            String propertyName) {
        if (!isSuppressed(pmo.getElement(), setterOnlyInModelObjectSeverity)
                && !isSuppressed(componentDeclaration.getElement(), setterOnlyInModelObjectSeverity)) {

            String message = Messages.format(SETTER_ONLY_IN_MODEL_OBJECT,
                                             componentDeclaration.getAnnotationMirror(),
                                             propertyName,
                                             componentDeclaration.getModelObject().map(AptModelObject::getAnnotation)
                                                     .map(ModelObject::name).orElse("?"),
                                             pmo.getElement());

            messager.printMessage(setterOnlyInModelObjectSeverity,
                                  message,
                                  componentDeclaration.getElement(),
                                  componentDeclaration.getAnnotationMirror());
        }
    }

    private Set<String> findSettableProperties(Element modelClass) {
        return modelClass.getEnclosedElements()
                .stream()
                .filter(it -> it.getKind() == ElementKind.METHOD)
                .map(ExecutableElement.class::cast)
                .filter(it -> it.getParameters().size() == 1)
                .map(it -> it.getSimpleName().toString())
                .filter(it -> it.startsWith(BeanUtils.SET_PREFIX))
                .map(it -> StringUtils.uncapitalize(it.substring(3)))
                .collect(Collectors.toSet());
    }
}
