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
package org.linkki.core.binding.manager;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.Validate;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.MessageList;

import edu.umd.cs.findbugs.annotations.OverrideMustInvoke;

/**
 * Manages a set of {@link BindingContext BindingContexts} that are affected by each other.
 */
public abstract class BindingManager {

    private final Map<String, BindingContext> contextsByName = new HashMap<>();

    private final CopyOnWriteArrayList<UiUpdateObserver> uiUpdateObservers = new CopyOnWriteArrayList<>();

    private final ValidationService validationService;

    public BindingManager(ValidationService validationService) {
        this.validationService = requireNonNull(validationService, "validationService must not be null");
    }

    /**
     * Creates a new {@link BindingContext} and assigns it to this manager. The class' qualified name is
     * used as context name.
     * <p>
     * The {@link BindingContext} can then be retrieved via {@link #getContext(Class)}.
     * 
     * @param clazz the class of which the qualified name is used to identify the
     *            {@linkplain BindingContext} in this manager
     * @param behaviorProvider the {@link PropertyBehaviorProvider} to be used in the
     *            {@link BindingContext}
     * 
     * @throws IllegalArgumentException if there already exists a context for the given class
     * 
     * @see BindingContext
     * @see #getContext(Class)
     * @see DefaultBindingManager#getDefaultBehaviorProvider()
     * @see PropertyBehaviorProvider#append(org.linkki.core.binding.dispatcher.behavior.PropertyBehavior...)
     * @see PropertyBehaviorProvider#prepend(org.linkki.core.binding.dispatcher.behavior.PropertyBehavior...)
     */
    public BindingContext createContext(Class<?> clazz, PropertyBehaviorProvider behaviorProvider) {
        return createContext(clazz.getName(), behaviorProvider);
    }

    /**
     * Creates a new {@link BindingContext} with the given name and assigns it to this
     * {@linkplain BindingManager}.
     * 
     * @param name the name of the {@linkplain BindingContext} that identifies it in this manager
     * 
     * @throws IllegalArgumentException if there already exists a context with the given name
     * 
     * @see BindingContext
     * @see #createContext(String, PropertyBehaviorProvider) createContext(String,
     *      PropertyBehaviorProvider) to start a {@link BindingContext} with a custom
     *      {@link PropertyBehaviorProvider}
     */
    private BindingContext createContext(String name) {
        requireNonNull(name, "name must not be null");
        Validate.isTrue(!contextsByName.containsKey(name), "BindingManager already contains a BindingContext '%s'.",
                        name);

        BindingContext newContext = newBindingContext(name);
        contextsByName.put(name, newContext);
        return newContext;
    }

    /**
     * Creates a new {@link BindingContext} with the given name and assigns it to this
     * {@linkplain BindingManager}.
     * <p>
     * The {@link BindingContext} can then be retrieved via {@link #getContext(String)}.
     * 
     * @param name the name of the {@link BindingContext} that identifies it in this manager
     * @param behaviorProvider the {@link PropertyBehaviorProvider} to be used in the
     *            {@link BindingContext}
     * 
     * @throws IllegalArgumentException if there already exists a context with the given name
     * 
     * @see BindingContext
     * @see #getContext(String)
     * @see DefaultBindingManager#getDefaultBehaviorProvider()
     * @see PropertyBehaviorProvider#append(org.linkki.core.binding.dispatcher.behavior.PropertyBehavior...)
     * @see PropertyBehaviorProvider#prepend(org.linkki.core.binding.dispatcher.behavior.PropertyBehavior...)
     */
    public BindingContext createContext(String name, PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(name, "name must not be null");
        Validate.isTrue(!contextsByName.containsKey(name), "BindingManager already contains a BindingContext '%s'.",
                        name);

        BindingContext newContext = newBindingContext(name, behaviorProvider);
        contextsByName.put(name, newContext);
        return newContext;
    }

    /**
     * Creates a new {@link BindingContext} with the given name, without a specific
     * {@link PropertyBehaviorProvider}. It is up to the implementation to decide which
     * {@link PropertyBehaviorProvider} should be used.
     * 
     * @implNote Implementations have to ensure that the context calls {@link #afterUpdateUi()} if this
     *           manager should be notified of UI updates inside the context. This can be achieved by
     *           passing {@code this::afterUpdateUI} as a
     *           {@link BindingContext#BindingContext(String, PropertyBehaviorProvider, org.linkki.util.handler.Handler)
     *           afterUpdateHandler}.
     * 
     * @see #newBindingContext(String, PropertyBehaviorProvider)
     */
    protected abstract BindingContext newBindingContext(String name);

