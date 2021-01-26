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
package org.linkki.core.binding.descriptor.bindingdefinition.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.UIElementAnnotationReader;

/**
 * Marks an annotation used to mark a UI element created by linkki, such as {@code @UILabel} or
 * {@code @UITextField}.
 * <p>
 * Every such annotation is accompanied by a
 * {@link org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition} which in turn is used
 * by the {@link UIElementAnnotationReader} to create the actual UI element based on the annotation.
 * 
 * @deprecated since 1.4.0 because this concept was replaced. See "Custom UI element annotation" at
 *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
 *             more information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Deprecated
public @interface LinkkiBindingDefinition {

    /**
     * The {@link org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition} used to
     * implement the annotated Annotation.
     * 
     * @see org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition
     */
    Class<? extends org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition> value();
}
