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

package org.linkki.core.binding.validation.message;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ObjectPropertyTest {

    @Test
    public void testHashCode() {
        ObjectProperty objectProperty = new ObjectProperty("foo");
        assertThat(objectProperty.hashCode(), is(objectProperty.hashCode()));
        assertThat(new ObjectProperty("foo").hashCode(), is(new ObjectProperty("foo").hashCode()));
        assertThat(new ObjectProperty("foo", "bar").hashCode(), is(new ObjectProperty("foo", "bar").hashCode()));
        assertThat(new ObjectProperty("foo", "bar", 1).hashCode(), is(new ObjectProperty("foo", "bar", 1).hashCode()));
    }

    @Test
    public void testHashCode_IsCached() {
        HashCodeCounter hashCodeCounter = new HashCodeCounter();
        ObjectProperty objectProperty = new ObjectProperty(hashCodeCounter);
        assertThat(objectProperty.hashCode(), is(objectProperty.hashCode()));
        assertThat(hashCodeCounter.count, is(1));
    }

    @Test
    public void testHasIndex() {
        String object = "foo";
        assertThat(new ObjectProperty(object).hasIndex(), is(false));
        assertThat(new ObjectProperty(object, "chars").hasIndex(), is(false));
        assertThat(new ObjectProperty(object, "chars", 1).hasIndex(), is(true));
    }

    @Test
    public void testEquals() {
        ObjectProperty objectProperty = new ObjectProperty("foo");
        assertThat(objectProperty, is(equalTo(objectProperty)));
        assertThat(new ObjectProperty("foo"), is(equalTo(new ObjectProperty("foo"))));
        assertThat(new ObjectProperty("foo", "bar"), is(equalTo(new ObjectProperty("foo", "bar"))));
        assertThat(new ObjectProperty("foo", "bar", 1), is(equalTo(new ObjectProperty("foo", "bar", 1))));

        assertThat(new ObjectProperty("foo", "bar", 1), is(not(equalTo(new ObjectProperty("foo", "bar", 2)))));
        assertThat(new ObjectProperty("foo", "bar", 1), is(not(equalTo(new ObjectProperty("foo", "bar")))));
        assertThat(new ObjectProperty("foo", "bar"), is(not(equalTo(new ObjectProperty("foo", "bar", 1)))));
        assertThat(new ObjectProperty("foo", "bar", 1), is(not(equalTo(new ObjectProperty("foo")))));
        assertThat(new ObjectProperty("foo"), is(not(equalTo(new ObjectProperty("foo", "bar", 1)))));
        assertThat(new ObjectProperty("foo", "bar"), is(not(equalTo(new ObjectProperty("foo")))));
        assertThat(new ObjectProperty("foo"), is(not(equalTo(new ObjectProperty("foo", "bar")))));
        assertThat(new ObjectProperty("foo", "bar"), is(not(equalTo(new ObjectProperty("foo", "baz")))));
        assertThat(new ObjectProperty("foo", "bar", 1), is(not(equalTo(new ObjectProperty("foo", "baz", 1)))));
        assertThat(new ObjectProperty("foo", "bar", 1), is(not(equalTo(new ObjectProperty("baz", "bar", 1)))));
        assertThat(new ObjectProperty("foo", "bar"), is(not(equalTo(new ObjectProperty("baz", "bar")))));
        assertThat(new ObjectProperty("foo"), is(not(equalTo(new ObjectProperty("baz")))));
        assertThat(new ObjectProperty("foo"), is(not(equalTo("foo"))));
    }

    public static class HashCodeCounter {
        int count;

        @Override
        public int hashCode() {
            count++;
            return 42;
        }
    }

}
