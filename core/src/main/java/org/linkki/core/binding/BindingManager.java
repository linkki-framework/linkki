package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.commons.lang3.Validate;
import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.util.MessageListUtil;

import com.vaadin.cdi.ViewScoped;

/**
 * Manages a set of binding contexts.
 */
@ViewScoped
public abstract class BindingManager {

    private final Map<String, BindingContext> contextsByName = new HashMap<>();

    private final ValidationService validationService;

    public BindingManager(@Nonnull ValidationService validationService) {
        this.validationService = requireNonNull(validationService, "ValidationService must not be null.");
    }

    /**
     * Starts a new binding context and uses the class' qualified name as context name.
     */
    public BindingContext startNewContext(Class<?> clazz) {
        return startNewContext(clazz.getName());
    }

    public BindingContext startNewContext(String name) {
        Validate.isTrue(!contextsByName.containsKey(name), "BindingManager already contains a BindingContext '%s'.",
                        name);
        BindingContext newContext = newBindingContext(name);
        contextsByName.put(name, newContext);
        return newContext;
    }

    protected abstract BindingContext newBindingContext(String name);

    public Optional<BindingContext> getExistingContext(Class<?> clazz) {
        return getExistingContext(clazz.getName());
    }

    public Optional<BindingContext> getExistingContext(String name) {
        return Optional.ofNullable(contextsByName.get(name));
    }

    public BindingContext getExistingContextOrStartNewOne(Class<?> clazz) {
        return getExistingContextOrStartNewOne(clazz.getName());
    }

    public BindingContext getExistingContextOrStartNewOne(String name) {
        BindingContext context = contextsByName.get(name);
        if (context == null) {
            context = startNewContext(name);
        }
        return context;
    }

    public void removeContext(BindingContext context) {
        contextsByName.remove(context.getName());
    }

    public void removeAllContexts() {
        contextsByName.clear();
    }

    /**
     * Retrieves the current messages from the validation service and uses them to update the
     * messages in all registered contexts.
     * <p>
     * Should be called by all binding contexts after they updated their UI. Will be passed as the
     * after-update handler to the {@link BindingContext} constructor by the
     * {@link DefaultBindingManager}.
     */
    public void afterUpdateUi() {
        MessageList messages = this.validationService.getValidationMessages();
        updateMessages(MessageListUtil.sortBySeverity(messages));
    }

    /**
     * Uses the given messages to update all registered binding contexts. Can be overridden in
     * subclasses to notify further observers about the new messages.
     */
    @OverridingMethodsMustInvokeSuper
    protected void updateMessages(MessageList messages) {
        contextsByName.values().forEach(bc -> bc.updateMessages(messages));
    }

    @Override
    public String toString() {
        return "BindingManager [validationService=" + validationService + ", contextsByName=" + contextsByName.keySet()
                + "]";
    }

}
