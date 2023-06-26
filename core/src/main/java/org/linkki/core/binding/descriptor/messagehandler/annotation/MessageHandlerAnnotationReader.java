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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.validation.handler.DefaultMessageHandler;
import org.linkki.util.reflection.Classes;
import org.linkki.util.reflection.MetaAnnotation;

/**
 * Searches for meta-annotations of type {@link LinkkiMessages} and creates configured
 * {@link LinkkiMessageHandler}.
 */
public class MessageHandlerAnnotationReader {

    private static final MetaAnnotation<LinkkiMessages> LINKKI_MESSAGES_ANNOTATION = MetaAnnotation
            .of(LinkkiMessages.class);

    private MessageHandlerAnnotationReader() {
        // Utility class
    }

    /**
     * Searches for an annotation on the specified annotated element that is annotated with
     * {@link LinkkiMessages}.
     * <p>
     * There must be at most one configured message handler. If there is no configured message handler,
     * the {@link DefaultMessageHandler} is used instead.
     *
     * @param annotatedElement The annotated element which configures the message handler
     * @return The configured message handler
     */
    public static LinkkiMessageHandler getMessageHandler(AnnotatedElement annotatedElement) {
        return LINKKI_MESSAGES_ANNOTATION
                .findAnnotatedAnnotationsOn(annotatedElement)
                .reduce(LINKKI_MESSAGES_ANNOTATION.onlyOneOn(annotatedElement))
                .map(a -> createHandler(a, annotatedElement))
                .orElse(DefaultMessageHandler.INSTANCE);
    }

    private static <A extends Annotation> LinkkiMessageHandler createHandler(A annotation,
                                                                             AnnotatedElement annotatedElement) {
        @SuppressWarnings("unchecked")
        var creatorClass = (Class<? extends MessageHandlerCreator<A>>)
                annotation.annotationType().getAnnotation(LinkkiMessages.class).value();
        return Classes.instantiate(creatorClass).create(annotation, annotatedElement);
    }

}
