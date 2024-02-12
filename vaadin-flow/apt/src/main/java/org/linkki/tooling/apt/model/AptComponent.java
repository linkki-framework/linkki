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

package org.linkki.tooling.apt.model;

import java.util.List;

/**
 * A virtual construct that represents a UI component which can be created from multiple
 * {@code @UI-Annotations} (possibly distributed among multiple methods that result in the same
 * property name like {@code void foo()} and {@code int getFoo()}. <br>
 * Components with multiple {@code @UI-Annotations} are known as "dynamic field"). <br>
 * <br>
 *
 * It also is the owner of the "Bind" annotations, like {@code @BindReadOnly} and also the
 * {@link AptAspectMethod AptAspectMethods}. <br>
 * <br>
 * (Normally the {@link AptAspectMethod AptAspectMethods} would be part of
 * {@link AptComponentDeclaration}, but in the case of dynamic fields there are some common aspects,
 * like enabled, visible, required, that they share.)
 */
public class AptComponent {

    private final String propertyName;

    private final List<AptComponentDeclaration> componentDeclarations;

    private final List<AptAspectBinding> aspectBindings;

    public AptComponent(String propertyName, List<AptComponentDeclaration> componentDeclarations,
            List<AptAspectBinding> aspectBindings) {
        this.propertyName = propertyName;
        this.componentDeclarations = componentDeclarations;
        this.aspectBindings = aspectBindings;
    }

    public int getPosition() {
        return getComponentDeclarations().stream()
                .findFirst()
                .orElseThrow(() -> new IndexOutOfBoundsException(
                        "The following component did not have component declarations:\n" + this))
                .getPosition();
    }

    public boolean isDynamicField() {
        return componentDeclarations.size() > 1;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public List<AptComponentDeclaration> getComponentDeclarations() {
        return componentDeclarations;
    }

    public List<AptAspectBinding> getAspectBindings() {
        return aspectBindings;
    }

}
