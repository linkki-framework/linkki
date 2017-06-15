/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.message;

import static org.linkki.core.message.MessageListCollector.toMessageList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.SystemUtils;
import org.linkki.util.validation.ValidationMarker;

/**
 * A list of {@link Message Messages}.
 */
public class MessageList implements Serializable, Iterable<Message> {

    private static final long serialVersionUID = 5518835977871253111L;

    private List<Message> messages = new ArrayList<>(0);

    /**
     * Creates an empty message list.
     */
    public MessageList() {
        // Provides default constructor.
    }

    /**
     * Creates a message list that contains the given message.
     *
     * @throws NullPointerException if any msg is null.
     */
    public MessageList(Message... msgs) {
        for (Message msg : msgs) {
            add(msg);
        }
    }

    /**
     * Adds the message to the list.
     *
     * @throws NullPointerException if msg is null.
     */
    public void add(Message msg) {
        Objects.requireNonNull(msg, "msg must not be null");
        messages.add(msg);
    }

    /**
     * Adds the messages in the given list to this list.
     */
    public void add(@Nullable MessageList msgList) {
        if (msgList == null) {
            return;
        }

        msgList.forEach(this::add);
    }

    /**
     * Returns true if the list is empty.
     */
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * @return The size of the message list
     */
    public int size() {
        return messages.size();
    }

    /**
     * @return the message at the indicated index
     */
    public Message getMessage(int index) {
        return messages.get(index);
    }

    /**
     * @return the first message with the given severity or null if none is found.
     */
    public Optional<Message> getFirstMessage(Severity severity) {
        return messages.stream()
                .filter(m -> m.getSeverity() == severity)
                .findFirst();
    }

    /**
     * Returns the first message in the list that has the specified message code. Returns null, if
     * the list does not contain such a message.
     */
    public Optional<Message> getMessageByCode(@Nullable String code) {
        return messages.stream()
                .filter(m -> Objects.equals(code, m.getCode()))
                .findFirst();
    }

    /**
     * Returns a new message list containing all the message in this list that have the specified
     * message code. Returns an empty list if either code is <code>null</code> or this list does
     * contain any message with the given code.
     */
    public MessageList getMessagesByCode(@Nullable String code) {
        return messages.stream()
                .filter(m -> Objects.equals(code, m.getCode()))
                .collect(toMessageList());
    }

    /**
     * Returns the message list's severity. This is the maximum severity of the list's messages. If
     * the list does not contain any messages, the method returns 0.
     */
    public Severity getSeverity() {
        return messages.stream().map(Message::getSeverity).max(Comparator.naturalOrder()).orElse(Severity.NONE);
    }

    /**
     * @return the text of all messages in the list, separated by the system's default line ending
     */
    public String getText() {
        return messages.stream()
                .map(Message::getText)
                .collect(Collectors.joining(SystemUtils.LINE_SEPARATOR));
    }

    /**
     * Returns true if one the messages in the list is an error message, otherwise false.
     */
    public boolean containsErrorMsg() {
        return messages.stream()
                .anyMatch(m -> m.getSeverity() == Severity.ERROR);
    }

    /**
     * Returns a new list with the messages in this list that belong to the given object (any
     * property). Returns an empty list if no such message is found.
     */
    public MessageList getMessagesFor(Object object) {
        return getMessagesFor(object, null);
    }

    /**
     * Returns a new list with the messages in this list that belong to the given object and
     * property and the property is of the given index. Returns an empty list if no such message is
     * found.
     */
    public MessageList getMessagesFor(Object object, @Nullable String property, int index) {
        return messages.stream()
                .filter(m -> m.getInvalidObjectProperties().stream()
                        .filter(op -> op.getObject().equals(object))
                        .anyMatch(op -> property == null
                                || (property.equals(op.getProperty())
                                        && (index < 0 || op.getIndex() == index))))
                .collect(toMessageList());

    }

    /**
     * Returns a new list with the messages in this list that belong to the given object and
     * property. Returns an empty list if no such message is found.
     */
    public MessageList getMessagesFor(Object object, @Nullable String property) {
        return getMessagesFor(object, property, -1);
    }


    /**
     * Returns a new message list containing all the message in this list that have the specified
     * {@link ValidationMarker}. If {@code marker} is {@code null} all {@link Message messges}
     * without any markers are returned.
     *
     * @param marker the {@link ValidationMarker} to look for
     * @return a new {@link MessageList} containing all {link Message messages} with the given
     *         {@link ValidationMarker}
     */
    public MessageList getMessagesByMarker(@Nullable ValidationMarker marker) {
        return messages.stream()
                .filter(m -> (marker == null && !m.hasMarkers()) || marker != null && m.hasMarker(marker))
                .collect(toMessageList());
    }

    /**
     * Returns a new message list containing all the message in this list with a
     * {@link ValidationMarker} the specified {@link Predicate} matches. Returns an empty list if
     * this list does not contain any such message.
     * <p>
     * Sample usage:
     * <code>messages.getMessagesByMarker(ValidationMarker::isMandatoryFieldValidation</code>
     *
     * @param markerPredicate to match a {@link ValidationMarker} with.
     *
     * @return new {@link MessageList} with all {@link Message messages} with a matching
     *         {@link ValidationMarker} or an empty {@link MessageList}
     *
     * @throws NullPointerException if markerPredicate is <code>null</code>
     */
    public MessageList getMessagesByMarker(Predicate<ValidationMarker> markerPredicate) {
        Objects.requireNonNull(markerPredicate, "markerPredicate must not be null");

        return messages.stream()
                .filter(m -> containsMatchingMarker(m, markerPredicate))
                .collect(toMessageList());
    }

    private boolean containsMatchingMarker(Message message, Predicate<ValidationMarker> markerPredicate) {
        return message.getMarkers().stream()
                .anyMatch(markerPredicate);
    }

    /**
     * Returns the message with the highest severity. If there are multiple such messages, the first
     * one is returned. If this list {@link #isEmpty()}, <code>null</code> is returned.
     */
    @CheckForNull
    public Message getMessageWithHighestSeverity() {
        return messages.stream().sorted(Comparator.comparing(Message::getSeverity).reversed()).findFirst().orElse(null);
    }

    /**
     * Returns an iterator over the messages in this list.
     */
    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    /**
     * Removes all of the messages from this list. This list will be empty after this call returns.
     */
    public void clear() {
        messages.clear();
    }

    @SuppressWarnings({ "null", "unused" })
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MessageList other = (MessageList)obj;
        return Objects.equals(messages, other.messages);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + messages.hashCode();
        return result;
    }

    /**
     * Returns all messages in the list separated by a line separator.
     */
    @Override
    public String toString() {
        return messages.stream()
                .map(Message::toString)
                .collect(Collectors.joining(SystemUtils.LINE_SEPARATOR));
    }

    /**
     * @return this list's {@link Message messages} as a stream.
     */
    public Stream<Message> stream() {
        return messages.stream();
    }

    /**
     * Shortcut to {@link MessageListCollector#toMessageList()}
     */
    public static Collector<Message, ?, MessageList> collector() {
        return MessageListCollector.toMessageList();
    }

}
