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

package org.linkki.core.binding.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WrapperTypeTest {

    @Test
    public void testIsAssignableFrom() {
        assertTrue(WrapperType.ROOT.isAssignableFrom(WrapperType.ROOT));
        assertTrue(WrapperType.ROOT.isAssignableFrom(WrapperType.COMPONENT));
        assertTrue(WrapperType.ROOT.isAssignableFrom(WrapperType.FIELD));
        assertTrue(WrapperType.ROOT.isAssignableFrom(WrapperType.LAYOUT));
        assertTrue(WrapperType.ROOT.isAssignableFrom(WrapperType.of("custom")));
        assertTrue(WrapperType.ROOT.isAssignableFrom(WrapperType.of("customComponent", WrapperType.COMPONENT)));
        assertTrue(WrapperType.COMPONENT.isAssignableFrom(WrapperType.COMPONENT));
        assertTrue(WrapperType.COMPONENT.isAssignableFrom(WrapperType.LAYOUT));
        assertTrue(WrapperType.COMPONENT.isAssignableFrom(WrapperType.FIELD));

        assertFalse(WrapperType.FIELD.isAssignableFrom(WrapperType.COMPONENT));
        assertFalse(WrapperType.FIELD.isAssignableFrom(WrapperType.ROOT));
        assertFalse(WrapperType.of("custom").isAssignableFrom(WrapperType.COMPONENT));
        assertFalse(WrapperType.of("custom", WrapperType.COMPONENT).isAssignableFrom(WrapperType.COMPONENT));
    }

    @Test
    public void testIsRoot() {
        assertTrue(WrapperType.ROOT.isRoot());

        assertFalse(WrapperType.COMPONENT.isRoot());
        assertFalse(WrapperType.FIELD.isRoot());
        assertFalse(WrapperType.of("foo", null).isRoot());
        assertFalse(WrapperType.of("", null).isRoot());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals() {
        assertTrue(WrapperType.ROOT.equals(WrapperType.ROOT));
        assertTrue(WrapperType.ROOT.equals(WrapperType.of("", null)));
        assertTrue(WrapperType.of("", null).equals(WrapperType.ROOT));
        assertFalse(WrapperType.ROOT.equals(WrapperType.COMPONENT));
        assertFalse(WrapperType.COMPONENT.equals(WrapperType.ROOT));
        assertTrue(WrapperType.COMPONENT.equals(WrapperType.COMPONENT));
        assertTrue(WrapperType.COMPONENT.equals(WrapperType.of("component", WrapperType.ROOT)));
        assertTrue(WrapperType.of("component", WrapperType.ROOT).equals(WrapperType.COMPONENT));
        assertFalse(WrapperType.COMPONENT.equals(WrapperType.FIELD));
        assertFalse(WrapperType.FIELD.equals(WrapperType.COMPONENT));
        assertFalse(WrapperType.FIELD.equals("field"));
        assertFalse(WrapperType.FIELD.equals(WrapperType.of("field", WrapperType.FIELD)));
        assertFalse(WrapperType.FIELD.equals(WrapperType.of("field", null)));
        assertFalse(WrapperType.of("field", null).equals(WrapperType.FIELD));
    }

    @Test
    public void testHashcode() {
        assertThat(WrapperType.ROOT.hashCode(), is(WrapperType.ROOT.hashCode()));
        assertThat(WrapperType.ROOT.hashCode(), is(WrapperType.of("", null).hashCode()));
        assertThat(WrapperType.of("", null).hashCode(), is(WrapperType.ROOT.hashCode()));
        assertThat(WrapperType.ROOT.hashCode(), is(not((WrapperType.COMPONENT.hashCode()))));
        assertThat(WrapperType.COMPONENT.hashCode(), is(not((WrapperType.ROOT.hashCode()))));
        assertThat(WrapperType.COMPONENT.hashCode(), is(WrapperType.COMPONENT.hashCode()));
        assertThat(WrapperType.COMPONENT.hashCode(), is(WrapperType.of("component", WrapperType.ROOT).hashCode()));
        assertThat(WrapperType.of("component", WrapperType.ROOT).hashCode(), is(WrapperType.COMPONENT.hashCode()));
        assertThat(WrapperType.COMPONENT.hashCode(), is(not((WrapperType.FIELD.hashCode()))));
        assertThat(WrapperType.FIELD.hashCode(), is(not((WrapperType.COMPONENT.hashCode()))));
        assertThat(WrapperType.FIELD.hashCode(), is(not(("field".hashCode()))));
        assertThat(WrapperType.FIELD.hashCode(), is(not((WrapperType.of("field", WrapperType.FIELD).hashCode()))));
        assertThat(WrapperType.FIELD.hashCode(), is(not((WrapperType.of("field", null).hashCode()))));
        assertThat(WrapperType.of("field", null).hashCode(), is(not((WrapperType.FIELD.hashCode()))));
    }

}
