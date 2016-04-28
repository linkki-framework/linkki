package org.linkki.core.binding.dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.faktorips.runtime.MessageList;

/**
 * @deprecated Temporary solution. Should be replaced with a regular
 *             {@link BindingAnnotationDispatcher}
 */
@Deprecated
public class ButtonPmoDispatcher extends AbstractPropertyDispatcherDecorator {

    private static final String ON_CLICK_METHOD_NAME = "onClick";

    public ButtonPmoDispatcher(PropertyDispatcher wrappedDispatcher) {
        super(wrappedDispatcher);
    }

    @Override
    public void invoke() {
        Object boundObject = getBoundObject();
        try {
            MethodUtils.invokeExactMethod(boundObject, ON_CLICK_METHOD_NAME);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getValueClass() {
        return Void.class;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
        // do nothing
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public Collection<?> getAvailableValues() {
        return Collections.emptySet();
    }

    @Override
    public MessageList getMessages(MessageList messageList) {
        return new MessageList();
    }

}
