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

package org.linkki.core.binding.descriptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.linkki.test.matcher.Matchers.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.TestComponentClickAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;

public class PropertyElementDescriptorsTest {

    @Test
    public void testGetPosition_WithoutDescriptor() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        assertThat(descriptors.getPosition(), is(0));
    }

    @Test
    public void testGetPosition_FromDescriptor() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        descriptors.addDescriptor(TestUIAnnotation.class,
                                  new ElementDescriptor(10, TestLinkkiComponentDefinition.create(),
                                          BoundProperty.of(TestPmo.SINGLE_PMO_PROPERTY),
                                          Collections.emptyList()),
                                  TestPmo.class);

        assertThat(descriptors.getPosition(), is(10));
    }

    @Test
    public void testGetPmoPropertyName() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        assertThat(descriptors.getPmoPropertyName(), is(TestPmo.SINGLE_PMO_PROPERTY));
    }

    @Test
    public void testGetDescriptor_WithoutDescriptor_NoComponentTypeMethod() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            descriptors.getDescriptor(new TestPmo());
        });

    }

    @Test
    public void testGetDescriptor_WithoutDescriptor_WithComponentTypeMethod() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.DUAL_PMO_PROPERTY);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            descriptors.getDescriptor(new TestPmo());
        });
    }

    @Test
    public void testGetDescriptor_SetDescriptors_FromComponentTypeMethod() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.DUAL_PMO_PROPERTY);

        descriptors.addDescriptor(TestUIAnnotation.class,
                                  new ElementDescriptor(10,
                                          TestLinkkiComponentDefinition.create(() -> "TestUIAnnotation"),
                                          BoundProperty.of(TestPmo.DUAL_PMO_PROPERTY),
                                          Collections.emptyList()),
                                  TestPmo.class);
        descriptors.addDescriptor(AnotherTestUIAnnotation.class,
                                  new ElementDescriptor(10,
                                          TestLinkkiComponentDefinition.create(() -> "AnotherTestUIAnnotation"),
                                          BoundProperty.of(TestPmo.DUAL_PMO_PROPERTY), Collections.emptyList()),
                                  TestPmo.class);

        TestPmo pmo = new TestPmo();
        assertThat(descriptors.getDescriptor(pmo), is(not(nullValue())));
        ElementDescriptor descriptor = descriptors.getDescriptor(pmo);
        assertThat(descriptor.getPosition(), is(10));
        assertThat(descriptor.getBoundProperty().getPmoProperty(), is(TestPmo.DUAL_PMO_PROPERTY));
        assertThat(descriptor.newComponent(pmo), is("AnotherTestUIAnnotation"));
    }

    @Test
    public void testGetDescriptor_Single() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        descriptors.addDescriptor(TestUIAnnotation.class, new ElementDescriptor(10,
                TestLinkkiComponentDefinition.create(() -> "TestUIAnnotation"),
                BoundProperty.of(TestPmo.SINGLE_PMO_PROPERTY),
                Collections.emptyList()), TestPmo.class);

        TestPmo pmo = new TestPmo();
        assertThat(descriptors.getDescriptor(pmo), is(not(nullValue())));
        ElementDescriptor descriptor = descriptors.getDescriptor(pmo);
        assertThat(descriptor.getPosition(), is(10));
        assertThat(descriptor.getBoundProperty().getPmoProperty(), is(TestPmo.SINGLE_PMO_PROPERTY));
        assertThat(descriptor.newComponent(pmo), is("TestUIAnnotation"));
    }

    @Test
    public void testGetDescriptor_AddsAspects() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        descriptors.addDescriptor(TestUIAnnotation.class,
                                  new ElementDescriptor(0, TestLinkkiComponentDefinition.create(),
                                          BoundProperty.of(TestPmo.SINGLE_PMO_PROPERTY), Collections.emptyList()),
                                  TestPmo.class);

        EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.ENABLED);
        descriptors.addAspect(Arrays.<LinkkiAspectDefinition> asList(enabledAspectDefinition));

        assertThat(descriptors.getDescriptor(new TestPmo()).getAspectDefinitions(), contains(enabledAspectDefinition));
    }

    @Test
    public void testAddDescriptor_DualDifferentPosition() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(
                TestPmo.ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            descriptors.addDescriptor(TestUIAnnotation.class,
                                      new ElementDescriptor(30,
                                              TestLinkkiComponentDefinition.create(() -> "TestUIAnnotation"),
                                              BoundProperty
                                                      .of(TestPmo.ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION),
                                              Collections.emptyList()),
                                      TestPmo.class);
            descriptors.addDescriptor(AnotherTestUIAnnotation.class,
                                      new ElementDescriptor(31,
                                              TestLinkkiComponentDefinition.create(() -> "AnotherTestUIAnnotation"),
                                              BoundProperty
                                                      .of(TestPmo.ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION),
                                              Collections.emptyList()),
                                      TestPmo.class);
        });
    }

    @Test
    public void testAddDescriptor_NoComponentTypeMethod() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(
                TestPmo.ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            descriptors.addDescriptor(TestUIAnnotation.class,
                                      new ElementDescriptor(30,
                                              TestLinkkiComponentDefinition.create(() -> "TestUIAnnotation"),
                                              BoundProperty
                                                      .of(TestPmo.ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR),
                                              Collections.emptyList()),
                                      TestPmo.class);
            descriptors.addDescriptor(AnotherTestUIAnnotation.class,
                                      new ElementDescriptor(30,
                                              TestLinkkiComponentDefinition.create(() -> "AnotherTestUIAnnotation"),
                                              BoundProperty
                                                      .of(TestPmo.ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR),
                                              Collections.emptyList()),
                                      TestPmo.class);
        });
    }

    @Test
    public void testAddDescriptor_DifferentProperty() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            descriptors.addDescriptor(TestUIAnnotation.class,
                                      new ElementDescriptor(30,
                                              TestLinkkiComponentDefinition.create(() -> "TestUIAnnotation"),
                                              BoundProperty.of(TestPmo.DUAL_PMO_PROPERTY),
                                              Collections.emptyList()),
                                      TestPmo.class);
        });
    }

    @Test
    public void testAddAspect() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        assertThat(descriptors.getAllAspects(), is(empty()));

        EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.ENABLED);
        descriptors.addAspect(Arrays.<LinkkiAspectDefinition> asList(enabledAspectDefinition));

        assertThat(descriptors.getAllAspects(), contains(enabledAspectDefinition));
    }

    @Test
    public void testIsNotEmpty() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);
        assertThat(descriptors.isNotEmpty(), is(false));

        descriptors.addAspect(Arrays.<LinkkiAspectDefinition> asList(new EnabledAspectDefinition(EnabledType.ENABLED)));
        assertThat(descriptors.isNotEmpty(), is(false));

        descriptors.addDescriptor(TestUIAnnotation.class,
                                  new ElementDescriptor(0, TestLinkkiComponentDefinition.create(),
                                          BoundProperty.of(TestPmo.SINGLE_PMO_PROPERTY), Collections.emptyList()),
                                  TestPmo.class);

        assertThat(descriptors.isNotEmpty());
    }

    @Test
    public void testGetAllAspects() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.DUAL_PMO_PROPERTY);

        EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.ENABLED);
        descriptors.addDescriptor(TestUIAnnotation.class,
                                  new ElementDescriptor(0, TestLinkkiComponentDefinition.create(),
                                          BoundProperty.of(TestPmo.DUAL_PMO_PROPERTY),
                                          Collections.singletonList(enabledAspectDefinition)),
                                  TestPmo.class);
        VisibleAspectDefinition visibleAspectDefinition = new VisibleAspectDefinition(VisibleType.VISIBLE);
        descriptors.addDescriptor(AnotherTestUIAnnotation.class,
                                  new ElementDescriptor(0, TestLinkkiComponentDefinition.create(),
                                          BoundProperty.of(TestPmo.DUAL_PMO_PROPERTY),
                                          Collections.singletonList(visibleAspectDefinition)),
                                  TestPmo.class);

        TestComponentClickAspectDefinition clickAspectDefinition = new TestComponentClickAspectDefinition();
        descriptors.addAspect(Arrays.<LinkkiAspectDefinition> asList(clickAspectDefinition));

        assertThat(descriptors.getAllAspects(),
                   containsInAnyOrder(enabledAspectDefinition, visibleAspectDefinition, clickAspectDefinition));

    }


    public static class TestPmo {

        public static final String SINGLE_PMO_PROPERTY = "singlePmoProperty";

        public static final String DUAL_PMO_PROPERTY = "dualPmoProperty";

        public static final String ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION = "illegalDualPmoPropertyWithDifferentPosition";

        public static final String ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR = "illegalDualPmoPropertyWithoutDifferentiator";

        public static final String DUAL_PMO_PROPERTY_OTHER_LABEL = "other";

        @TestUIAnnotation
        public String getSinglePmoProperty() {
            return "foo";
        }

        @TestUIAnnotation
        @AnotherTestUIAnnotation
        public String getDualPmoProperty() {
            return "bar";
        }

        public Class<? extends Annotation> getDualPmoPropertyComponentType() {
            return AnotherTestUIAnnotation.class;
        }

        @TestUIAnnotation
        @AnotherTestUIAnnotation
        public String getIllegalDualPmoPropertyWithDifferentPosition() {
            return "positions differ";
        }

        public Class<? extends Annotation> getIllegalDualPmoPropertyWithDifferentPositionComponentType() {
            return TestUIAnnotation.class;
        }

        @TestUIAnnotation
        @AnotherTestUIAnnotation
        public String getIllegalDualPmoPropertyWithoutDifferentiator() {
            return "no ...ComponentType() method";
        }
    }

    public @interface TestUIAnnotation {
        // marker interface
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AnotherTestUIAnnotation {
        // marker interface
    }

}