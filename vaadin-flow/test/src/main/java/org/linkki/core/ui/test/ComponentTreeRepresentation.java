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

package org.linkki.core.ui.test;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;

import com.vaadin.flow.component.Component;

public class ComponentTreeRepresentation implements Representation {

    private final StandardRepresentation fallback = new StandardRepresentation();

    @Override
    public String toStringOf(Object object) {
        if (object instanceof Component component) {
            return KaribuUtils.getComponentTree(component);
        } else {
            return fallback.toStringOf(object);
        }
    }
}
