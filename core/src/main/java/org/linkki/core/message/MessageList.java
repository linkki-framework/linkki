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

import javax.annotation.Nullable;

import org.apache.commons.lang3.SystemUtils;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;

/**
 * A list of {@link Message Messages}.
 */
public class MessageList implements Serializable, Iterable<Message> {

    private static final long serialVersionUID = 1557794794967025627L;

    private final List<Message> messages;


    /**
     * Creates a message list that contains the given {@link Message message(s)}.
     *
     * @param messages {@link Message messages} which should be added initially to the created
     *            {@link MessageList}
     * 
     * @throws NullPointerException if {@code messages} or any {@link Message message} is
     *             {@code null}
     */
    public MessageList(Message... messages) {
        Objects.requireNonNull(messages, "messages must not be null");

        this.messages = new ArrayList<>(messages.length);
        for (Message msg : messages) {
            add(msg);
        }
    }

    /**
     * Adds the given {@link Message} to this {@link MessageList list}.
     *
     * @param message the {@link Message} to add
     *
     * @throws NullPointerException if {@code message} is {@code null}.
     */
    public void add(Message message) {
        Objects.requireNonNull(message, "message must not be null");
        messages.add(message);
    }

    /**
     * Adds the {@link Message messages} in the given {@link MessageList list} to this
     * {@link MessageList list}. If the given {@link MessageList list} is {@code null} nothing will
     * be added to this {@link MessageList list}.
     *
     * @param messageList {@link MessageList} with the {@link Message messages} to add to this
     *            {@link MessageList list}
     */
    public void add(@Nullable MessageList messageList) {
        if (messageList == null) {
            return;
        }

        messageList.forEach(this::add);
    }

    /**
     * @return {@code true} if the list is empty otherwise {@code false}.
     */
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * @return the size of the message list (the number of {@link Message messages} in this list)
     */
    public int size() {
        return messages.size();
    }

    /**
     * @param index index of the {@link Message} to return
     * 
     * @return the {@link Message} at the specified index
     */
    public Message getMessage(int index) {
        return messages.get(index);
    }

    /**
     * Searches the <strong>first</strong> {@link Message} with the given {@link ErrorLevel}. If no
     * such {@link Message} exists, an empty {@link Optional} will be returned.
     *
     * @param errorLevel {@link ErrorLevel} to search for
     * 
     * @return the <strong>first</strong> {@link Message} with the given {@link ErrorLevel} or
     *         {@link Optional#empty()} if no {@link Message} with the given {@link ErrorLevel}
     *         exists.
     */
    public Optional<Message> getFirstMessage(ErrorLevel errorLevel) {
        return messages.stream()
                .filter(m -> m.getErrorLevel() == errorLevel)
                .findFirst();
    }

    /**
     * Searches the <strong>first</strong> {@link Message} with given {@code code}. If this list
     * does not contain a {@link Message} with the given {@code code}, an empty {@link Optional}
     * will be returned.
     * <p>
     * If the {@code code} is {@code null}, the first {@link Message} with a {@code null code} will
     * be returned.
     *
     * @param code the code to search for
     * 
     * @return the <strong>first</strong> {@link Message} with the given {@code code} or
     *         {@link Optional#empty()}
     */
    public Optional<Message> getMessageByCode(@Nullable String code) {
        return messages.stream()
                .filter(m -> Objects.equals(code, m.getCode()))
                .findFirst();
    }

    /**
     * Returns a <strong>new</strong> {@link MessageList} containing all {@link Message messages}
     * with the specified message code.
     * <p>
     * If this {@link MessageList list} does not contain any {@link Message} with the given code.
     *
     * @param code the code to search for
     * 
     * @return <strong>new</strong> {@link MessageList} with all {@link Message messages} with the
     *         given {@code code}
     */
    public MessageList getMessagesByCode(@Nullable String code) {
        return messages.stream()
                .filter(m -> Objects.equals(code, m.getCode()))
                .collect(toMessageList());
    }

    /**
     * Returns the maximum {@link ErrorLevel} of this {@link MessageList}. If this
     * {@link MessageList list} {@link #isEmpty()}, {@link Optional#empty()} will be returned.
     * <p>
     * For Example:<br>
     * A {@link MessageList} contains 2 {@link Message messages}.<br>
     * One with {@link ErrorLevel#INFORMATION} and one with {@link ErrorLevel#ERROR}.<br>
     * {@code getErrorLevel()} returns {@link ErrorLevel#ERROR}.
     *
     * @return the {@link MessageList message list's} maximum {@link ErrorLevel} or
     *         {@link Optional#empty()}.
     */
    public Optional<ErrorLevel> getErrorLevel() {
        return messages.stream()
                .map(Message::getErrorLevel)
                .max(Comparator.comparing(ErrorLevel::intValue));
    }

    /**
     * Collects all {@link Message#getText() texts} from the containing {@link Message messages}.
     * <p>
     * If this {@link MessageList} does not contain any message, an empty String ("") will be
     * returned.
     *
     * @return the text of all {@link Message messages} in this list, separated by the system's
     *         default line ending or an emtpy String
     */
    public String getText() {
        return messages.stream()
                .map(Message::getText)
                .collect(Collectors.joining(SystemUtils.LINE_SEPARATOR));
    }

