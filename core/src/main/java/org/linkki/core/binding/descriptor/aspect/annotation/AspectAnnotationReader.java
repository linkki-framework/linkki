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

package org.linkki.core.binding.descriptor.aspect.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;

/**
 * Utility class to create {@link LinkkiAspectDefinition LinkkiAspectDefinitions} from annotations.
 */
public class AspectAnnotationReader {

    private AspectAnnotationReader() {
        // do not instantiate
    }

    /**
     * Creates a list of {@link LinkkiAspectDefinition LinkkiAspectDefinitions} that are annotated in
     * the definition of the given linkki UI annotation.
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

    protected static <ASPECT_ANNOTATION extends Annotation, UI_ANNOTATION extends Annotation> List<Class<? extends AspectDefinitionCreator<UI_ANNOTATION>>> getAspectDefinitionClasses(
            ASPECT_ANNOTATION annotation) {
        return Arrays.asList(annotation.annotationType().getAnnotationsByType(LinkkiAspect.class)).stream()
                .map(aspectAnnotation -> {
                    @SuppressWarnings("unchecked")
                    Class<? extends AspectDefinitionCreator<UI_ANNOTATION>> creatorClass = (Class<? extends AspectDefinitionCreator<UI_ANNOTATION>>)aspectAnnotation
                            .value();
                    return creatorClass;
                })
                .collect(Collectors.toList());
    }

    private static <UI_ANNOTATION extends Annotation> LinkkiAspectDefinition instantiateDefinition(
            Class<? extends AspectDefinitionCreator<UI_ANNOTATION>> aspectDefCreatorClass,
            UI_ANNOTATION uiAnnotation) {
        try {
            AspectDefinitionCreator<UI_ANNOTATION> aspectDefCreator = aspectDefCreatorClass.newInstance();
            return aspectDefCreator.create(uiAnnotation);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new LinkkiBindingException(
                    "Cannot instantiate aspect definition " + aspectDefCreatorClass + " for "
                            + uiAnnotation.annotationType(),
                    e);
        }
    }


    /**
     * Creates a list of {@link LinkkiAspectDefinition LinkkiAspectDefinitions} from all
     * {@link LinkkiAspect}-annotated annotations on the given {@link AnnotatedElement}.
     * 
     * @param annotatedElement an element with annotations
     * @return list of aspect definitions that apply to the given element's annotations
     */
    public static List<LinkkiAspectDefinition> createAspectDefinitionsFor(AnnotatedElement annotatedElement) {
        return Arrays.asList(annotatedElement.getAnnotations()).stream()
                .map(a -> AspectAnnotationReader.createAspectDefinitionsFrom(a))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
