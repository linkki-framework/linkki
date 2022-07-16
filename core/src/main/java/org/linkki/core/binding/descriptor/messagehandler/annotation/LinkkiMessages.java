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

package org.linkki.core.binding.descriptor.messagehandler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;

/**
 * Configures a specific {@link LinkkiMessageHandler} to have a special processing of validation
 * messages in a specific binding. This is a meta-annotation that means it is annotated at another
 * annotation to create specific message handler annotations.
 * <p>
 * The annotation is read by the {@link MessageHandlerAnnotationReader}.
 * 
 * @see LinkkiMessageHandler
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LinkkiMessages {

    /**
     * The creator class for the {@link LinkkiMessageHandler}, the class must have a zero argument
     * constructor and must be visible for the framework to instantiate.
     */
    Class<? extends MessageHandlerCreator<?>> value();

}
