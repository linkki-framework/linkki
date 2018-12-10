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

package org.linkki.util;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Test;

public class SequenceTest {
    private static final String DUMMY_ARG = "dummy";
    private static final String FAKE_ARG = "fake";

    @Test
    public void testEmpty() {
        Sequence<Object> sequence = Sequence.empty();
        assertThat(sequence.list(), is(Collections.emptyList()));
    }

    @Test
    public void testWith_VarArgs() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);

        assertThat(sequence.list().size(), is(1));
        assertThat(sequence.list(), hasItem(not(FAKE_ARG)));

        sequence = sequence.with(FAKE_ARG);

        assertThat(sequence.list().size(), is(2));
        assertThat(sequence.list(), hasItems(DUMMY_ARG, FAKE_ARG));
    }

    @Test
    public void testWith_Sequence() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);

        assertThat(sequence.list().size(), is(1));
        assertThat(sequence.list(), hasItem(not(FAKE_ARG)));

        Sequence<String> newSequence = Sequence.of(FAKE_ARG);
        sequence = sequence.with(newSequence);

        assertThat(sequence.list().size(), is(2));
        assertThat(sequence.list(), hasItems(DUMMY_ARG, FAKE_ARG));
    }
}
