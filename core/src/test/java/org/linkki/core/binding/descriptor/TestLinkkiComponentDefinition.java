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

package org.linkki.core.binding.descriptor;

import java.util.function.Supplier;

import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

public class TestLinkkiComponentDefinition implements LinkkiComponentDefinition {

    private Supplier<Object> componentCreator;
    private int position;

    private TestLinkkiComponentDefinition(Supplier<Object> componentCreator, int position) {
        this.componentCreator = componentCreator;
        this.position = position;
    }

    @Override
    public Object createComponent(Object pmo) {
        return componentCreator.get();
    }

    @Override
    public int getPosition() {
        return position;
    }

    public static TestLinkkiComponentDefinition withPosition(int position) {
        return new TestLinkkiComponentDefinition(() -> new Object(), position);
    }

    public static TestLinkkiComponentDefinition create(Supplier<Object> componentCreator, int position) {
        return new TestLinkkiComponentDefinition(componentCreator, position);
    }
}