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

package org.linkki.core.message;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MsgReplacementParameterTest {

    @Test
    public void testHashCode() {
        assertThat(new MsgReplacementParameter("foo", "bar").hashCode(),
                   is(new MsgReplacementParameter("foo", "bar").hashCode()));
        assertThat(new MsgReplacementParameter("foo", null).hashCode(),
                   is(new MsgReplacementParameter("foo", null).hashCode()));
        // ignores value
        assertThat(new MsgReplacementParameter("foo", "bar").hashCode(),
                   is(new MsgReplacementParameter("foo", "baz").hashCode()));
        assertThat(new MsgReplacementParameter("foo", "bar").hashCode(),
                   is(new MsgReplacementParameter("foo", null).hashCode()));
        assertThat(new MsgReplacementParameter("foo", "bar").hashCode(),
                   is(not(new MsgReplacementParameter("bar", "bar").hashCode())));
    }

    @Test
    public void testEquals() {
        assertThat(new MsgReplacementParameter("foo", "bar"),
                   is(equalTo(new MsgReplacementParameter("foo", "bar"))));
        assertThat(new MsgReplacementParameter("foo", null),
                   is(equalTo(new MsgReplacementParameter("foo", null))));
        assertThat(new MsgReplacementParameter("foo", "bar"),
                   is(not(equalTo(new MsgReplacementParameter("foo", "baz")))));
        assertThat(new MsgReplacementParameter("foo", "bar"),
                   is(not(equalTo(new MsgReplacementParameter("foo", null)))));
        assertThat(new MsgReplacementParameter("foo", null),
                   is(not(equalTo(new MsgReplacementParameter("foo", "baz")))));
        assertThat(new MsgReplacementParameter("foo", "bar"),
                   is(not(equalTo(new MsgReplacementParameter("bar", "bar")))));
    }

}
