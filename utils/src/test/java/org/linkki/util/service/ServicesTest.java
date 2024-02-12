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

package org.linkki.util.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.util.service.ServicesTest.InterfaceWithSingleImplementation.SingleImplementation;

public class ServicesTest {

    @Test
    public void testGetSingleImplementation() {
        assertThat(Services.get(InterfaceWithSingleImplementation.class), is(instanceOf(SingleImplementation.class)));
    }

    @Test
    public void testGetNoImplementation() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Services.get(InterfaceWithoutImplementation.class);
        });

    }

    @Test
    public void testGetMultipleImplementations() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Services.get(InterfaceWithMultipleImplementations.class);
        });

    }

    public interface InterfaceWithoutImplementation {
        // marker

    }

    public interface InterfaceWithSingleImplementation {
        // marker

        public static class SingleImplementation implements InterfaceWithSingleImplementation {
            // only to test if it is found

        }

    }

    public interface InterfaceWithMultipleImplementations {
        // marker

        public static class Implementation1 implements InterfaceWithMultipleImplementations {
            // only to test if it is found

        }

        public static class Implementation2 implements InterfaceWithMultipleImplementations {
            // only to test if it is found

        }

    }

}
