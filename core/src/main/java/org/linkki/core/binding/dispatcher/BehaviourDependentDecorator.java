/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.function.Predicate;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.aspect.InjectablePropertyBehavior;

/**
 * {@link AbstractPropertyDispatcherDecorator Decorator} that lets
 * {@link InjectablePropertyBehavior} instances influence the data as well as the data flow from/to
 * the wrapped dispatcher.
 * <p>
 * This decorator uses all {@link InjectablePropertyBehavior} instances provided by the container
 * (dependency injection). All instances are involved equally when deciding the behavior of a
 * property. Boolean return values are evaluated with a logical AND.
 * <p>
 * An example. To decide whether a property is visible, this decorator calls
 * {@link InjectablePropertyBehavior#isVisible(String)} for all behaviors. The field is visible only
 * if all behaviors return <code>true</code>. If at least one returns <code>false</code>, the
 * property is hidden.
 * <p>
 * In other words behaviors normally return <code>true</code>, but can veto an aspect, by returning
 * <code>false</code>, if they desire to change the behavior.
 *
 * @author widmaier
 */
public class BehaviourDependentDecorator extends AbstractPropertyDispatcherDecorator {

    private PropertyBehaviorProvider provider;

    public BehaviourDependentDecorator(PropertyDispatcher wrappedDispatcher, PropertyBehaviorProvider provider) {
        super(wrappedDispatcher);
        this.provider = provider;
    }

    /**
     * Checks whether the given property is writable (as defined by the
     * {@link InjectablePropertyBehavior behaviors}). Calls setValue() on the wrapped dispatcher
     * only if the property is writable.
     */
    @Override
    public void setValue(String property, Object value) {
        if (isConsensus(b -> b.isWritable(property))) {
            super.setValue(property, value);
        }
    }

    /**
     * Checks whether the given property is writable (as defined by the
     * {@link InjectablePropertyBehavior behaviors}). If it is writable, returns the read-only value
     * returned by the wrapped dispatcher. If it is write protected, returns <code>true</code> .
     */
    @Override
    public boolean isReadonly(String property) {
        if (isConsensus(b -> b.isWritable(property))) {
            return super.isReadonly(property);
        } else {
            return true;
        }
    }

    /**
     * Checks whether the given property is visible (as defined by the
     * {@link InjectablePropertyBehavior behaviors}). If it is visible, returns the visible value
     * returned by the wrapped dispatcher. If it is hidden, returns <code>false</code>.
     */
    @Override
    public boolean isVisible(String property) {
        if (isConsensus(b -> b.isVisible(property))) {
            return super.isVisible(property);
        } else {
            return false;
        }
    }

    /**
     * Checks whether the given property shows validation messages (as defined by the
     * {@link InjectablePropertyBehavior behaviors}). If it shows messages, returns the messages
     * returned by the wrapped dispatcher. If it hides messages, returns an empty message list.
     */
    @Override
    public MessageList getMessages(String property) {
        if (isConsensus(b -> b.isShowValidationMessages(property))) {
            return super.getMessages(property);
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
    protected boolean isConsensus(Predicate<InjectablePropertyBehavior> aspectIsTrue) {
        if (provider == null || provider.getBehaviors() == null) {
            return true;
        }
        return provider.getBehaviors().stream().allMatch(aspectIsTrue);
    }

}
