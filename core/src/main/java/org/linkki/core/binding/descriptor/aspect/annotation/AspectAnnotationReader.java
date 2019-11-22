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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.uicreation.ComponentAnnotationReader;
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
        return createAspectDefinitionsStreamFrom(uiAnnotation)
                .collect(Collectors.toList());
    }

    private static <UI_ANNOTATION extends Annotation> Stream<LinkkiAspectDefinition> createAspectDefinitionsStreamFrom(
            UI_ANNOTATION uiAnnotation) {
        return LINKKI_ASPECT_ANNOTATION.findAllOn(uiAnnotation)
                .map(t -> {
                    @SuppressWarnings("unchecked")
                    Class<? extends AspectDefinitionCreator<UI_ANNOTATION>> creator = (Class<? extends AspectDefinitionCreator<UI_ANNOTATION>>)t
                            .value();
                    return creator;
                })
                .map(Classes::instantiate)
                .map(c -> c.create(uiAnnotation));
    }

    /**
     * Creates a list of {@link LinkkiAspectDefinition LinkkiAspectDefinitions} from all
     * {@link LinkkiAspect}-annotated annotations on the given {@link AnnotatedElement}.
     * <p>
     * If the given element is a class, {@link InheritedAspect inherited annotations} from superclasses
     * and interfaces are returned as well. Only the first occurrence of an annotation type is included
     * in the result. Local annotations on the annotated class take precedence over those from
     * supertypes, which take precedence over those from interfaces. There is no guarantee about the
     * order of annotations from different (super-)interfaces.
     * 
     * <em>Note:</em> If the annotated element supports multiple component definition annotations it
     * might be wrong to just use all available aspects. Instead it might be necessary to select the
     * aspects from only one component definition annotation. In this case
     * {@link #createAspectDefinitionsFor(Annotation, AnnotatedElement)} is the correct option.
     * 
     * 
     * @param annotatedElement an element with annotations
     * @return list of aspect definitions that apply to the given element's annotations
     */
    public static List<LinkkiAspectDefinition> createAspectDefinitionsFor(AnnotatedElement annotatedElement) {
        return getAllAnnotations(annotatedElement)
                .map(a -> AspectAnnotationReader.createAspectDefinitionsFrom(a))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static Stream<Annotation> getAllAnnotations(AnnotatedElement annotatedElement) {
        List<Annotation> annotations = annotatedElement instanceof Class<?>
                ? getUniqueLinkkiAnnotations((Class<?>)annotatedElement)
                : Arrays.asList(annotatedElement.getAnnotations());

        return annotations.stream();
    }

    /**
     * Creates a list of {@link LinkkiAspectDefinition LinkkiAspectDefinitions} from the component
     * definition annotation and additionally all aspects from annotations that are not defining
     * component definitions. This must be used if the {@link AnnotatedElement} allows multiple
     * component definitions.
     * 
     * @param componentDefAnnotation the selected component definition annotation
     * @param annotatedElement the annotated element that provides annotations with additional aspects
     * @return The list of {@link LinkkiAspectDefinition aspect definitions} that are used for the field
     *         created for the given annotation.
     */
    public static List<LinkkiAspectDefinition> createAspectDefinitionsFor(Annotation componentDefAnnotation,
            AnnotatedElement annotatedElement) {
        Stream<LinkkiAspectDefinition> componentDefAspects = AspectAnnotationReader
                .createAspectDefinitionsStreamFrom(componentDefAnnotation);
        Stream<LinkkiAspectDefinition> additionalAspects = getAllAnnotations(annotatedElement)
                .filter(a -> !ComponentAnnotationReader.isComponentDefinition(a))
                .map(AspectAnnotationReader::createAspectDefinitionsFrom)
                .flatMap(List::stream);
        return Stream.concat(componentDefAspects, additionalAspects).collect(Collectors.toList());
    }

    /**
     * Returns linkki annotations present on the given class, as well as annotations on superclasses and
     * interfaces marked with {@link InheritedAspect @InheritedAspect}. Only the first occurrence of an
     * annotation type is included in the result.
     * 
     * Accessible for testing purposes only.
     * 
     * @see LinkkiAspect
     */
    /* private */ static List<Annotation> getUniqueLinkkiAnnotations(Class<?> cls) {
        LinkedList<Annotation> annotations = getAnnotations(cls, false);
        Set<Class<?>> annotationTypes = new HashSet<Class<?>>();

        for (Iterator<Annotation> i = annotations.descendingIterator(); i.hasNext();) {
            Annotation annotation = i.next();
            if (!annotationTypes.contains(annotation.annotationType())) {
                annotationTypes.add(annotation.annotationType());
            } else {
                i.remove();
            }
        }

        return annotations;
    }

    private static LinkedList<Annotation> getAnnotations(Class<?> cls, boolean isParent) {
        LinkedList<Annotation> annotations = new LinkedList<>();

        for (Annotation annotation : cls.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(LinkkiAspect.class)
                    && (!isParent || annotation.annotationType().isAnnotationPresent(InheritedAspect.class))) {
                annotations.add(annotation);
            }
        }

        if (cls.getSuperclass() != null) {
            annotations.addAll(0, getAnnotations(cls.getSuperclass(), true));
        }

        for (Class<?> ifcace : cls.getInterfaces()) {
            annotations.addAll(0, getAnnotations(ifcace, true));
        }

        return annotations;
    }

}
