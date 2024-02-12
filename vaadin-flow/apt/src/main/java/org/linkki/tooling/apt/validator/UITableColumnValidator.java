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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.linkki.core.ui.table.column.annotation.UITableColumn;

public class UITableColumnValidator extends MethodAnnotationValidator<UITableColumn> {

    static final String SORTABLE_METHOD_RETURNS_VOID = "UITableColumnValidator.sortableMethodReturnsVoid";
    static final String SORTABLE_RETURN_TYPE_NOT_COMPARABLE = "UITableColumnValidator.sortableReturnTypeNotComparable";

    private static final String PROPERTY_SORTABLE = "sortable";

    public UITableColumnValidator(ProcessingEnvironment env) {
        super(env, UITableColumn.class);
    }

    @Override
    public void validate(UITableColumn annotation, ExecutableElement method) {
        if (annotation.sortable()) {
            if (method.getReturnType().getKind().equals(TypeKind.VOID)) {
                printError(method, PROPERTY_SORTABLE, SORTABLE_METHOD_RETURNS_VOID);
            } else if (!isComparable(method.getReturnType())) {
                printError(method, PROPERTY_SORTABLE, SORTABLE_RETURN_TYPE_NOT_COMPARABLE);
            }
        }
    }

    private boolean isComparable(TypeMirror type) {
        TypeElement comparable = elements().getTypeElement(Comparable.class.getCanonicalName());
        return types().isAssignable(type, types().getDeclaredType(comparable));
    }
}
