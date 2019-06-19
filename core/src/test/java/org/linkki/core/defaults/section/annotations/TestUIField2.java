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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.section.annotations.TestUIField2.TestUIField2BoundPropertyCreator;
import org.linkki.core.defaults.section.annotations.TestUIField2.TestUIField2ComponentDefinitionCreator;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBoundProperty(TestUIField2BoundPropertyCreator.class)
@LinkkiComponent(TestUIField2ComponentDefinitionCreator.class)
@LinkkiPositioned
public @interface TestUIField2 {

    /** Mandatory attribute that defines the order in which UI-Elements are displayed */
    @LinkkiPositioned.Position
    int position();

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute() default "";

    class TestUIField2ComponentDefinition implements LinkkiComponentDefinition {
        @Override
        public Object createComponent(Object pmo) {
            return new TestUiComponent();
        }
    }

    class TestUIField2ComponentDefinitionCreator implements ComponentDefinitionCreator<TestUIField2> {
        @Override
        public LinkkiComponentDefinition create(TestUIField2 annotation, AnnotatedElement annotatedElement) {
            return new TestUIField2ComponentDefinition();
        }
    }

    class TestUIField2BoundPropertyCreator implements BoundPropertyCreator<TestUIField2> {
        @Override
        public BoundProperty createBoundProperty(TestUIField2 annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of((Method)annotatedElement).withModelAttribute(annotation.modelAttribute())
                    .withModelObject(annotation.modelObject());
        }
    }
}
