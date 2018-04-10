/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.binding.aspect;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;

/**
 * Utility class to creates {@link LinkkiAspectDefinition}s from an UI annotation.
 */
public class AspectAnnotationReader {

    private AspectAnnotationReader() {
        // do not instantiate
    }

    /**
     * Creates a list of {@link LinkkiAspectDefinition}s that are annotated in the definition of the
     * given linkki UI annotation.
     * 
     * @param uiAnnotation UI annotation that is used for linkki bindings
     * @return list of aspect definitions that apply to the given UI annotation
     */
    public static <UI_ANNOTATION extends Annotation> List<LinkkiAspectDefinition> createAspectDefinitionsFrom(
            UI_ANNOTATION uiAnnotation) {
        return getAspectDefinitionClasses(uiAnnotation).stream()
                .map(c -> instantiateDefinition(c, uiAnnotation))
                .collect(Collectors.toList());
    }

    protected static <ASPECT_ANNOTATION extends Annotation> List<Class<? extends LinkkiAspectDefinition>> getAspectDefinitionClasses(
            ASPECT_ANNOTATION annotation) {
        return Arrays.asList(annotation.annotationType().getAnnotationsByType(LinkkiAspect.class)).stream()
                .map(LinkkiAspect::value)
                .collect(Collectors.toList());
    }

    private static <D extends LinkkiAspectDefinition> D instantiateDefinition(Class<? extends D> aspectDefClass,
            Annotation uiAnnotation) {
        try {
            D aspectDef = aspectDefClass.newInstance();
            aspectDef.initialize(uiAnnotation);
            return aspectDef;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate aspect definition " + aspectDefClass + " for " + uiAnnotation.annotationType(),
                    e);
        }
    }
}
