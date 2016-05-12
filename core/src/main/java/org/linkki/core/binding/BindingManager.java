package org.linkki.core.binding;

import static com.google.gwt.thirdparty.guava.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.validation.ValidationService;

/**
 * Manages a set of binding contexts.
 */
public abstract class BindingManager {

    private Map<String, BindingContext> contextsByName = new HashMap<>();
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
        checkState(!contextsByName.containsKey(name), "BindingManager already contains a BindingContext '%s'.", name);
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
     * Should be called by all binding contexts after they updated their UI. Will be passed as the
     * after-update handler to the {@link BindingContext} constructor by the
     * {@link DefaultBindingManager}.
     */
    public void afterUpdateUi() {
        MessageList messages = this.validationService.getValidationMessages();
        contextsByName.values().forEach(bc -> bc.updateMessages(messages));
    }

    @Override
    public String toString() {
        return "BindingManager [validationService=" + validationService + ", contextsByName=" + contextsByName.keySet()
                + "]";
    }
}
