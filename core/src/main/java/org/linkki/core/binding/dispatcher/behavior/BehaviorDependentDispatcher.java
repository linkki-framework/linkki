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
package org.linkki.core.binding.dispatcher.behavior;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.AbstractPropertyDispatcherDecorator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.util.function.TriPredicate;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * {@link AbstractPropertyDispatcherDecorator DispatcherDecorator} that lets
 * {@link PropertyBehavior} instances influence the data as well as the data flow from/to the
 * wrapped dispatcher.
 * <p>
 * Boolean return values are evaluated with a logical AND.
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
     * Checks whether the given property shows validation messages (as defined by the
     * {@link PropertyBehavior behaviors}). If it shows messages, returns the messages returned by
     * the wrapped dispatcher. If it hides messages, returns an empty message list.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        if (isConsensus(forBoundObjectAndProperty(PropertyBehavior::isShowValidationMessages))) {
            return super.getMessages(messageList);
        } else {
            return new MessageList();
        }
    }

    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "that's why we use requireNonNull")
    private Predicate<PropertyBehavior> forBoundObjectAndProperty(
            TriPredicate<PropertyBehavior, Object, String> triPredicate) {
        Object boundObject = requireNonNull(getBoundObject(), "boundObject must not be null");
        String property = getProperty();
        return b -> triPredicate.test(b, boundObject, property);
    }

    /**
     * Returns <code>true</code> if all behaviors return <code>true</code> for the given aspect
     * (e.g. isVisible()), <code>false</code> if at least one returns <code>false</code> (logical
     * AND).
     * <p>
     * Returns <code>true</code> if there are no registered behaviors.
     */
    protected boolean isConsensus(Predicate<PropertyBehavior> aspectIsTrue) {
        if (provider.getBehaviors().isEmpty()) {
            return true;
        }
        return provider.getBehaviors().stream().allMatch(aspectIsTrue);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegates to the wrapped dispatcher except for boolean valued aspect. In case of boolean
     * valued aspects, this dispatcher only delegates to the wrapped dispatcher if all
     * {@link PropertyBehavior PropertyBehaviors} return <code>true</code> for the aspect. Otherwise
     * this method returns <code>false</code>.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T pull(Aspect<T> aspect) {
        if (aspect.getName().equals(VisibleAspectDefinition.NAME) &&
                !isConsensus(forBoundObjectAndProperty(PropertyBehavior::isVisible))) {
            return (T)Boolean.FALSE;
        } else {
            return super.pull(aspect);
        }
    }

    @Override
    public <T> boolean isPushable(Aspect<T> aspect) {
        if (aspect.getName().equals(LinkkiAspectDefinition.VALUE_ASPECT_NAME)
                && !isConsensus(forBoundObjectAndProperty(PropertyBehavior::isWritable))) {
            return false;
        } else {
            return super.isPushable(aspect);
        }
    }

}
