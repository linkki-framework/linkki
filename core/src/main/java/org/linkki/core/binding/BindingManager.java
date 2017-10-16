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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.commons.lang3.Validate;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.MessageList;

import com.vaadin.cdi.ViewScoped;

/**
 * Manages a set of {@link BindingContext}s that are effected by each other.
 */
@ViewScoped
public abstract class BindingManager {

    private final Map<String, BindingContext> contextsByName = new HashMap<>();

    private final CopyOnWriteArrayList<UiUpdateObserver> uiUpdateObservers = new CopyOnWriteArrayList<>();

    private final ValidationService validationService;

    public BindingManager(ValidationService validationService) {
        this.validationService = requireNonNull(validationService, "validationService must not be null");
    }

    /**
     * Creates a new {@link BindingContext} and assigns it to this manager. The class' qualified
     * name is used as context name.
     * 
     * @param clazz the class of which the qualified name is used to identify the
     *            {@linkplain BindingContext} in this manager
     * @see BindingContext
     */
    public BindingContext startNewContext(Class<?> clazz) {
        return startNewContext(clazz.getName());
    }

    /**
     * Creates a new {@link BindingContext} with the given name and assigns it to this
     * {@linkplain BindingManager}.
     * 
     * @param name the name of the {@linkplain BindingContext} that identifies it in this manager
     * @see BindingContext
     */

    public BindingContext startNewContext(String name) {
        requireNonNull(name, "name must not be null");
        Validate.isTrue(!contextsByName.containsKey(name), "BindingManager already contains a BindingContext '%s'.",
                        name);

        BindingContext newContext = newBindingContext(name);
        contextsByName.put(name, newContext);
        return newContext;
    }

    /**
     * Creates a new {@link BindingContext} with the given name. Does not assign the created context
     * to this manager.
     * <p>
     * Note that the created {@linkplain BindingContext} should call {@link #afterUpdateUi()} (e.g.
     * by providing this::afterUpdateUI as a handler) if it is to be added to a manager, so related
     * binding contexts can be notified about UI updates.
     * 
     * @see DefaultBindingManager#newBindingContext(String)
     * @see BindingManager#afterUpdateUi()
     */

    protected abstract BindingContext newBindingContext(String name);

    public Optional<BindingContext> getExistingContext(Class<?> clazz) {
        requireNonNull(clazz, "clazz must not be null");
        return getExistingContext(clazz.getName());
    }

    public Optional<BindingContext> getExistingContext(String name) {
        requireNonNull(name, "name must not be null");
        return Optional.ofNullable(contextsByName.get(name));
    }


    public BindingContext getExistingContextOrStartNewOne(Class<?> clazz) {
        requireNonNull(clazz, "clazz must not be null");
        return getExistingContextOrStartNewOne(clazz.getName());
    }


    public BindingContext getExistingContextOrStartNewOne(String name) {
        requireNonNull(name, "name must not be null");

        BindingContext context = contextsByName.get(name);
        if (context == null) {
            context = startNewContext(name);
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
     * Retrieves the current messages from the validation service and uses them to update the
     * messages in all registered contexts using {@link #updateMessages(MessageList)}. The
     * {@link UiUpdateObserver}s are then notified by {@link #notifyUiUpdateObservers()}.
     * <p>
     * Should be called by all binding contexts after they updated their UI. Will be passed as the
     * after-update handler to the {@link BindingContext} constructor by the
     * {@link DefaultBindingManager}.
     * <p>
     * All overriding methods should call {@link #notifyUiUpdateObservers()} to notify registered
     * {@link UiUpdateObserver}s properly.
     */
    public void afterUpdateUi() {
        MessageList messages = this.validationService.getValidationMessages();


        MessageList sortedMessages = messages.sortByErrorLevel();
        updateMessages(sortedMessages);

        notifyUiUpdateObservers();
    }

    /**
     * Uses the given messages to update all registered binding contexts. Can be overridden in
     * subclasses to notify further observers about the new messages.
     */
    @OverridingMethodsMustInvokeSuper
    protected void updateMessages(MessageList messages) {
        requireNonNull(messages, "messages must not be null");
        contextsByName.values().forEach(bc -> bc.updateMessages(messages));
    }

    /**
     * Notifies all registered {@link UiUpdateObserver}s about UI changes triggered by a managed
     * {@linkplain BindingContext}.
     * <p>
     * If a {@linkplain BindingContext} do not apply {@link #afterUpdateUi()} after UI updates, this
     * method has to be called manually.
     */
    public void notifyUiUpdateObservers() {
        uiUpdateObservers.forEach(o -> o.uiUpdated());
    }

    @Override
    public String toString() {
        return "BindingManager [validationService=" + validationService + ", contextsByName=" + contextsByName.keySet()
                + "]";
    }

}
