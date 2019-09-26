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

import java.util.List;
import java.util.Optional;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

import org.linkki.tooling.model.AptAttribute;
import org.linkki.tooling.model.AptPmo;

/**
 * Some utility for better use of the model classes.
 */
public final class ModelUtils {

    private ModelUtils() {
        // util
    }

    public static boolean isAbstractType(AptPmo pmo) {
        return pmo.getElement().getKind() == ElementKind.INTERFACE
                || pmo.getElement().getModifiers().contains(Modifier.ABSTRACT);
    }

    /**
     * Tries to find an {@link AptAttribute} with a given nam.
     * 
     * @param attributes the attributes
     * @param name the name of the attribute
     * @return the attribute with the given name and type, or {@link Optional#empty()}
     */
    public static Optional<AptAttribute> findAttribute(
            List<AptAttribute> attributes,
            String name) {
        return attributes.stream()
                .filter(it -> it.getName().equals(name))
                .findFirst();
    }
}
