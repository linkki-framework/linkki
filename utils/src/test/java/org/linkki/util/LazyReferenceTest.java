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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class LazyReferenceTest {

    @Test
    public void testSuppliersGetIsNotCalledOnInitialization() {
        LazyReference.lazy(() -> {
            fail("Should never be called");
            return "never";
        });
    }

    @Test
    public void testSuppliersGetIsOnlyCalledOnce() {
        AtomicInteger i = new AtomicInteger(0);
        Supplier<Integer> lazyCachingSupplier = LazyReference.lazy(i::incrementAndGet);

        assertThat(lazyCachingSupplier.get(), is(1));
        assertThat(lazyCachingSupplier.get(), is(1));
        assertThat(lazyCachingSupplier.get(), is(1));
        assertThat(i.get(), is(1));
    }

    @Test
    public void testIsValuePresent() {
        AtomicInteger i = new AtomicInteger(0);
        LazyReference<Integer> lazyReference = LazyReference.lazy(i::incrementAndGet);
        lazyReference.get();

        assertThat(lazyReference.isValuePresent(), is(true));
    }

    @Test
    public void testIsValuePresent_DoesNotCallSupplier() {
        LazyReference<?> lazyReference = LazyReference.lazy(() -> fail());

        assertThat(lazyReference.isValuePresent(), is(false));
    }

}
