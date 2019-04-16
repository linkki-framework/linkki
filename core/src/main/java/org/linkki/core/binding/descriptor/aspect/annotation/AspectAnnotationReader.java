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

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.util.Classes;
import org.linkki.util.MetaAnnotation;

/**
 * Utility class to create {@link LinkkiAspectDefinition LinkkiAspectDefinitions} from annotations.
 */
public class AspectAnnotationReader {

    private static final MetaAnnotation<LinkkiAspect> LINKKI_ASPECT_ANNOTATION = MetaAnnotation.of(LinkkiAspect.class);

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
        return LINKKI_ASPECT_ANNOTATION.findAllOn(uiAnnotation)
                .map(t -> {
                    @SuppressWarnings("unchecked")
                    Class<? extends AspectDefinitionCreator<UI_ANNOTATION>> creator = (Class<? extends AspectDefinitionCreator<UI_ANNOTATION>>)t
                            .value();
                    return creator;
                })
                .map(Classes::instantiate)
                .map(c -> c.create(uiAnnotation))
                .collect(Collectors.toList());
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