    /**
     * Creates a new {@link BindingContext} with the given name and the given
     * {@link PropertyBehaviorProvider}.
     * 
     * @implNote Implementations have to ensure that the context calls {@link #afterUpdateUi()} if this
     *           manager should be notified of UI updates inside the context. This can be achieved by
     *           passing {@code this::afterUpdateUI} as a
     *           {@link BindingContext#BindingContext(String, PropertyBehaviorProvider, org.linkki.util.handler.Handler)
     *           afterUpdateHandler}.
     * 
     * @see #newBindingContext(String)
     */
    protected abstract BindingContext newBindingContext(String name, PropertyBehaviorProvider behaviorProvider);

    /**
     * Returns the {@link BindingContext} for the given class' name, creating it if it does not already
     * exist.
     * <p>
     * If you need a custom {@link PropertyBehaviorProvider}, you can start the {@link BindingContext}
     * with {@link #createContext(Class, PropertyBehaviorProvider)} and access it with this method
     * afterwards.
     */
    public BindingContext getContext(Class<?> clazz) {
        requireNonNull(clazz, "clazz must not be null");
        return getContext(clazz.getName());
    }

    /**
     * Returns the {@link BindingContext} for the given name, creating it if it does not already exist.
     * <p>
     * If you need a custom {@link PropertyBehaviorProvider}, you can start the {@link BindingContext}
     * with {@link #createContext(String, PropertyBehaviorProvider)} and access it with this method
     * afterwards.
     */
    public BindingContext getContext(String name) {
        requireNonNull(name, "name must not be null");

        BindingContext context = contextsByName.get(name);
        if (context == null) {
            context = createContext(name);
        }
        return context;
    }

    public void removeContext(BindingContext context) {
        requireNonNull(context, "context must not be null");
        contextsByName.remove(context.getName());
    }

    public void removeAllContexts() {
        contextsByName.clear();
    }

    public void addUiUpdateObserver(UiUpdateObserver observer) {
        requireNonNull(observer, "observer must not be null");
        uiUpdateObservers.addIfAbsent(observer);
    }

    public void removeUiUpdateObserver(UiUpdateObserver observer) {
        requireNonNull(observer, "observer must not be null");
        uiUpdateObservers.remove(observer);
    }

    /**
     * After a {@link BindingContext} updated the UI this method is called to trigger necessary updates.
     * This includes the update of registered {@link UiUpdateObserver update observers} as well as the
     * update of all validation messages in all {@link BindingContext binding contexts}.
     * <p>
     * The {@link UiUpdateObserver}s are then notified by {@link #notifyUiUpdateObservers()}.
     * <p>
     * Current messages are retrieved from the validation service and are forwarded to all registered
     * binding contexts using {@link #updateMessages(MessageList)}.
     * <p>
     * All overriding methods should call {@link #notifyUiUpdateObservers()} to notify registered
     * {@link UiUpdateObserver}s properly.
     */
    public void afterUpdateUi() {
        notifyUiUpdateObservers();
        updateMessages(validationService.getFilteredMessages().sortBySeverity());
    }

    /**
     * Uses the given messages to update all registered binding contexts. Can be overridden in
     * subclasses to notify further observers about the new messages.
     */
    @OverrideMustInvoke
    protected void updateMessages(MessageList messages) {
        requireNonNull(messages, "messages must not be null");
        contextsByName.values().forEach(bc -> bc.displayMessages(messages));
    }

    /**
     * Notifies all registered {@link UiUpdateObserver}s about UI changes triggered by a managed
     * {@linkplain BindingContext}.
     * <p>
     * If a {@linkplain BindingContext} do not apply {@link #afterUpdateUi()} after UI updates, this
     * method has to be called manually.
     */
    public void notifyUiUpdateObservers() {
        uiUpdateObservers.forEach(UiUpdateObserver::uiUpdated);
    }

    /**
     * Updates all managed {@link BindingContext}.
     * <p>
     * Note that this may be a costly operation thus should be used with caution.
     */
    public void updateAll() {
        contextsByName.values().forEach(BindingContext::uiUpdated);
        afterUpdateUi();
    }

    @Override
    public String toString() {
        return "BindingManager [validationService=" + validationService + ", contextsByName=" + contextsByName.keySet()
                + "]";
    }

}
