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
package org.linkki.core.defaults.section.annotations;

import static org.linkki.core.defaults.ui.aspects.types.EnabledType.ENABLED;
import static org.linkki.core.defaults.ui.aspects.types.VisibleType.VISIBLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.TestComponentClickAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.section.annotations.TestUIField.TestUIFieldAspectCreator;
import org.linkki.core.defaults.section.annotations.TestUIField.TestUIFieldBoundPropertyCreator;
import org.linkki.core.defaults.section.annotations.TestUIField.TestUIFieldComponentDefinitionCreator;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiComponent(TestUIFieldComponentDefinitionCreator.class)
@LinkkiAspect(TestUIFieldAspectCreator.class)
@LinkkiBoundProperty(TestUIFieldBoundPropertyCreator.class)
@LinkkiPositioned
public @interface TestUIField {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /** Provides a description label next to the UI element */
    String label() default LinkkiAspectDefinition.DERIVED_BY_LINKKI;

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    /** Defines if an UI-Component is editable, using values of {@link EnabledType} */
    EnabledType enabled() default ENABLED;

    /**
     * Specifies if a component is shown, using values of {@link VisibleType}
     */
    VisibleType visible() default VISIBLE;

    class TestUIFieldAspectCreator implements AspectDefinitionCreator<TestUIField> {

        @Override
        public LinkkiAspectDefinition create(TestUIField annotation) {
            return new CompositeAspectDefinition(new EnabledAspectDefinition(annotation.enabled()),
                    new VisibleAspectDefinition(annotation.visible()), new TestComponentClickAspectDefinition());
        }

    }

    static class TestUIFieldBoundPropertyCreator implements BoundPropertyCreator<TestUIField> {

        @Override
        public BoundProperty createBoundProperty(TestUIField annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement)
                    .withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }

    }

    static class TestUIFieldComponentDefinitionCreator implements ComponentDefinitionCreator<TestUIField> {

        @Override
        public LinkkiComponentDefinition create(TestUIField annotation, AnnotatedElement annotatedElement) {
            return pmo -> new TestUiComponent();
        }
    }

}
