/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.table;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A supplier for item PMOs based on a list of underlying model objects. The PMOs are created from
 * the model objects via a mapping function. The simple item supplier recreates the list of item
 * PMOs it supplies only, if the underlying model object list changes.
 * <p>
 * You can use this supplier as follows:
 * <p>
 * In your ContainerPMO write a method that returns the list of model objects, e.g.
 * <code>List&lt;Foo&gt; getFoos();</code>. Assuming you can create a fooItem based on a foo object
 * just with <code>new FooItem(foo)</code>, you can create the supplier as follows:
 * 
 * <pre>
 * private SimpleItemSupplier&lt;FooItem, Foo&gt; itemSupplier = new SimpleItemSupplier&lt;&gt;(this::getFoos,
 *         foo -&gt; new FooItem(foo));
 * </pre>
 *
 * @author ortmann
 */
public class SimpleItemSupplier<PMO, MO> implements Supplier<List<PMO>> {

    private List<PMO> items = new ArrayList<>(0);
    @Nullable
    private List<? extends MO> modelObjectsCopy;

    @Nonnull
    private final Supplier<? extends List<? extends MO>> modelObjectSupplier;
    private final Function<MO, PMO> mo2pmoMapping;

    /**
     * Creates a supplier supplying item PMOs.
     * 
     * @param modelObjectSupplier Supplies the underlying model objects.
     * @param mo2pmoMapping A function to create an item PMO based on a model object.
     */
    public SimpleItemSupplier(Supplier<? extends List<? extends MO>> modelObjectSupplier,
            Function<MO, PMO> mo2pmoMapping) {
        this.modelObjectSupplier = requireNonNull(modelObjectSupplier, "modelObjectSupplier must not be null");
        this.mo2pmoMapping = requireNonNull(mo2pmoMapping, "mo2pmoMapping must not be null");
    }

    @Override
    public List<PMO> get() {
        List<? extends MO> actualModelObjects = modelObjectSupplier.get();
        if (actualModelObjects == null) {
            throw new NullPointerException(
                    "modelObjectSupplier must supply a List of model objects (which may be empty)");
        }
        if (hasUnderlyingModelObjectListChanged(actualModelObjects)) {
            modelObjectsCopy = new ArrayList<>(actualModelObjects);
            items = actualModelObjects.stream().map(mo2pmoMapping).collect(Collectors.toList());
        }
        return items;
    }

    private boolean hasUnderlyingModelObjectListChanged(List<? extends MO> modelObjects) {
        return !modelObjects.equals(modelObjectsCopy);
    }

}
