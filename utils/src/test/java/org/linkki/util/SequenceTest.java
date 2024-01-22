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


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SequenceTest {
    private static final String DUMMY_ARG = "dummy";
    private static final String FAKE_ARG = "fake";

    @Test
    public void testConstructor_listIsNotMutableFromOutside() {
        var abc = new ArrayList<String>();
        abc.add(null);
        abc.add(DUMMY_ARG);

        Sequence<String> sequence = new Sequence<>(abc);
        assertThat(sequence.list()).containsExactly(null, DUMMY_ARG);

        abc.clear();

        assertThat(sequence.list()).containsExactly(null, DUMMY_ARG);
    }

    @Test
    public void testOfCollection() {
        Collection<String> collection = new ArrayList<>();
        collection.add(DUMMY_ARG);
        collection.add(null);
        var sequence = Sequence.of(collection);

        assertThat(sequence.list()).hasSize(2);
        assertThat(sequence.list()).containsExactly(DUMMY_ARG, null);
    }


    @Test
    public void testStreamAndCollect() {
        var sequence = Sequence.of(1, 2, 3);
        assertThat(sequence.list()).containsExactly(1, 2, 3);

        var collect = sequence.stream().collect(Sequence.collect());
        assertThat(collect.list()).containsExactly(1, 2, 3);

        var filtered = sequence.stream().filter(x -> x < 2).collect(Sequence.collect());
        assertThat(filtered.list()).containsExactly(1);
    }

    @Test
    public void testEmpty() {
        Sequence<Object> sequence = Sequence.empty();
        assertThat(sequence.list()).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testWith_VarArgs() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);

        assertThat(sequence.list()).hasSize(1);
        assertThat(sequence.list()).doesNotContain(FAKE_ARG);

        sequence = sequence.with(FAKE_ARG);

        assertThat(sequence.list()).hasSize(2);
        assertThat(sequence.list()).contains(DUMMY_ARG, FAKE_ARG);
    }

    @Test
    public void testNullvalues() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG, null);

        assertThat(sequence.list()).hasSize(2);
        assertThat(sequence.list()).doesNotContain(FAKE_ARG);

        sequence = sequence.with(null, null);

        assertThat(sequence.list()).hasSize(4);
        assertThat(sequence.list()).contains(DUMMY_ARG, null, null, null);
    }

    @Test
    public void testWith_Sequence() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);

        assertThat(sequence.list()).hasSize(1);
        assertThat(sequence.list()).doesNotContain(FAKE_ARG);

        Sequence<String> newSequence = Sequence.of(FAKE_ARG);
        sequence = sequence.with(newSequence);

        assertThat(sequence.list()).hasSize(2);
        assertThat(sequence.list()).contains(DUMMY_ARG, FAKE_ARG);
    }

    @Test
    public void testIfWith_SupplierVarArgs_true() {
        var sequence = Sequence.of(DUMMY_ARG).withIf(true, () -> FAKE_ARG, () -> null);

        assertThat(sequence.list()).hasSize(3);
        assertThat(sequence.list()).containsExactly(DUMMY_ARG, FAKE_ARG, null);
    }

    @Test
    public void testIfWith_Supplier_false() {
        var sequence = Sequence.of(DUMMY_ARG).withIf(false, () -> FAKE_ARG);

        assertThat(sequence.list()).hasSize(1);
        assertThat(sequence.list()).containsExactly(DUMMY_ARG);
    }


    @Test
    public void testToString() {
        Sequence<Integer> sequence = Sequence.of(1, 2, 3);
        assertThat(sequence.toString()).isEqualTo("[1, 2, 3]");
    }

    @Test
    public void testToString_ensureNoNPEifEmpty() {
        assertThat(Sequence.empty().toString()).isEqualTo("[]");
    }

    @Test
    public void testWith_Collection() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);
        sequence = sequence.with(List.of(FAKE_ARG));

        assertThat(sequence.list()).hasSize(2);
        assertThat(sequence.list()).contains(DUMMY_ARG, FAKE_ARG);
    }

    @Test
    public void testWithNewElementsFrom() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG);
        sequence = sequence.withNewElementsFrom(Arrays.asList(FAKE_ARG, DUMMY_ARG));

        assertThat(sequence.list()).hasSize(2);
        assertThat(sequence.list()).contains(DUMMY_ARG, FAKE_ARG);
    }

    @Test
    public void testList_returnsUnmodifiableList() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG, FAKE_ARG);
        assertThatThrownBy(() -> sequence.list().add("foo")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testList_returnsValuesInOrderOfSequence() {
        Sequence<String> sequence = Sequence.of(DUMMY_ARG, FAKE_ARG);
        List<String> list = sequence.list();
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isEqualTo(DUMMY_ARG);
        assertThat(list.get(1)).isEqualTo(FAKE_ARG);
    }

}
