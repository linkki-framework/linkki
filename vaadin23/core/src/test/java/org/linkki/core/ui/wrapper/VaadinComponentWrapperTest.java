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

package org.linkki.core.ui.wrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.WrapperType;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class VaadinComponentWrapperTest {

    @ParameterizedTest
    @MethodSource("messages")
    void testSetValidationMessages_ComponentWithValidation_ShowsMessages(Message message, String expectedSeverity) {
        TextField component = new TextField();
        VaadinComponentWrapper componentWrapper = new TestComponentWrapper(component);

        componentWrapper.setValidationMessages(new MessageList(message));

        assertThat(component.isInvalid(), is(true));
        assertThat(component.getElement().getAttribute("severity"), is(expectedSeverity));
        assertThat(component.getErrorMessage(), is(message.getText()));
    }

    @ParameterizedTest
    @MethodSource("messages")
    void testSetValidationMessages_ComponentWithValidation_ReadOnly(Message message, String expectedSeverity) {
        TextField component = new TextField();
        component.setReadOnly(true);
        VaadinComponentWrapper componentWrapper = new TestComponentWrapper(component);

        componentWrapper.setValidationMessages(new MessageList(message));

        assertThat(component.isInvalid(), is(true));
        assertThat(component.getElement().getAttribute("severity"), is(expectedSeverity));
        assertThat(component.getErrorMessage(), is(message.getText()));
    }

    @Test
    void testSetValidationMessages_ComponentWithValidation_ClearsMessages() {
        TextField component = new TextField();
        VaadinComponentWrapper componentWrapper = new TestComponentWrapper(component);
        componentWrapper.setValidationMessages(new MessageList(Message.newError("e", "error text")));

        componentWrapper.setValidationMessages(new MessageList());

        assertThat(component.isInvalid(), is(false));
        assertThat(component.getElement().hasAttribute("severity"), is(false));
        assertThat(component.getErrorMessage(), is(""));
    }

    @ParameterizedTest
    @MethodSource("messages")
    void testSetValidationMessages_ComponentWithoutValidation_ShowsMessages(Message message, String expectedSeverity) {
        Div component = new Div();
        VaadinComponentWrapper componentWrapper = new TestComponentWrapper(component);

        componentWrapper.setValidationMessages(new MessageList(message));

        assertThat(component.getElement().hasAttribute("invalid"), is(true));
        assertThat(component.getElement().getAttribute("severity"), is(expectedSeverity));
    }

    @Test
    void testSetValidationMessages_ComponentWithoutValidation_ClearsMessages() {
        Div component = new Div();
        VaadinComponentWrapper componentWrapper = new TestComponentWrapper(component);
        componentWrapper.setValidationMessages(new MessageList(Message.newError("e", "error text")));

        componentWrapper.setValidationMessages(new MessageList());

        assertThat(component.getElement().hasAttribute("invalid"), is(false));
        assertThat(component.getElement().hasAttribute("severity"), is(false));
    }

    private static Stream<Arguments> messages() {
        return Stream.of(
                         Arguments.of(Message.newError("e", "error text"), "error"),
                         Arguments.of(Message.newWarning("w", "warning text"), "warning"),
                         Arguments.of(Message.newInfo("i", "info text"), "info"));
    }

    static class TestComponentWrapper extends VaadinComponentWrapper {

        private static final long serialVersionUID = 1L;

        public TestComponentWrapper(Component component) {
            super(component, WrapperType.COMPONENT);
        }

        @Override
        public void setLabel(String labelText) {
            throw new UnsupportedOperationException();
        }

    }

}
