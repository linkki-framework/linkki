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

package org.linkki.core.uicreation.layout;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.function.Function;

import org.linkki.core.uicreation.MetaAnnotationReader;

/**
 * Reads the annotation {@link LinkkiLayout} to create a {@link LinkkiLayoutDefinition}.
 */
public final class LayoutAnnotationReader {

    private LayoutAnnotationReader() {
        // do not instantiate
    }

    /**
     * Checks whether the given {@link Annotation} is annotated with {@link LinkkiLayout @LinkkiLayout}.
     */
    public static boolean isLayoutDefinition(Annotation annotation) {
        return MetaAnnotationReader.isMetaAnnotationPresent(annotation, LinkkiLayout.class);
    }

    /**
     * Returns the {@link LinkkiLayoutDefinition} that is defined in the {@link LinkkiLayout} annotation
     * that must be present in the given annotation.
     * 
     * @param annotation annotation that defines a {@link LinkkiLayoutDefinition}
     * @return the layout definition
     * @throws IllegalArgumentException if the definition could not be created
     */
    public static <ANNOTATION extends Annotation> LinkkiLayoutDefinition getLayoutDefinition(
            ANNOTATION annotation,
            AnnotatedElement annotatedElement) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        LinkkiLayoutDefinition layoutDefinition = MetaAnnotationReader
                .create(annotation, annotatedElement, LinkkiLayout.class,
                        (Function<LinkkiLayout, Class<? extends LayoutDefinitionCreator>>)LinkkiLayout::value,
                        LayoutDefinitionCreator.class);
        return layoutDefinition;
    }
}
