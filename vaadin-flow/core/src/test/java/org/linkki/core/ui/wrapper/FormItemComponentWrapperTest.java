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

package org.linkki.core.ui.wrapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.handler.DefaultMessageHandler;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.vaadin.component.base.LabelComponentFormItem;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Basically the same tests as in {@code ElementBindingTest} but focused on the
 * {@link FormItemComponentWrapper}
 **/
class FormItemComponentWrapperTest extends BaseComponentWrapperTest {

    private final NativeLabel label = spy(new NativeLabel());

    private final TextField field = new TextField();
    private final ComboBox<String> selectField = new ComboBox<>();

    private ElementBinding selectBinding;

    private final MessageList messageList = new MessageList();
    private PropertyDispatcher propertyDispatcherValue;

    @BeforeEach
    void setUp() {
        propertyDispatcherValue = new TestPropertyDispatcher("value", Object.class, messageList);

        var propertyDispatcherEnumValue = new TestPropertyDispatcher("enumValue", TestEnum.class, messageList);
        selectBinding = new ElementBinding(new FormItemComponentWrapper(new LabelComponentFormItem(selectField, label)),
                propertyDispatcherEnumValue,
                Handler.NOP_HANDLER,
                new ArrayList<>(), new DefaultMessageHandler());
    }

    @Test
    void testUpdateFromPmo_updateAspect() {
        Handler componentUpdater = mock(Handler.class);
        class TestlinkkiAspectDefinition implements LinkkiAspectDefinition {

            @Override
            public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
                return componentUpdater;
            }

            @Override
            public boolean supports(WrapperType type) {
                return true;
            }
        }
        var aspectDefinition = new TestlinkkiAspectDefinition();
        var fieldBinding = new ElementBinding(
                new FormItemComponentWrapper(new LabelComponentFormItem(field, label)),
                propertyDispatcherValue,
                Handler.NOP_HANDLER, Arrays.asList(aspectDefinition), new DefaultMessageHandler());
        fieldBinding.updateFromPmo();

        verify(componentUpdater).apply();
    }

    @Test
    void testDisplayMessages() {
        messageList.add(Message.newError("code", "text"));

        selectBinding.displayMessages(messageList);

        assertThat(selectField.getErrorMessage()).isEqualTo("text");
    }

    @Test
    void testDisplayMessages_noMessages() {
        selectBinding.displayMessages(messageList);

        assertThat(selectField.getErrorMessage()).isEmpty();
        assertThat(selectField.isInvalid()).isFalse();
    }

    @Test
    void testDisplayMessages_noMessageList() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            selectBinding.displayMessages(null);
        });
    }

    @Test
    void testSetTooltip() {
        TextField formTextField = new TextField();
        LabelComponentFormItem formItem = new LabelComponentFormItem(formTextField, "SomeText");
        FormItemComponentWrapper wrapper = new FormItemComponentWrapper(formItem);

        wrapper.setTooltip("testTip");
        assertThat(getTitleAttribute(wrapper)).isEqualTo("testTip");
        wrapper.setTooltip("<script>");
        assertThat(getTitleAttribute(wrapper)).isEqualTo("");
        wrapper.setTooltip("<div> some text </div>");
        assertThat(getTitleAttribute(wrapper)).isEqualTo(" some text ");
        wrapper.setTooltip("<div> some text <br> with page break</div> ");
        assertThat(getTitleAttribute(wrapper)).isEqualTo(" some text \n with page break ");
    }

    private static class TestPropertyDispatcher implements PropertyDispatcher {

        private final String value;
        private final Class<?> valueClass;
        private final MessageList messages;

        public TestPropertyDispatcher(String value, Class<?> valueClass, MessageList messages) {
            this.value = value;
            this.valueClass = valueClass;
            this.messages = messages;
        }

        @Override
        public String getProperty() {
            return value;
        }

        @CheckForNull
        @Override
        public Object getBoundObject() {
            return null;
        }

        @Override
        public Class<?> getValueClass() {
            return valueClass;
        }

        @Override
        public MessageList getMessages(MessageList messageList) {
            return messages;
        }

        @CheckForNull
        @Override
        public <T> T pull(Aspect<T> aspect) {
            return null;
        }

        @Override
        public <T> void push(Aspect<T> aspect) {

        }

        @Override
        public <T> boolean isPushable(Aspect<T> aspect) {
            return false;
        }
    }
}