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

package org.linkki.tooling.apt.validator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;

import org.linkki.core.ui.aspects.annotation.BindClearButton;

public class BindClearButtonValidator extends MethodAnnotationValidator<BindClearButton> {

    static final String BIND_CLEAR_BUTTON_WITH_PRIMITIVE = "BindClearButtonValidator.primitiveReturnType";

    private final ProcessingEnvironment env;

    public BindClearButtonValidator(ProcessingEnvironment env) {
        super(env, BindClearButton.class);
        this.env = env;
    }

    @Override
    public void validate(BindClearButton annotation, ExecutableElement method) {
        if (method.getReturnType().getKind().isPrimitive()) {
            var message = Messages.getString(BIND_CLEAR_BUTTON_WITH_PRIMITIVE);
            env.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
        }
    }
}
