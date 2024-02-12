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

package org.linkki.util.reflection;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.assertThat;
import static org.linkki.test.matcher.Matchers.present;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MetaAnnotationTest {

    private final AnnotatedAnnotation annotatedAnnotation = ClassAnnotatedWithAnnotatedAnnotation.class
            .getAnnotation(AnnotatedAnnotation.class);

    private final AnnotatedAnnotation2 annotatedAnnotation2 = ClassAnnotatedWithMultipleAnnotatedAnnotations.class
            .getAnnotation(AnnotatedAnnotation2.class);

    private final BlankAnnotation blankAnnotation = ClassAnnotatedWithBlankAnnotation.class
            .getAnnotation(BlankAnnotation.class);

    @Test
    void testIsPresentOn() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isPresentOn(annotatedAnnotation));
    }

    @Test
    void testIsPresentOn_Repeatable() {
        assertThat(MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class).isPresentOn(annotatedAnnotation2));
    }

    @Test
    void testIsPresentOn_Not() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isPresentOn(blankAnnotation), is(false));
    }

    @Test
    void testIsPresentOn_Null() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isPresentOn(null), is(false));
    }

    @Test
    void testIsPresentOnAnyAnnotationOn_Single() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithAnnotatedAnnotation.class));
    }

    @Test
    void testIsPresentOnAnyAnnotationOn_Multiple() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class));
    }

    @Test
    void testIsPresentOnAnyAnnotationOn_None() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithBlankAnnotation.class), is(false));
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(String.class), is(false));
    }

    @Test
    void testFindOn() {
        Optional<MetaMarkerAnnotation> metaMarkerAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findOn(annotatedAnnotation);
        assertThat(metaMarkerAnnotation.isPresent());
        assertThat(metaMarkerAnnotation.get().value(), is("foo"));
    }

    @Test
    void testFindOn_Repeatable_Single() {
        AnnotatedAnnotation3 annotatedAnnotation3 = ClassAnnotatedWithMultipleAnnotatedAnnotations.class
                .getAnnotation(AnnotatedAnnotation3.class);
        Optional<RepeatableMetaMarkerAnnotation> metaMarkerAnnotation = MetaAnnotation.of(
                                                                                          RepeatableMetaMarkerAnnotation.class)
                .findOn(annotatedAnnotation3);

        assertThat(metaMarkerAnnotation.isPresent());
        assertThat(metaMarkerAnnotation.get().value(), is("single"));
    }

    @Test
    void testFindOn_Repeatable_Multiple() {
        try {
            MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class).findOn(annotatedAnnotation2);
            fail("expected an " + IllegalArgumentException.class.getSimpleName());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(AnnotatedAnnotation2.class.getSimpleName()));
            assertThat(e.getMessage(), containsString(RepeatableMetaMarkerAnnotation.class.getSimpleName()));
            assertThat(e.getMessage(), containsString("findAllOn"));
        }
    }

    @Test
    void testFindOn_NotPresent() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).findOn(blankAnnotation), is(absent()));
    }

    @Test
    public void testFindAllOn() {
        List<MetaMarkerAnnotation> metaMarkerAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAllOn(annotatedAnnotation).collect(Collectors.toList());
        assertThat(metaMarkerAnnotation, hasSize(1));
        assertThat(metaMarkerAnnotation.get(0).value(), is("foo"));
    }

    @Test
    void testFindAllOn_Repeatable() {
        List<RepeatableMetaMarkerAnnotation> metaMarkerAnnotation = MetaAnnotation.of(
                                                                                      RepeatableMetaMarkerAnnotation.class)
                .findAllOn(annotatedAnnotation2).collect(Collectors.toList());
        assertThat(metaMarkerAnnotation, hasSize(2));
        assertThat(metaMarkerAnnotation.get(0).value(), is("baz"));
        assertThat(metaMarkerAnnotation.get(1).value(), is("bak"));
    }

    @Test
    void testFindAnnotatedAnnotationsOn_Single() {
        List<Annotation> annotatedAnnotations = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithAnnotatedAnnotation.class).collect(Collectors.toList());
        assertThat(annotatedAnnotations, hasSize(1));
        assertThat(annotatedAnnotations, contains(instanceOf(AnnotatedAnnotation.class)));
    }

    @Test
    void testFindAnnotatedAnnotationsOn_Multiple() {
        List<Annotation> annotatedAnnotations = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class)
                .collect(Collectors.toList());
        assertThat(annotatedAnnotations, hasSize(2));
        assertThat(annotatedAnnotations,
                   contains(instanceOf(AnnotatedAnnotation.class), instanceOf(AnnotatedAnnotation2.class)));
    }

    @Test
    void testFindAnnotatedAnnotationsOn_None() {
        List<Annotation> annotatedAnnotations = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithBlankAnnotation.class).collect(Collectors.toList());
        assertThat(annotatedAnnotations, is(empty()));
    }

    @Test
    void testOnlyOneOn_Single() {
        BinaryOperator<Annotation> onlyOneOn = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class);
        Optional<Annotation> optionalAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithAnnotatedAnnotation.class).reduce(onlyOneOn);
        assertThat(optionalAnnotation, is(present()));
    }

    @Test
    void testOnlyOneOn_Multiple() {
        BinaryOperator<Annotation> onlyOneOn = MetaAnnotation.of(MetaMarkerAnnotation.class)
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
    void testOnlyOneOn_None() {
        BinaryOperator<Annotation> onlyOneOn = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithBlankAnnotation.class);
        Optional<Annotation> optionalAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithBlankAnnotation.class).reduce(onlyOneOn);
        assertThat(optionalAnnotation, is(absent()));
    }

    @Test
    void testIsRepeatable() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isRepeatable(), is(false));
        assertThat(MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class).isRepeatable());
    }

    @Test
    void testOf_NoTarget() {
        try {
            MetaAnnotation.of(NoTargetAnnotation.class);
            fail("expected a " + IllegalArgumentException.class.getSimpleName());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(NoTargetAnnotation.class.getSimpleName()));
            assertThat(e.getMessage(), containsString(Target.class.getSimpleName()));
        }
    }

    @Test
    void testOf_WrongTarget() {
        try {
            MetaAnnotation.of(MethodAnnotation.class);
            fail("expected a " + IllegalArgumentException.class.getSimpleName());
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(MethodAnnotation.class.getSimpleName()));
            assertThat(e.getMessage(), containsString(Target.class.getSimpleName()));
            assertThat(e.getMessage(), containsString(ElementType.METHOD.toString()));
            assertThat(e.getMessage(), containsString(ElementType.ANNOTATION_TYPE.toString()));
        }
    }

    @Test
    void testMissingAnnotation() {
        var metaAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class);

        var exception = metaAnnotation.missingAnnotation(blankAnnotation,
                                                         ClassAnnotatedWithBlankAnnotation.class,
                                                         "checkerMethod")
                .get();

        Assertions.assertThat(exception)
                .hasMessageContaining("checkerMethod")
                .hasMessageContaining(ClassAnnotatedWithBlankAnnotation.class.toString())
                .hasMessageContaining(blankAnnotation.annotationType().getSimpleName())
                .hasMessageContaining(MetaMarkerAnnotation.class.getSimpleName());
    }

    public @interface NoTargetAnnotation {
        // test
    }

    @Retention(RUNTIME)
    @Target(METHOD)
    public @interface MethodAnnotation {
        // test
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

    @Retention(RUNTIME)
    @Target(ANNOTATION_TYPE)
    @Repeatable(RepeatableMetaMarkerAnnotations.class)
    public @interface RepeatableMetaMarkerAnnotation {
        String value();
    }

    @Retention(RUNTIME)
    @Target(ANNOTATION_TYPE)
    public @interface RepeatableMetaMarkerAnnotations {
        RepeatableMetaMarkerAnnotation[] value();
    }

    @MetaMarkerAnnotation("foo")
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface AnnotatedAnnotation {
        // test
    }

    @MetaMarkerAnnotation("bar")
    @RepeatableMetaMarkerAnnotation("baz")
    @RepeatableMetaMarkerAnnotation("bak")
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface AnnotatedAnnotation2 {
        // test
    }

    @RepeatableMetaMarkerAnnotation("single")
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface AnnotatedAnnotation3 {
        // test
    }

    @AnnotatedAnnotation
    static class ClassAnnotatedWithAnnotatedAnnotation {
        // test
    }

    @AnnotatedAnnotation
    @AnnotatedAnnotation2
    @AnnotatedAnnotation3
    static class ClassAnnotatedWithMultipleAnnotatedAnnotations {
        // test
    }
}
