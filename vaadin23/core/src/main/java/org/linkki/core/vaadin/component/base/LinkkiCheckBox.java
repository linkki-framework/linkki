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

package org.linkki.core.vaadin.component.base;

import com.vaadin.flow.component.checkbox.Checkbox;

/**
 * {@link LinkkiCheckBox} extending vaadin {@link Checkbox} for read only enhancements
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class LinkkiCheckBox extends Checkbox {

    private static final long serialVersionUID = 1L;

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        getElement().setAttribute("readonly", readOnly);
        setEnabled(!readOnly);
    }

}