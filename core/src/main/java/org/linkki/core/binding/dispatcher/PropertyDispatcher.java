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
package org.linkki.core.binding.dispatcher;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.validation.message.MessageList;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Provides field information for an arbitrary property through an unified interface.
 * <p>
 * Each aspects value can be set to the model by using {@link #pull(Aspect)}, and retrieved by
 * {@link #push(Aspect)}. {{@link #push(Aspect)} can only be used if {@link #isPushable(Aspect)}
 * returns {@code true}. Additionally, validation messages can be retrieved with
 * {@linkplain #getMessages(MessageList)}, which are not handled by aspects.
 */
public interface PropertyDispatcher {

    /**
     * @return the name of the property
     */
    String getProperty();

    /**
     * @return the model object containing the property.
     */
    @CheckForNull
    Object getBoundObject();

    /**
     * Retrieves the value class for the property. i.e. the return type of the getter method.
     *
     * @return the value class of the property
     * @throws IllegalArgumentException if the property is not available.
     */
    Class<?> getValueClass();

    /**
     * Retrieves the validation messages for the property.
     *
     * @param messageList the existing messages from previous model object validation
     * @return the list of messages
     */
    MessageList getMessages(MessageList messageList);

    /**
     * Pulls the value for the given {@link Aspect} from this dispatcher.
     * <p>
     * The given {@link Aspect} may have a {@link Aspect#getValue() value}. It is up to the decision
     * of this dispatcher to use this value or to provide another value instead.
     * <p>
     * Note: This method may return <code>null</code> if the aspect is designed to accept
     * <code>null</code> value.
     * 
     * @param aspect the {@link Aspect} that is requested from this dispatcher
     * @return value of the given {@link Aspect} according to this dispatcher
     */
    @CheckForNull
    <T> T pull(Aspect<T> aspect);

    /**
     * Push the value of the given aspect. If the {@link Aspect} contains a value this value should
     * be set to the model. If it does not have a value an appropriate method without argument
     * should be invoked.
     * 
     * @param aspect an aspect with the name of the aspect and the value if a value should be
     *            stored.
     *
     * @throws IllegalArgumentException if the property is not available
     */
    <T> void push(Aspect<T> aspect);

    /**
     * Checks whether {@link #push(Aspect)} can be called that means the value could be stored in
     * the model respectively an appropriate method could be called.
     * 
     * @param aspect The aspect which should be checked
     * @return <code>true</code> if the aspect could be pushed
     */
    <T> boolean isPushable(Aspect<T> aspect);

}
