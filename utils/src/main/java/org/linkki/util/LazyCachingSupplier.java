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

import java.util.function.Supplier;

/**
 * A {@link Supplier} that wraps another {@link Supplier} which gets called only once upon the first
 * call to {@link #get()}; afterwards the value is cached.
 */
public class LazyCachingSupplier<T> implements Supplier<T> {

    private Supplier<T> wrappedSupplier;

    private LazyCachingSupplier(Supplier<T> supplier) {
        this.wrappedSupplier = () -> getAndCache(supplier);
    }

    private T getAndCache(Supplier<T> supplier) {
        T value = supplier.get();
        this.wrappedSupplier = () -> value;
        return value;
    }

    @Override
    public T get() {
        return wrappedSupplier.get();
    }

    public static <T> LazyCachingSupplier<T> lazyCaching(Supplier<T> supplier) {
        return new LazyCachingSupplier<>(supplier);
    }
}