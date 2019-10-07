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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.util.handler.Handler;

public class AspectAnnotationReaderTest_OrderTest {

    // @formatter:off
    /*
     *   3               Level 3                        |   Horizontal: Interface
     *   |                                              |   Vertical: Parent class/interface
     *   |    5          Level 2 Parent interface        ―――――――――――――――――――――――――――――――――――
     *   |    |
     *   2――――4          Level 2
     *   |
     *   |    7          Level 1 Parent interface
     *   |    |
     *   1――――6――――8     Level 1
     */
     // @formatter:on
    @Test
    public void testAnnotationOrder() {
        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(Level1.class);

        assertThat(annotations.size(), is(8));
        assertThat(annotations.get(0).annotationType(), is(Annotation8.class));
        assertThat(annotations.get(1).annotationType(), is(Annotation7.class));
        assertThat(annotations.get(2).annotationType(), is(Annotation6.class));
        assertThat(annotations.get(3).annotationType(), is(Annotation5.class));
        assertThat(annotations.get(4).annotationType(), is(Annotation4.class));
        assertThat(annotations.get(5).annotationType(), is(Annotation3.class));
        assertThat(annotations.get(6).annotationType(), is(Annotation2.class));
        assertThat(annotations.get(7).annotationType(), is(Annotation1.class));
    }

    // @formatter:off
    /*
     *   3               Level 3                        |   Horizontal: Interface
     *   |                                              |   Vertical: Parent class/interface
     *   |    5          Level 2 Parent interface        ―――――――――――――――――――――――――――――――――――
     *   |    |
     *   2――――4          Level 2
     *   |
     *   |    7          Level 1 Parent interface
     *   |    |
     *   1*―――6*――8      Level 1
     *   |
     *   |
     *   0――――9          Level 0 reuses 1 and 6 on 0 and 6,7,9  on 9
     *
     */
     // @formatter:on
    @Test
    public void testAnnotationOrderWithDuplicates() {
        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(Level0.class);

        assertThat(annotations.size(), is(9));
        assertThat(annotations.get(0).annotationType(), is(Annotation9.class));
        assertThat(annotations.get(1).annotationType(), is(Annotation8.class));
        assertThat(annotations.get(2).annotationType(), is(Annotation7.class));
        assertThat(((Annotation7)annotations.get(2)).value(), is(FirstLevel1ParentInterface.class));
        assertThat(annotations.get(3).annotationType(), is(Annotation5.class));
        assertThat(annotations.get(4).annotationType(), is(Annotation4.class));
        assertThat(annotations.get(5).annotationType(), is(Annotation3.class));
        assertThat(annotations.get(6).annotationType(), is(Annotation2.class));
        assertThat(annotations.get(7).annotationType(), is(Annotation1.class));
        assertThat(((Annotation1)annotations.get(7)).value(), is(Level0.class));
        assertThat(annotations.get(8).annotationType(), is(Annotation6.class));
        assertThat(((Annotation6)annotations.get(8)).value(), is(Level0.class));
    }

    @Test
    public void testAspectOrder() {
        List<DummyLinkkiAspectDefinition> aspects = AspectAnnotationReader.createAspectDefinitionsFor(Level1.class)
                .stream()
                .map(DummyLinkkiAspectDefinition.class::cast)
                .collect(Collectors.toList());

        assertThat(aspects.size(), is(8));
        assertThat(aspects.get(0).getAnnotation().annotationType(), is(Annotation8.class));
        assertThat(aspects.get(1).getAnnotation().annotationType(), is(Annotation7.class));
        assertThat(aspects.get(2).getAnnotation().annotationType(), is(Annotation6.class));
        assertThat(aspects.get(3).getAnnotation().annotationType(), is(Annotation5.class));
        assertThat(aspects.get(4).getAnnotation().annotationType(), is(Annotation4.class));
        assertThat(aspects.get(5).getAnnotation().annotationType(), is(Annotation3.class));
        assertThat(aspects.get(6).getAnnotation().annotationType(), is(Annotation2.class));
        assertThat(aspects.get(7).getAnnotation().annotationType(), is(Annotation1.class));
    }

    @Annotation1(Level0.class)
    @Annotation6(Level0.class)
    class Level0 extends Level1 implements FirstLevel0Interface {
        // empty
    }

    @Annotation7(FirstLevel0Interface.class)
    @Annotation6(FirstLevel0Interface.class)
    @Annotation9(FirstLevel0Interface.class)
    interface FirstLevel0Interface extends FirstLevel1ParentInterface {
        // empty
    }

    @Annotation1(Level1.class)
    class Level1 extends Level2 implements FirstLevel1Interface, SecondLevel1Interface {
        // empty
    }

    @Annotation2(Level2.class)
    class Level2 extends Level3 implements Level2Interface {
        // empty
    }

    @Annotation3(Level3.class)
    class Level3 {
        // empty
    }

    @Annotation6(FirstLevel1Interface.class)
    interface FirstLevel1Interface extends FirstLevel1ParentInterface {
        // empty
    }

    @Annotation7(FirstLevel1ParentInterface.class)
    interface FirstLevel1ParentInterface {
        // empty
    }

    @Annotation8(SecondLevel1Interface.class)
    interface SecondLevel1Interface {
        // empty
    }

    @Annotation4(Level2Interface.class)
    interface Level2Interface extends Level2ParentInterface {
        // empty
    }

    @Annotation5(Level2ParentInterface.class)
    interface Level2ParentInterface {
        // empty
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation1 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation2 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation3 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation4 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation5 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation6 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation7 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation8 {
        Class<?> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(value = Creator.class)
    private @interface Annotation9 {
        Class<?> value();
    }

    public static class Creator implements AspectDefinitionCreator<Annotation> {
        @Override
        public LinkkiAspectDefinition create(Annotation annotation) {
            return new DummyLinkkiAspectDefinition(annotation);
        }
    }

    public static class DummyLinkkiAspectDefinition implements LinkkiAspectDefinition {

        private final Annotation annotation;

        public DummyLinkkiAspectDefinition(Annotation annotation) {
            this.annotation = annotation;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        @Override
        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            throw new UnsupportedOperationException();
        }

    }
}
