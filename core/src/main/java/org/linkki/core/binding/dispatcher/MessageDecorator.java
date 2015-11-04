/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import org.faktorips.runtime.IModelObject;
import org.faktorips.runtime.MessageList;
import org.faktorips.runtime.ValidationContext;

/**
 * Provides validation messages for a given model object.
 *
 * @author widmaier
 */
public class MessageDecorator extends AbstractPropertyDispatcherDecorator {

    private final Object modelObject;
    private MessageList cachedMessageList = new MessageList();

    public MessageDecorator(PropertyDispatcher wrappedDispatcher, Object modelObject) {
        super(wrappedDispatcher);
        this.modelObject = modelObject;
    }

    /**
     * Returns an empty message list if the wrapped model object is not an {@link IModelObject}, or
     * if no given property exists.
     * <p>
     * Otherwise validates the {@link IModelObject}. Returns all messages for the respective model
     * object property.
     */
    @Override
    public MessageList getMessages(String property) {
        if (property == null) {
            return new MessageList();
        }
        return cachedMessageList.getMessagesFor(getModelObject(), property);
    }

    private Object getModelObject() {
        return modelObject;
    }

    @Override
    public void prepareUpdateUI() {
        cachedMessageList = validate();
    }

    private MessageList validate() {
        if (canValidate()) {
            return validateModelObject();
        } else {
            return new MessageList();
        }
    }

    private MessageList validateModelObject() {
        // FIPM-33 Warum einen neuen ValidationContext erzeugen?
        // Es kann m.E. ja durchaus kunden-spezifische IValidationContext-Implementierungen geben.
        // Von daher würde ich dne zu verwendenden eher im Konstruktor als Dependency des Decorators
        // sehen
        return ((IModelObject)getModelObject()).validate(new ValidationContext());
    }

    private boolean canValidate() {
        return getModelObject() instanceof IModelObject;
    }

}