    /**
     * @return {@code true} if one of the {@link Message messages} in this list is has the
     *         {@link ErrorLevel#ERROR}, otherwise {code false}.
     */
    public boolean containsErrorMsg() {
        return messages.stream()
                .anyMatch(m -> m.getErrorLevel() == ErrorLevel.ERROR);
    }

    /**
     * Returns a <strong>new</strong> {@link MessageList} containing all {@link Message messages}
     * for the given {@code object} no matter of the property the {@link Message} is for
     * ({@link Message#getInvalidObjectProperties()}).
     * <p>
     * If this {@link MessageList list} does not contain any {@link Message} for the given
     * {@code object}, an empty {@link MessageList} will be returned.
     *
     * @param object object to search for
     * 
     * @return <strong>new</strong> {@link MessageList} containing all {@link Message messages} for
     *         the given {@code object}
     * 
     * @throws NullPointerException if {@code object} is {@code null}
     */
    public MessageList getMessagesFor(Object object) {
        return getMessagesFor(object, null);
    }

    /**
     * Returns a <strong>new</strong> {@link MessageList} containing all {@link Message messages}
     * for the given {@code object} and {@code property}
     * ({@link Message#getInvalidObjectProperties()}).
     * <p>
     * If this {@link MessageList list} does not contain any {@link Message} for the given
     * parameters, an empty {@link MessageList} will be returned.
     *
     * @param object object to search for
     * @param property property of the object to search for
     * 
     * @return <strong>new</strong> {@link MessageList} containing all {@link Message messages} for
     *         the given {@code object} and {@code property}
     * 
     * @throws NullPointerException if {@code object} is {@code null}
     */
    public MessageList getMessagesFor(Object object, @Nullable String property) {
        return getMessagesFor(object, property, -1);
    }

    /**
     * Returns a <strong>new</strong> {@link MessageList} containing all {@link Message messages}
     * for the given {@code object}, {@code property} and {@code index}
     * ({@link Message#getInvalidObjectProperties()}).
     * <p>
     * The {@code index} is relevant, if the {@code property} is a collection and the {@link Message
     * messages} for an element at a specific position are needed.
     * <p>
     * If this {@link MessageList list} does not contain any {@link Message} for the given
     * parameters, an empty {@link MessageList} will be returned.
     *
     * @param object object to search for
     * @param property property of the object to search for
     * @param index index of the object
     *
     * @return <strong>new</strong> {@link MessageList} containing all {@link Message messages} for
     *         the given parameters
     *
     * @throws NullPointerException if {@code object} is {@code null}
     */
    public MessageList getMessagesFor(Object object, @Nullable String property, int index) {
        Objects.requireNonNull(object, "object must not be null");

        return messages.stream()
                .filter(m -> m.getInvalidObjectProperties().stream()
                        .filter(op -> op.getObject().equals(object))
                        .anyMatch(op -> property == null
                                || (property.equals(op.getProperty())
                                        && (index < 0 || op.getIndex() == index))))
                .collect(toMessageList());

    }

    /**
     * Returns a <strong>new</strong> {@link MessageList} containing all {@link Message messages}
     * containing the given {@link ValidationMarker}. If {@code marker} is {@code null} all
     * {@link Message messages} without any markers are returned.
     * <p>
     * If no {@link Message} with the given marker exists in this {@link MessageList}, an empty
     * {@link MessageList} will be returned.
     *
     * @param marker the {@link ValidationMarker} to look for
     *
     * @return a <strong>new</strong> {@link MessageList} containing all {link Message messages}
     *         with the given {@link ValidationMarker} or an empty {@link MessageList}
     */
    public MessageList getMessagesByMarker(@Nullable ValidationMarker marker) {
        return messages.stream()
                .filter(m -> (marker == null && !m.hasMarkers()) || marker != null && m.hasMarker(marker))
                .collect(toMessageList());
    }

    /**
     * Returns a <strong>new</strong> {@link MessageList} containing all {@link Message messages}
     * with a {@link ValidationMarker} the specified {@link Predicate} matches.
     * <p>
     * Returns an empty {@link MessageList list} no such {@link Message} exists.
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
    @SuppressWarnings("null")
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
     * Returns a new {@link MessageList} containing the same {@link Message Messages} as this list,
     * sorted by descending {@link ErrorLevel}. Within each error level the previous order is
     * preserved.
     */
    public MessageList sortByErrorLevel() {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getErrorLevel).reversed())
                .collect(collector());
    }

    /**
     * @return the <strong>first</strong> {@link Message} with the highest {@link ErrorLevel} or
     *         {@link Optional#empty()} if this list is {@link #isEmpty()}
     */
    public Optional<Message> getMessageWithHighestErrorLevel() {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getErrorLevel).reversed())
                .findFirst();
    }

    /**
     * @return iterator for the {@link Message messages} in this list
     */
    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }

    /**
     * Removes all {@link Message messages} from this list.
     */
    public void clear() {
        messages.clear();
    }

    @Override
    @SuppressWarnings({ "null", "unused" })
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
