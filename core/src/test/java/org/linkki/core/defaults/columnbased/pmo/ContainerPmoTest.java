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
package org.linkki.core.defaults.columnbased.pmo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContainerPmoTest {

    @Test
    public void testGetItemPmoClass() {
        TestContainerPmo testTablePmo = new TestContainerPmo();

        Class<?> itemPmoClass = testTablePmo.getItemPmoClass();

        assertThat(itemPmoClass, is(TestRowPmo.class));
    }

    @Test
    public void testGetItemPmoClass_Indirect() {
        IndirectContainerPmo indirectContainerPmo = new IndirectContainerPmo();

        Class<?> itemPmoClass = indirectContainerPmo.getItemPmoClass();

        assertThat(itemPmoClass, is(SubTestRow.class));
    }

    @Test
    public void testGetItemPmoClass_exception() {
        RawContainerPmo rawContainerPmo = new RawContainerPmo();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            rawContainerPmo.getItemPmoClass();
        });
    }

    @SuppressWarnings("rawtypes")
    private static class RawContainerPmo implements ContainerPmo {

        @Override
        public List getItems() {
            return new ArrayList<>();
        }
    }

    private static class SubTestRow extends TestRowPmo {
        // nothing
    }

    private abstract static class AnotherTestTablePmo<T extends TestRowPmo> implements ContainerPmo<T> {
        // nothing
    }

    private static class IndirectContainerPmo extends AnotherTestTablePmo<SubTestRow> {

        @Override
        public List<SubTestRow> getItems() {
            return Arrays.asList(new SubTestRow());
        }
    }
}
