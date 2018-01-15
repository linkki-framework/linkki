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
package org.linkki.core.binding.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.message.MessageList;

/**
 * {@link AbstractPropertyDispatcherDecorator DispatcherDecorator} that lets
 * {@link PropertyBehavior} instances influence the data as well as the data flow from/to the
 * wrapped dispatcher.
 * <p>
 * This decorator uses all {@link PropertyBehavior} instances provided by the container (dependency
 * injection). All instances are involved equally when deciding the behavior of a property. Boolean
 * return values are evaluated with a logical AND.
 * <p>
 * An example. To decide whether a property is visible, this dispatcher calls
 * {@link PropertyBehavior#isVisible(Object, String)} for all behaviors. The field is visible only
 * if all behaviors return <code>true</code>. If at least one returns <code>false</code>, the
 * property is hidden.
 * <p>
 * In other words behaviors normally return <code>true</code>, but can veto an aspect, by returning
 * <code>false</code>, if they desire to change the behavior.
 */
public class BehaviorDependentDispatcher extends AbstractPropertyDispatcherDecorator {

    private PropertyBehaviorProvider provider;

    public BehaviorDependentDispatcher(PropertyDispatcher wrappedDispatcher,
            PropertyBehaviorProvider provider) {
        super(wrappedDispatcher);
        this.provider = requireNonNull(provider, "provider must not be null");
    }

    /**
     * Checks whether the given property is writable (as defined by the {@link PropertyBehavior
     * behaviors}). Calls setValue() on the wrapped dispatcher only if the property is writable.
     */
    @Override
    public void setValue(@Nullable Object value) {
        if (isConsensus(b -> b.isWritable(getBoundObject(), getProperty()))) {
            super.setValue(value);
        }
    }

    /**
     * Checks whether the given property is writable (as defined by the {@link PropertyBehavior
     * behaviors}). If it is writable, returns the read-only value returned by the wrapped
     * dispatcher. If it is write protected, returns <code>true</code> .
     */
    @Override
    public boolean isReadOnly() {
        if (isConsensus(b -> b.isWritable(getBoundObject(), getProperty()))) {
            return super.isReadOnly();
        } else {
            return true;
        }
    }

    /**
     * Checks whether the given property is visible (as defined by the {@link PropertyBehavior
     * behaviors}). If it is visible, returns the visible value returned by the wrapped dispatcher.
     * If it is hidden, returns <code>false</code>.
     */
    @Override
    public boolean isVisible() {
        if (isConsensus(b -> b.isVisible(getBoundObject(), getProperty()))) {
            return super.isVisible();
        } else {
            return false;
        }
    }

    /**
     * Checks whether the given property shows validation messages (as defined by the
     * {@link PropertyBehavior behaviors}). If it shows messages, returns the messages returned by
     * the wrapped dispatcher. If it hides messages, returns an empty message list.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        if (isConsensus(b -> b.isShowValidationMessages(getBoundObject(), getProperty()))) {
            return super.getMessages(messageList);
        } else {
            return new MessageList();
        }
    }

    /**
     * Returns <code>true</code> if all behaviors return <code>true</code> for the given aspect
     * (e.g. isVisible()), <code>false</code> if at least one returns <code>false</code> (logical
     * AND).
     * <p>
     * Returns <code>true</code> if there are no registered behaviors.
     *
     */
    protected boolean isConsensus(Predicate<PropertyBehavior> aspectIsTrue) {
        if (provider.getBehaviors().isEmpty()) {
            return true;
        }
        return provider.getBehaviors().stream().allMatch(aspectIsTrue);
    }

    @Override
    public String toString() {
        return "BehaviourDependentDispatcher[wrappedDispatcher=" + getWrappedDispatcher() + ", providedBehaviours="
                + provider.getBehaviors() + "]";
    }

}
