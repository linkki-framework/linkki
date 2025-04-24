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

package org.linkki.core.binding.descriptor.aspect.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;

/**
 * {@link Annotation} to add a {@link LinkkiAspectDefinition}. This is a meta-annotation that means
 * it is applied on another annotation which should be used in client code, for example a PMO class.
 * This might be a UI field annotation but could also be a new annotation that simply defines one
 * aspect binding.
 * <p>
 * For example a tool tip aspect might be declared like this:
 * 
 * <pre>
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;Target(value = { ElementType.FIELD, ElementType.METHOD })
 * &#64;LinkkiAspect(BindTooltip.AspectCreator.class)
 * public @interface BindTooltip {
 * 
 *     String text() default StringUtils.EMPTY;
 * 
 *     BindTooltipType tooltipType() default BindTooltipType.STATIC;
 * }
 * </pre>
 * 
 * <p>
 * This annotation can be used multiple times to add different {@link LinkkiAspectDefinition
 * LinkkiAspectDefinitions} to the same annotation. Alternatively, a
 * {@link CompositeAspectDefinition} can be used to create multiple aspects.
 * <p>
 * For further information see {@link Aspect} and {@link LinkkiAspectDefinition}.
 * <p>
 * Use {@link InheritedAspect @InheritedAspect} alongside this annotation to make the aspect
 * inheritable from superclasses and interfaces.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(LinkkiAspects.class)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LinkkiAspect {

    Class<? extends AspectDefinitionCreator<?>> value();

}
