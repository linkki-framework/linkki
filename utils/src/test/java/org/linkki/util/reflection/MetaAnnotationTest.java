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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isPresentOn(annotatedAnnotation)).isTrue();
    }

    @Test
    void testIsPresentOn_Repeatable() {
        assertThat(MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class).isPresentOn(annotatedAnnotation2)).isTrue();
    }

    @Test
    void testIsPresentOn_Not() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isPresentOn(blankAnnotation)).isFalse();
    }

    @Test
    void testIsPresentOn_Null() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isPresentOn(null)).isFalse();
    }

    @Test
    void testIsPresentOnAnyAnnotationOn_Single() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithAnnotatedAnnotation.class)).isTrue();
    }

    @Test
    void testIsPresentOnAnyAnnotationOn_Multiple() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class)).isTrue();
    }

    @Test
    void testIsPresentOnAnyAnnotationOn_None() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(ClassAnnotatedWithBlankAnnotation.class)).isFalse();
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class)
                .isPresentOnAnyAnnotationOn(String.class)).isFalse();
    }

    @Test
    void testFindOn() {
        var metaMarkerAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findOn(annotatedAnnotation);
        assertThat(metaMarkerAnnotation)
                .isPresent()
                .map(MetaMarkerAnnotation::value).hasValue("foo");
    }

    @Test
    void testFindOn_Repeatable_Single() {
        var annotatedAnnotation3 = ClassAnnotatedWithMultipleAnnotatedAnnotations.class
                .getAnnotation(AnnotatedAnnotation3.class);
        var metaMarkerAnnotation = MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class)
                .findOn(annotatedAnnotation3);

        assertThat(metaMarkerAnnotation).isPresent()
                .map(RepeatableMetaMarkerAnnotation::value).hasValue("single");
    }

    @Test
    void testFindOn_Repeatable_Multiple() {
        var metaAnnotation = MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> metaAnnotation.findOn(annotatedAnnotation2))
                .withMessageContaining(AnnotatedAnnotation2.class.getSimpleName())
                .withMessageContaining(RepeatableMetaMarkerAnnotation.class.getSimpleName())
                .withMessageContaining("findAllOn");
    }

    @Test
    void testFindOn_NotPresent() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).findOn(blankAnnotation)).isEmpty();
    }

    @Test
    void testFindAllOn() {
        var metaMarkerAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAllOn(annotatedAnnotation).toList();
        assertThat(metaMarkerAnnotation).hasSize(1);
        assertThat(metaMarkerAnnotation.getFirst().value()).isEqualTo("foo");
    }

    @Test
    void testFindAllOn_Repeatable() {
        var metaMarkerAnnotation = MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class)
                .findAllOn(annotatedAnnotation2).toList();
        assertThat(metaMarkerAnnotation).hasSize(2);
        assertThat(metaMarkerAnnotation.get(0).value()).isEqualTo("baz");
        assertThat(metaMarkerAnnotation.get(1).value()).isEqualTo("bak");
    }

    @Test
    void testFindAnnotatedAnnotationsOn_Single() {
        var annotatedAnnotations = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithAnnotatedAnnotation.class).toList();
        assertThat(annotatedAnnotations).hasSize(1)
                .allSatisfy(annotation -> assertThat(annotation).isInstanceOfAny(AnnotatedAnnotation.class));
    }

    @Test
    void testFindAnnotatedAnnotationsOn_Multiple() {
        var annotatedAnnotations = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class)
                .toList();
        assertThat(annotatedAnnotations).hasSize(2)
                .allSatisfy(annotation -> {
                    assertThat(annotation).isInstanceOfAny(AnnotatedAnnotation.class, AnnotatedAnnotation2.class);
                });
    }

    @Test
    void testFindAnnotatedAnnotationsOn_None() {
        var annotatedAnnotations = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithBlankAnnotation.class).toList();
        assertThat(annotatedAnnotations).isEmpty();
    }

    @Test
    void testOnlyOneOn_Single() {
        var onlyOneOn = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class);
        var optionalAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithAnnotatedAnnotation.class).reduce(onlyOneOn);
        assertThat(optionalAnnotation).isPresent();
    }

    @Test
    void testOnlyOneOn_Multiple() {
        var onlyOneOn = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithMultipleAnnotatedAnnotations.class);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> onlyOneOn.apply(blankAnnotation, annotatedAnnotation))
                .withMessageContaining(ClassAnnotatedWithMultipleAnnotatedAnnotations.class.getSimpleName());
    }

    @Test
    void testOnlyOneOn_None() {
        var onlyOneOn = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .onlyOneOn(ClassAnnotatedWithBlankAnnotation.class);
        var optionalAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class)
                .findAnnotatedAnnotationsOn(ClassAnnotatedWithBlankAnnotation.class).reduce(onlyOneOn);
        assertThat(optionalAnnotation).isEmpty();
    }

    @Test
    void testIsRepeatable() {
        assertThat(MetaAnnotation.of(MetaMarkerAnnotation.class).isRepeatable()).isFalse();
        assertThat(MetaAnnotation.of(RepeatableMetaMarkerAnnotation.class).isRepeatable()).isTrue();
    }

    @Test
    void testOf_NoTarget() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MetaAnnotation.of(NoTargetAnnotation.class))
                .withMessageContaining(NoTargetAnnotation.class.getSimpleName())
                .withMessageContaining(Target.class.getSimpleName());
    }

    @Test
    void testOf_WrongTarget() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MetaAnnotation.of(MethodAnnotation.class))
                .withMessageContaining(MethodAnnotation.class.getSimpleName())
                .withMessageContaining(Target.class.getSimpleName())
                .withMessageContaining(ElementType.METHOD.toString())
                .withMessageContaining(ElementType.ANNOTATION_TYPE.toString());
    }

    @Test
    void testMissingAnnotation() {
        var metaAnnotation = MetaAnnotation.of(MetaMarkerAnnotation.class);

        var exception = metaAnnotation.missingAnnotation(blankAnnotation,
                                                         ClassAnnotatedWithBlankAnnotation.class,
                                                         "checkerMethod")
                .get();

        assertThat(exception)
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
