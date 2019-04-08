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

package org.linkki.samples.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.section.annotations.aspect.FieldValueAspectDefinition;
import org.linkki.samples.binding.annotation.BindValue.BindFieldValueAspectDefinitionCreator;
import org.linkki.samples.binding.annotation.BindValue.BindValueAnnotationBoundPropertyCreator;
import org.linkki.util.BeanUtils;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiBoundProperty(BindValueAnnotationBoundPropertyCreator.class)
@LinkkiAspect(BindFieldValueAspectDefinitionCreator.class)
public @interface BindValue {

    String pmoProperty() default "";

    String modelObject() default ModelObject.DEFAULT_NAME;

    String modelAttribute() default "";

    class BindValueAnnotationBoundPropertyCreator implements BoundPropertyCreator<BindValue> {

        @Override
        public BoundProperty createBoundProperty(BindValue annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of(getPmoProperty(annotation, annotatedElement))
                    .withModelObject(annotation.modelObject())
                    .withModelAttribute(annotation.modelAttribute());
        }

        private String getPmoProperty(BindValue bindAnnotation, AnnotatedElement annotatedElement) {
            String pmoProperty = bindAnnotation.pmoProperty();
            if (StringUtils.isEmpty(pmoProperty)) {
                if (annotatedElement instanceof Method) {
                    pmoProperty = BeanUtils.getPropertyName((Method)annotatedElement);
                } else if (annotatedElement instanceof Field) {
                    pmoProperty = ((Field)annotatedElement).getName();
                } else {
                    throw new IllegalArgumentException("The @" + BindValue.class.getSimpleName()
                            + " annotation only supports reading the property name from " + Field.class.getSimpleName()
                            + "s and " + Method.class.getSimpleName() + "s");
                }
            }
            return pmoProperty;
        }

    }

    static class BindFieldValueAspectDefinitionCreator implements AspectDefinitionCreator<BindValue> {

        @Override
        public LinkkiAspectDefinition create(BindValue annotation) {
            return new FieldValueAspectDefinition();
        }

    }

}