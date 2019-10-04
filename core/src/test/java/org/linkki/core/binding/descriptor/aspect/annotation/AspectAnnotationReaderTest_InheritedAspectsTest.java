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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;

public class AspectAnnotationReaderTest_InheritedAspectsTest {

    @Test
    public void testGetAnnotations_Class() {

        @InheritedAspectAnnotation("first")
        @NormalAnnotation("second")
        class BaseClass {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);

        assertThat(annotations.size(), is(2));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("first"));
        assertThat(annotations.get(1).annotationType(), is(NormalAnnotation.class));
        assertThat(((NormalAnnotation)annotations.get(1)).value(), is("second"));
    }

    @Test
    public void testGetAnnotations_InheritInterface() {

        class BaseClass implements AnnotatedInterface {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);

        assertThat(annotations.size(), is(1));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("interface"));
    }

    @Test
    public void testGetAnnotations_OverrideInterface() {

        @InheritedAspectAnnotation("class")
        class BaseClass implements AnnotatedInterface {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);

        assertThat(annotations.size(), is(1));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("class"));
    }


    @Test
    public void testGetAnnotations_InheritParent() {

        @InheritedAspectAnnotation("parent")
        class ParentClass {
            // empty
        }

        @NormalAnnotation("base")
        class BaseClass extends ParentClass {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);
        System.out.println(annotations);


        assertThat(annotations.size(), is(2));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("parent"));
        assertThat(annotations.get(1).annotationType(), is(NormalAnnotation.class));
        assertThat(((NormalAnnotation)annotations.get(1)).value(), is("base"));
    }

    @Test
    public void testGetAnnotations_OverrideParent() {

        @InheritedAspectAnnotation("parent")
        class ParentClass {
            // empty
        }

        @InheritedAspectAnnotation("base")
        class BaseClass extends ParentClass {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);

        assertThat(annotations.size(), is(1));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("base"));
    }

    @Test
    public void testGetAnnotations_ParentWithInterface() {

        class ParentClass implements AnnotatedInterface {
            // empty
        }

        class BaseClass extends ParentClass {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);

        assertThat(annotations.size(), is(1));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("interface"));
    }

    /** "Normal" = Java's {@link Inherited @Inherited} **/
    @Test
    public void testGetAnnotations_NormalInheritance() {

        @NormalInheritedAnnotation("parent")
        class ParentClass implements AnnotatedInterface {
            // empty
        }

        class BaseClass extends ParentClass {
            // empty
        }

        List<Annotation> annotations = AspectAnnotationReader.getUniqueLinkkiAnnotations(BaseClass.class);

        assertThat(annotations.size(), is(2));
        assertThat(annotations.get(0).annotationType(), is(InheritedAspectAnnotation.class));
        assertThat(annotations.get(1).annotationType(), is(NormalInheritedAnnotation.class));
        assertThat(((InheritedAspectAnnotation)annotations.get(0)).value(), is("interface"));
        assertThat(((NormalInheritedAnnotation)annotations.get(1)).value(), is("parent"));
    }

    @InheritedAspectAnnotation("interface")
    @NormalAnnotation("interface")
    @NormalInheritedAnnotation("interface")
    interface AnnotatedInterface {
        // empty
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @InheritedAspect
    @LinkkiAspect(Creator.class)
    private @interface InheritedAspectAnnotation {
        String value();
    }

    /** "Normal" = Java's {@link Inherited @Inherited} **/
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @LinkkiAspect(Creator.class)
    private @interface NormalInheritedAnnotation {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @LinkkiAspect(Creator.class)
    private @interface NormalAnnotation {
        String value();
    }

    public static class Creator implements AspectDefinitionCreator<Annotation> {
        @Override
        public LinkkiAspectDefinition create(java.lang.annotation.Annotation annotation) {
            throw new IllegalStateException();
        }
    }
}
