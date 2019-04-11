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

package org.linkki.util;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.assertThat;
import static org.linkki.test.matcher.Matchers.present;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.junit.Test;

public class MetaAnnotationTest {

    private final AnnotatedAnnotation annotatedAnnotation = ClassAnnotatedWithAnnotatedAnnotation.class
            .getAnnotation(AnnotatedAnnotation.class);

    private final BlankAnnotation blankAnnotation = ClassAnnotatedWithBlankAnnotation.class
            .getAnnotation(BlankAnnotation.class);

    @Test
    public void testIsPresentOn() {
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class).isPresentOn(annotatedAnnotation));
    }

    @Test
    public void testIsPresentOn_Not() {
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class).isPresentOn(blankAnnotation), is(false));
    }

    @Test
    public void testIsPresentOn_Null() {
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class).isPresentOn(null), is(false));
    }

    @Test
    public void testIsPresentOnAnyAnnotationOn_Single() {
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithAnnotatedAnnotation.class));
    }

    @Test
    public void testIsPresentOnAnyAnnotationOn_Multiple() {
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class));
    }

    @Test
    public void testIsPresentOnAnyAnnotationOn_None() {
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithBlankAnnotation.class), is(false));
        assertThat(new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(String.class), is(false));
    }

    @Test
    public void testGetFrom() {
        MetaMarkerAnnotation metaMarkerAnnotation = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .getFrom(annotatedAnnotation);
        assertNotNull(metaMarkerAnnotation);
        assertThat(metaMarkerAnnotation.value(), is("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFrom_NotPresent() {
        new MetaAnnotation<>(MetaMarkerAnnotation.class).getFrom(blankAnnotation);
    }

    @Test
    public void testFindAnnotatedAnnotationsOn_Single() {
        List<Annotation> annotatedAnnotations = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithAnnotatedAnnotation.class).collect(Collectors.toList());
        assertThat(annotatedAnnotations, hasSize(1));
        assertThat(annotatedAnnotations, contains(instanceOf(AnnotatedAnnotation.class)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindAnnotatedAnnotationsOn_Multiple() {
        List<Annotation> annotatedAnnotations = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class)
                .collect(Collectors.toList());
        assertThat(annotatedAnnotations, hasSize(2));
        assertThat(annotatedAnnotations,
                   contains(instanceOf(AnnotatedAnnotation.class), instanceOf(AnnotatedAnnotation2.class)));
    }

    @Test
    public void testFindAnnotatedAnnotationsOn_None() {
        List<Annotation> annotatedAnnotations = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithBlankAnnotation.class).collect(Collectors.toList());
        assertThat(annotatedAnnotations, is(empty()));
    }

    @Test
    public void testOnlyOneOn_Single() {
        BinaryOperator<Annotation> onlyOneOn = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class);
        Optional<Annotation> optionalAnnotation = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithAnnotatedAnnotation.class).reduce(onlyOneOn);
        assertThat(optionalAnnotation, is(present()));
    }

    @Test
    public void testOnlyOneOn_Multiple() {
        BinaryOperator<Annotation> onlyOneOn = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class);
        try {
            onlyOneOn.apply(blankAnnotation, annotatedAnnotation);
            fail("expected a " + IllegalArgumentException.class.getSimpleName());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
                       containsString(ClassAnnotatedWithMultipleAnnotatedAnnotations.class.getSimpleName()));
        }
    }

    @Test
    public void testOnlyOneOn_None() {
        BinaryOperator<Annotation> onlyOneOn = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithBlankAnnotation.class);
        Optional<Annotation> optionalAnnotation = new MetaAnnotation<>(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithBlankAnnotation.class).reduce(onlyOneOn);
        assertThat(optionalAnnotation, is(absent()));
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
    @Target(ANNOTATION_TYPE)
    public @interface MetaMarkerAnnotation {
        String value();
    }

    @MetaMarkerAnnotation("foo")
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface AnnotatedAnnotation {
        // test
    }

    @MetaMarkerAnnotation("bar")
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface AnnotatedAnnotation2 {
        // test
    }

    @AnnotatedAnnotation
    static class ClassAnnotatedWithAnnotatedAnnotation {
        // test
    }

    @AnnotatedAnnotation
    @AnnotatedAnnotation2
    static class ClassAnnotatedWithMultipleAnnotatedAnnotations {
        // test
    }
}
