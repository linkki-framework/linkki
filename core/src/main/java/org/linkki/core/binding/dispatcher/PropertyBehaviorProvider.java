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
package org.linkki.core.binding.dispatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.behavior.PropertyBehavior;

@FunctionalInterface
public interface PropertyBehaviorProvider {

    /**
     * A provider that provides no additional / special behavior.
     */
    PropertyBehaviorProvider NO_BEHAVIOR_PROVIDER = Collections::emptyList;

    /**
     * Returns all behaviors relevant for the given context.
     */
    Collection<PropertyBehavior> getBehaviors();

    /**
     * Creates a new {@link PropertyBehaviorProvider} that returns the given behaviors.
     * 
     * @return a new {@link PropertyBehaviorProvider} that returns the given behaviors
     */
    public static PropertyBehaviorProvider with(@NonNull PropertyBehavior... behaviors) {
        List<PropertyBehavior> behaviorsList = Arrays.asList(behaviors);
        return () -> behaviorsList;
    }
}
