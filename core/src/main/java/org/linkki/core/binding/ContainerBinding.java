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

package org.linkki.core.binding;

import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.util.handler.Handler;

/**
 * A container binding represents a {@link Binding} that is itself a {@link BindingContext}. That means
 * it acts as a {@link Binding} within another {@link BindingContext} but also handles other bindings
 * that are included in the corresponding UI container. It gives the ability to structure the bindings
 * in the same hierarchy as the UI is structured.
 * <p>
 * For example a table has bindings for the whole table like the items or the page length. These are
 * aspects that are managed within a {@link ContainerBinding}. The table on the other hand includes
 * multiple fields that are managed as bindings within this {@link ContainerBinding}.
 * <p>
 * The logic of the {@link ContainerBinding} ensures that the aspects of this binding are dealt with
 * before any child {@link Binding} is triggered.
 */
public class ContainerBinding extends BindingContext implements Binding {

    private final Binding binding;

    private final Handler modelChanged;

    /**
     * Creates a new {@link ContainerBinding} that is able to create multiple child bindings.
     * 
     * @param selfBinding The {@link Binding} that contains the aspects that represent this
     *            {@link Binding} itself. The {@link ContainerBinding} will delegate to this binding
     *            whenever it is triggered.
     * @param behaviorProvider The {@link PropertyBehaviorProvider} used to create child bindings
     * @param dispatcherFactory The {@link PropertyDispatcherFactory} used to create child bindings
     * @param modelChanged A {@link Handler} that is applied when {@link #modelChanged()} is called.
     *            Should simply delegate to the parent's {@link BindingContext#modelChanged()}
     */
    public ContainerBinding(Binding selfBinding, PropertyBehaviorProvider behaviorProvider,
            PropertyDispatcherFactory dispatcherFactory, Handler modelChanged) {
        super("container for " + selfBinding.getPmo().getClass().getSimpleName(),
                behaviorProvider, dispatcherFactory, Handler.NOP_HANDLER);
        this.binding = selfBinding;
        this.modelChanged = modelChanged;
    }

    @Override
    public void modelChanged() {
        modelChanged.apply();
    }

    @Override
    public void updateFromPmo() {
        binding.updateFromPmo();
        super.updateFromPmo();
    }

    @Override
    public Object getBoundComponent() {
        return binding.getBoundComponent();
    }

    @Override
    public Object getPmo() {
        return binding.getPmo();
    }

}
