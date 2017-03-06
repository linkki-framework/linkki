package org.linkki.core.binding.dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

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
    @CheckForNull
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(@Nullable Object value) {
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
