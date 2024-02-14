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

package org.linkki.core.binding.uicreation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.linkki.core.uicreation.ComponentDefinitionCreator;

/**
 * {@link Annotation} to create a {@link LinkkiComponentDefinition}.
 * <p>
 * This is a meta-annotation that means it is applied to another annotation which should be used in
 * client code, for example a PMO class. This might be a UI field annotation but could also be a new
 * annotation that simply defines one component creation.
 */
@Retention(RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface LinkkiComponent {

    Class<? extends ComponentDefinitionCreator<?>> value();
}
