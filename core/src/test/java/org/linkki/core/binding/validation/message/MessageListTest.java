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
package org.linkki.core.binding.validation.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.util.validation.ValidationMarker;

class MessageListTest {

    private static final String ANY = "couldn't care less";

    private Message msg1;
    private Message msg2;
    private Message msg3;

    private MessageList msgList1;

    @BeforeEach
    void setUp() {

        var invalidObjectProperty1 = new ObjectProperty("A", "testProperty");
        var invalidObjectProperty2 = new ObjectProperty("B", "anotherTestProperty");
        var invalidObjectProperty3 = new ObjectProperty("C", "higherIndexProperty", 2);

        msg1 = Message.builder("Test1", Severity.INFO).invalidObject(invalidObjectProperty1).create();
        msg2 = Message.builder("Test2", Severity.INFO).invalidObject(invalidObjectProperty2).create();
        msg3 = Message.builder("Test3", Severity.INFO).invalidObject(invalidObjectProperty3).create();

        msgList1 = new MessageList(msg1);
        msgList1.add(msg2);
        msgList1.add(msg3);
    }

    @Test
    void testGetMessagesFor_shouldAddMessage() {
        assertThat(msgList1.getMessagesFor("A", "testProperty", -1).getMessage(0)).isEqualTo(msg1);
        assertThat(msgList1.getMessagesFor("B", "anotherTestProperty", -1).getMessage(0)).isEqualTo(msg2);
        assertThat(msgList1.getMessagesFor("B", null, -1).getMessage(0)).isEqualTo(msg2);
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", 2).getMessage(0)).isEqualTo(msg3);
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", -1).getMessage(0)).isEqualTo(msg3);
    }

    @Test
    void testGetMessagesFor_shouldNotAddMessage() {

        assertThat(msgList1.getMessagesFor("A", "anotherTestProperty", -1)).isEmpty();
        assertThat(msgList1.getMessagesFor("B", "anotherTestProperty", 1)).isEmpty();
        assertThat(msgList1.getMessagesFor("C", "higherIndexProperty", 1)).isEmpty();
    }

    @Test
    void testGetMessagesByMarker_withValidationMarker() {
        ValidationMarker marker = () -> false;

        var messages = new MessageList(Message.builder("msg1", Severity.INFO).create(),
                Message.builder("msgWithMarker", Severity.WARNING).markers(marker).create());

        var messagesByMarker = messages.getMessagesByMarker(marker);
        assertThat(messagesByMarker).isNotSameAs(messages).hasSize(1);
        assertThat(messagesByMarker.getMessage(0)).isEqualTo(messages.getMessage(1));
    }

    @Test
    void testGetMessagesByMarker_markerNull_shouldReturnAllMessagesWithoutMarker() {

        var messages = new MessageList(Message.builder("msg1", Severity.INFO).create(),
                Message.builder("msgWithMarker", Severity.WARNING).markers(() -> false).create());

        var messagesByMarker = messages.getMessagesByMarker((ValidationMarker)null);
        assertThat(messagesByMarker).isNotSameAs(messages).hasSize(1);
        assertThat(messagesByMarker.getMessage(0)).isEqualTo(messages.getMessage(0));
    }

