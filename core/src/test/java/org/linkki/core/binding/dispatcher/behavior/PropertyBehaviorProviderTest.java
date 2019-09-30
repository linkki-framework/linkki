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

package org.linkki.core.binding.dispatcher.behavior;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class PropertyBehaviorProviderTest {

    private PropertyBehavior readOnly = PropertyBehavior.readOnly();
    private PropertyBehavior invisible = PropertyBehavior.visible(() -> false);

    @Test
    public void testWith() {
        PropertyBehaviorProvider propertyBehaviorProvider = PropertyBehaviorProvider.with(readOnly, invisible);

        assertThat(propertyBehaviorProvider.getBehaviors(), contains(readOnly, invisible));
    }

    @Test
    public void testAppend() {
        PropertyBehaviorProvider propertyBehaviorProvider = PropertyBehaviorProvider.with(readOnly).append(invisible);

        assertThat(propertyBehaviorProvider.getBehaviors(), contains(readOnly, invisible));
    }

    @Test
    public void testPrepend() {
        PropertyBehaviorProvider propertyBehaviorProvider = PropertyBehaviorProvider.with(readOnly).prepend(invisible);

        assertThat(propertyBehaviorProvider.getBehaviors(), contains(invisible, readOnly));
    }

}
