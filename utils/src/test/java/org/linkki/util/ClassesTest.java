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

package org.linkki.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ClassesTest {

    @Test
    public void testInstantiate() {
        assertThat(Classes.instantiate(NoArg.class), is(instanceOf(NoArg.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstantiate_NoNoArgs() {
        Classes.instantiate(WithArg.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstantiate_PrivateConstructor() {
        Classes.instantiate(PrivateConstructor.class);
    }

    public static class NoArg {
        // default no-arguments constructor
    }

    public static class WithArg {
        public WithArg(@SuppressWarnings("unused") String ignored) {
            // should not be called
        }
    }

    public static class PrivateConstructor {
        private PrivateConstructor() {
            // can't be called
        }
    }

}
