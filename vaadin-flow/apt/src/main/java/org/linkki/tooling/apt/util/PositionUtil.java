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

package org.linkki.tooling.apt.util;

import java.util.List;
import java.util.Optional;

import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.core.uicreation.LinkkiPositioned.Position;
import org.linkki.tooling.apt.model.AptAttribute;

/**
 * Util to read the {@link Position @LinkkiPositioned.Position} marked attribute.
 */
public final class PositionUtil {

    private PositionUtil() {
        // util
    }

    /**
     * Returns the {@link Position @LinkkiPositioned.Position} marked attribute if present.
     */
    public static Optional<AptAttribute> findPositionAttribute(List<AptAttribute> attributes) {
        return AptAttribute.findByMetaAnnotation(attributes, LinkkiPositioned.Position.class);
    }
}
