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

package org.linkki.core.binding.descriptor.messagehandler.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;

/**
 * Implementations of this class are specific for an annotation which is annotated with
 * {@link LinkkiMessages @LinkkiMessages}. The implementation must have a zero argument constructor
 * and must be public for the framework to be instantiated.
 *
 * @see LinkkiMessages
 * @see LinkkiMessageHandler
 */
@FunctionalInterface
public interface MessageHandlerCreator<A extends Annotation> {

    LinkkiMessageHandler create(A annotation, AnnotatedElement annotatedElement);

}
