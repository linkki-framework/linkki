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

package org.linkki.core.uicreation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.linkki.test.matcher.Matchers.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.junit.Test;

public class MetaAnnotationReaderTest {

    private final AnnotatedAnnotation annotatedAnnotation = ClassAnnotatedWithAnnotatedAnnotation.class
            .getAnnotation(AnnotatedAnnotation.class);

    private final BlankAnnotation blankAnnotation = ClassAnnotatedWithBlankAnnotation.class
            .getAnnotation(BlankAnnotation.class);

    @Test
    public void testIsMetaAnnotationPresent() {
        assertThat(MetaAnnotationReader.isMetaAnnotationPresent(annotatedAnnotation, MetaMarkerAnnotation.class));
    }

    @Test
    public void testIsMetaAnnotationPresent_Not() {
        assertThat(MetaAnnotationReader.isMetaAnnotationPresent(blankAnnotation, MetaMarkerAnnotation.class),
                   is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_NotAnnotated() {
        MetaAnnotationReader.create(blankAnnotation, ClassAnnotatedWithBlankAnnotation.class,
                                    MetaMarkerAnnotation.class, a -> NoCreator.class,
                                    NoCreator.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_CantCreate() {
        abstract class DefectiveCreator implements ObjectFromAnnotationCreator<AnnotatedAnnotation, String> {
            // can't be instantiated
        }

        MetaAnnotationReader.create(annotatedAnnotation, ClassAnnotatedWithAnnotatedAnnotation.class,
                                    MetaMarkerAnnotation.class,
                                    a -> DefectiveCreator.class,
                                    DefectiveCreator.class);
    }

    @Test
    public void testCreate() {
        assertThat(MetaAnnotationReader.create(annotatedAnnotation, ClassAnnotatedWithAnnotatedAnnotation.class,
                                               MetaMarkerAnnotation.class,
                                               a -> FooCreator.class,
                                               FooCreator.class),
                   is("foo"));
    }

    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface BlankAnnotation {
        // test
    }

    @BlankAnnotation
    static class ClassAnnotatedWithBlankAnnotation {
        // test
    }

    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface MetaMarkerAnnotation {
        // test
    }

    @MetaMarkerAnnotation
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface AnnotatedAnnotation {
        // test
    }

    @AnnotatedAnnotation
    static class ClassAnnotatedWithAnnotatedAnnotation {
        // test
    }

    static class NoCreator implements ObjectFromAnnotationCreator<BlankAnnotation, String> {

        @Override
        public String create(BlankAnnotation annotation, AnnotatedElement annotatedElement) {
            return "nothing";
        }

    }

    public static class FooCreator implements ObjectFromAnnotationCreator<AnnotatedAnnotation, String> {

        @Override
        public String create(AnnotatedAnnotation annotation, AnnotatedElement annotatedElement) {
            return "foo";
        }

    }

}