    @Test
    void testGetMessagesByMarker_predicate() {

        var messages = new MessageList(Message.builder("msgWithoutMarker", Severity.INFO).create(),
                Message.builder("msgWithMatchingMarker", Severity.WARNING).markers(() -> true).create(),
                Message.builder("msgWithNonMathingMarker", Severity.ERROR).markers(() -> false).create());

        var messagesByMarker = messages.getMessagesByMarker(ValidationMarker::isRequiredInformationMissing);
        assertThat(messagesByMarker).isNotSameAs(messages).hasSize(1);
        assertThat(messagesByMarker.getMessage(0)).isEqualTo(messages.getMessage(1));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void testGetMessagesByMarker_predicateNull_shouldThrowNullPointerException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new MessageList().getMessagesByMarker((Predicate)null));
    }

    @Test
    void testGetMessageWithHighestSeverity() {

        var messages = new MessageList(Message.builder("info", Severity.INFO).create(),
                Message.builder("error", Severity.ERROR).create(),
                Message.builder("warning", Severity.WARNING).create());

        var message = messages.getMessageWithHighestSeverity();

        assertThat(message).isPresent()
                .map(Message::getSeverity).hasValue(Severity.ERROR);

    }

    @Test
    void testGetMessageWithHighestSeverity_null() {

        var messages = new MessageList();

        var message = messages.getMessageWithHighestSeverity();
        assertThat(message).isEmpty();

    }

    @SuppressWarnings({ "unused" })
    @Test
    void testNewMessageList_null_shouldThrowNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> new MessageList((Message[])null));
    }

    @Test
    void testNewMessageList_empty() {
        assertThat(new MessageList()).isEmpty();
    }

    @Test
    void testNewMessageList() {
        var m1 = Message.newError("error", "error");
        var m2 = Message.newWarning("warning", "warning");
        assertThat(new MessageList(m1, m2)).containsExactly(m1, m2);
    }

    @Test
    void testSortBySeverity() {
        var e1 = Message.newError("e1", "E1");
        var e2 = Message.newError("e2", "E2");
        var e3 = Message.newError("e3", "E3");
        var w1 = Message.newWarning("w1", "W1");
        var w2 = Message.newWarning("w2", "W2");
        var i1 = Message.newInfo("i1", "I1");
        var i2 = Message.newInfo("i2", "I2");
        var unsortedMessageList = new MessageList(i2, e1, w1, e3, i1, e2, w2);
        var sortedMessageList = new MessageList(e1, e3, e2, w1, w2, i2, i1);

        var actualMessageList = unsortedMessageList.sortBySeverity();

        assertThat(actualMessageList).isEqualTo(sortedMessageList);
    }

    @Test
    void testGetText() {
        var messageList = new MessageList(Message.newInfo("don't care", "we care"),
                Message.newError("don't care", "even more"));
        assertThat(messageList.getText()).isEqualTo("we care\neven more");
    }

    @Test
    void testGetText_OneMessageList() {
        var messageList = new MessageList(Message.newInfo("don't care", "we care"));
        assertThat(messageList.getText()).isEqualTo("we care");
    }

    @Test
    void testGetText_emptyList_shouldReturnEmptyString() {
        var emptyMessageList = new MessageList();
        assertThat(emptyMessageList.getText()).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void testGetMessagesFor_noObjects_shouldReturnEmptyMessageList() {
        var messages = new MessageList(Message.newError("code", "msg"),
                Message.newWarning("code", "msg"))
                        .getMessagesFor(new Object());

        assertThat(messages).isEmpty();
    }

    @Test
    void testGetMessagesFor_objectNull_shouldThrowNullPointerException() {
        var messageList = new MessageList(Message.newError("code", "msg"), Message.newWarning("code", "msg"));

        assertThatNullPointerException().isThrownBy(() -> messageList.getMessagesFor(null));
    }

    @Test
    void testGetMessagesFor_nullProperty_shouldFindMessagesWithEmptyProperty() {
        var o = new Object();
        var m = Message.builder("1", Severity.ERROR).code("1").invalidObject(new ObjectProperty(o)).create();
        var messages = new MessageList(m);

        assertThat(messages.getMessagesFor(o)).hasSize(1);
        assertThat(messages.getMessagesFor(o).getMessage(0)).isEqualTo(m);
        assertThat(messages.getMessagesFor(o, null)).hasSize(1);
        assertThat(messages.getMessagesFor(o, null).getMessage(0)).isEqualTo(m);
        assertThat(messages.getMessagesFor(o, "")).hasSize(1);
        assertThat(messages.getMessagesFor(o, "").getMessage(0)).isEqualTo(m);
    }

    @Test
    void testGetMessagesFor_EmptyProperty_shouldFindMessagesWithEmptyProperty() {
        var o = new Object();
        var m = Message.builder("1", Severity.ERROR).code("1").invalidObject(new ObjectProperty(o, "")).create();
        var messages = new MessageList(m);

        assertThat(messages.getMessagesFor(o)).hasSize(1);
        assertThat(messages.getMessagesFor(o).getMessage(0)).isEqualTo(m);
        assertThat(messages.getMessagesFor(o, null)).hasSize(1);
        assertThat(messages.getMessagesFor(o, null).getMessage(0)).isEqualTo(m);
        assertThat(messages.getMessagesFor(o, "")).hasSize(1);
        assertThat(messages.getMessagesFor(o, "").getMessage(0)).isEqualTo(m);
    }

    @Test
    void testGetSeverity() {
        var messageList = new MessageList(
                Message.newInfo(ANY, ANY),
                Message.newError(ANY, ANY),
                Message.newWarning(ANY, ANY));
        assertThat(messageList.getSeverity()).hasValue(Severity.ERROR);
    }

    @Test
    void testGetSeverity_OneSeverity() {
        var messageList = new MessageList(
                Message.newInfo(ANY, ANY),
                Message.newInfo(ANY, ANY));
        assertThat(messageList.getSeverity()).hasValue(Severity.INFO);
    }

    @Test
    void testGetSeverity_EmptyList() {
        assertThat(new MessageList().getSeverity()).isEmpty();
    }
}
