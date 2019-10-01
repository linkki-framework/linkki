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


package org.linkki.tooling.util;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.validator.Messages;

public final class ClassNotFoundMessageUtils {

    private ClassNotFoundMessageUtils() {
        // model binding
    }

    public static void printAnnotationNotFoundWarning(
            Messager messager,
            ExecutableElement methodElement,
            TypeElement uiComponent,
            ElementUtils elementUtils) {
        AnnotationMirror annotationMirror = elementUtils.getAnnotationMirror(methodElement, uiComponent);
        printAnnotationNotFoundWarning(messager,
                                       methodElement,
                                       annotationMirror);
    }

    public static void printAnnotationNotFoundWarning(
            Messager messager,
            ExecutableElement methodElement,
            AnnotationMirror annotationMirror) {

        messager.printMessage(Kind.NOTE,
                              String.format(
                                            Messages.getString("ClassNotFound_warning"),
                                            ((TypeElement)annotationMirror.getAnnotationType().asElement())
                                                    .getQualifiedName().toString(),
                                            annotationMirror),
                              methodElement,
                              annotationMirror);
    }
}
