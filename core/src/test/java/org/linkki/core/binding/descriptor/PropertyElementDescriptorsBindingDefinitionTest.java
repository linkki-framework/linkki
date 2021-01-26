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

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.PropertyElementDescriptorsBindingDefinitionTest.AnotherTestUIField.AnotherTestUIFieldAspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.TestComponentClickAspectDefinition;
import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.uicreation.LinkkiPositioned;

@SuppressWarnings("deprecation")
public class PropertyElementDescriptorsBindingDefinitionTest {

    @Test
    public void testGetPosition_WithoutDescriptor() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        assertThat(descriptors.getPosition(), is(0));
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
    public void testAddAspect() {
        PropertyElementDescriptors descriptors = new PropertyElementDescriptors(TestPmo.SINGLE_PMO_PROPERTY);

        assertThat(descriptors.getAllAspects(), is(empty()));

        EnabledAspectDefinition enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.ENABLED);
        descriptors.addAspect(Arrays.<LinkkiAspectDefinition> asList(enabledAspectDefinition));

        assertThat(descriptors.getAllAspects(), contains(enabledAspectDefinition));
    }


    public static class TestPmo {

        public static final String SINGLE_PMO_PROPERTY = "singlePmoProperty";

        public static final String DUAL_PMO_PROPERTY = "dualPmoProperty";

        public static final String ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION = "illegalDualPmoPropertyWithDifferentPosition";

        public static final String ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR = "illegalDualPmoPropertyWithoutDifferentiator";

        public static final String DUAL_PMO_PROPERTY_OTHER_LABEL = "other";

        @TestUIField(position = 10, label = SINGLE_PMO_PROPERTY)
        public String getSinglePmoProperty() {
            return "foo";
        }

        @TestUIField(position = 20, label = DUAL_PMO_PROPERTY)
        @AnotherTestUIField(position = 20, label = DUAL_PMO_PROPERTY_OTHER_LABEL)
        public String getDualPmoProperty() {
            return "bar";
        }

        public Class<? extends Annotation> getDualPmoPropertyComponentType() {
            return AnotherTestUIField.class;
        }

        @TestUIField(position = 30, label = ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION)
        @AnotherTestUIField(position = 31, label = ILLEGAL_DUAL_PMO_PROPERTY_WITH_DIFFERENT_POSITION)
        public String getIllegalDualPmoPropertyWithDifferentPosition() {
            return "positions differ";
        }

        public Class<? extends Annotation> getIllegalDualPmoPropertyWithDifferentPositionComponentType() {
            return TestUIField.class;
        }

        @TestUIField(position = 30, label = ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR)
        @AnotherTestUIField(position = 30, label = ILLEGAL_DUAL_PMO_PROPERTY_WITHOUT_DIFFERENTIATOR)
        public String getIllegalDualPmoPropertyWithoutDifferentiator() {
            return "no ...ComponentType() method";
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    // Must be qualified! Otherwise maven would fail to compile
    @org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition(AnotherTestFieldBindingDefinition.class)
    @org.linkki.core.binding.uicreation.LinkkiComponent(org.linkki.core.uicreation.BindingDefinitionComponentDefinition.Creator.class)
    @org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect(AnotherTestUIFieldAspectDefinitionCreator.class)
    @org.linkki.core.uicreation.LinkkiPositioned
    public @interface AnotherTestUIField {

        @LinkkiPositioned.Position
        int position();

        String label();

        String modelObject() default ModelObject.DEFAULT_NAME;

        String modelAttribute() default "";

        EnabledType enabled() default ENABLED;

        VisibleType visible() default VISIBLE;

        class AnotherTestUIFieldAspectDefinitionCreator implements AspectDefinitionCreator<AnotherTestUIField> {

            @Override
            public LinkkiAspectDefinition create(AnotherTestUIField annotation) {
                return new CompositeAspectDefinition(new EnabledAspectDefinition(annotation.enabled()),
                        new VisibleAspectDefinition(annotation.visible()), new TestComponentClickAspectDefinition());
            }

        }

    }

    public static class AnotherTestFieldBindingDefinition implements BindingDefinition {

        private final AnotherTestUIField testUIField;

        public AnotherTestFieldBindingDefinition(AnotherTestUIField testUIField) {
            this.testUIField = requireNonNull(testUIField, "testUIField must not be null");
        }

        @Override
        public TestUiComponent newComponent() {
            return new TestUiComponent();
        }

        @Override
        public String label() {
            return testUIField.label();
        }

        @Override
        public EnabledType enabled() {
            return testUIField.enabled();
        }

        @Override
        public RequiredType required() {
            return RequiredType.NOT_REQUIRED;
        }

        @Override
        public VisibleType visible() {
            return testUIField.visible();
        }

        @Override
        public String modelAttribute() {
            return testUIField.modelAttribute();
        }

        @Override
        public String modelObject() {
            return testUIField.modelObject();
        }
    }
}
