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
package org.linkki.core.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;

/**
 * A human readable text message with an optional code that identifies the type of the message and a
 * {@link ErrorLevel} that indicates if this is an error, warning or information.
 * <p>
 * In addition a message provides access to the objects and their properties the message relates to.
 * E.g. if a message reads that "insured person's age must be at least 18" than the person's age is
 * invalid. This information can be used for example to mark controls in the UI that display this
 * property.
 * <p>
 * If the provided message has replacement parameters that cannot be evaluated while creating the
 * message text, it is possible to provide these parameters to the message object. Have a look at
 * {@link MsgReplacementParameter} for further information.
 * <p>
 * If you need any further information stored with the message, it is possible to implement the
 * {@link ValidationMarker} object and provide some additional markers to the message. The exact use
 * of the markers depends on the custom implementation.
 * <p>
 * Message is an immutable value object. Two message objects are considered equal if they have the
 * same errorLevel, code, text, "invalid properties", replacement parameters and
 * {@link ValidationMarker markers}.
 * 
 * @see MsgReplacementParameter
 */

public class Message implements Serializable {

    private static final long serialVersionUID = -6818159480375138792L;


    private final ErrorLevel errorLevel;
    private final String text;
    @Nullable
    private final String code;

    /**
     * The objects and their properties that are addressed in the message
     */
    private final List<@NonNull ObjectProperty> invalidProperties;

    /**
     * A set of {@link ValidationMarker} containing additional information.
     */
    private final Set<@NonNull ValidationMarker> markers;


    /**
     * Creates a new message by defining the following parameters.
     * 
     * @param code A message code that identifies the kind of the message
     * @param text The human readable text of this message
     * @param errorLevel the message's {@link ErrorLevel}
     */
    public Message(@Nullable String code, String text, ErrorLevel errorLevel) {
        this(code, text, errorLevel, null, null);
    }

    private Message(@Nullable String code, String text, ErrorLevel errorLevel,
            @Nullable List<ObjectProperty> invalidObjectProperties, @Nullable Set<ValidationMarker> markers) {
        this.code = code;
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.errorLevel = Objects.requireNonNull(errorLevel, "errorLevel must not be null");
        if (markers != null) {
            this.markers = Collections.unmodifiableSet(new HashSet<>(markers));
        } else {
            this.markers = Collections.emptySet();
        }
        if (invalidObjectProperties != null) {
            invalidProperties = Collections.unmodifiableList(new ArrayList<>(invalidObjectProperties));
        } else {
            invalidProperties = Collections.emptyList();
        }
    }

    /**
     * Constructs a new information message.
     */
    public static Message newInfo(String code, String text) {
        return new Message(code, text, ErrorLevel.INFORMATION);
    }

    /**
     * Constructs a new warning message.
     */
    public static Message newWarning(String code, String text) {
        return new Message(code, text, ErrorLevel.WARNING);
    }

    /**
     * Constructs a new error message.
     */
    public static Message newError(String code, String text) {
        return new Message(code, text, ErrorLevel.ERROR);
    }

    public ErrorLevel getErrorLevel() {
        return errorLevel;
    }

    /**
     * Returns the human readable message text.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the message code.
     */
    @Nullable
    public String getCode() {
        return code;
    }

    /**
     * @return <strong>unmodifiable</strong> view of the invalid {@link ObjectProperty object
     *         properties}
     */
    public List<ObjectProperty> getInvalidObjectProperties() {
        return invalidProperties;
    }

    /**
     * @return <strong>unmodifiable</strong> view of all {@link ValidationMarker validation markers}
     */
    public Set<ValidationMarker> getMarkers() {
        return markers;
    }

    /**
     * @return {@code true} if the message contains the specified {@link ValidationMarker marker}
     *         otherwise {@code false}
     */
    public boolean hasMarker(ValidationMarker marker) {
        return markers.contains(marker);
    }

    /**
     * Returns <code>true</code> if the message has markers otherwise <code>false</code>.
     */
    public boolean hasMarkers() {
        return !markers.isEmpty();
    }

    /**
     * @return {@code true} if the message has at least one marker of type {@code ValidationMarker}
     *         marking it as a mandatory field validation message
     */
    public boolean isMandatoryFieldMessage() {
        return getMarkers().stream()
                .anyMatch(ValidationMarker::isRequiredInformationMissing);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        switch (errorLevel) {
            case ERROR:
                buffer.append("ERROR");
                break;
            case WARNING:
                buffer.append("WARNING");
                break;
            case INFORMATION:
                buffer.append("INFO");
                break;
            default:
                buffer.append("errorLevel ").append(errorLevel);
        }
        buffer.append(' ');
        buffer.append(code);
        buffer.append('[');
        int max = invalidProperties.size();
        for (int i = 0; i < max; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(invalidProperties.get(i).getObject().toString());
            buffer.append('.');
            buffer.append(invalidProperties.get(i).getProperty());
        }
        buffer.append(']');
        buffer.append('\n');
        buffer.append(text);
        return buffer.toString();
    }

    /**
     * Returns true if o is a Message and errorLevel, code and text are equal.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(@SuppressWarnings("null") Object o) {
        if (!(o instanceof Message)) {
            return false;
        }
        Message other = (Message)o;
        if (!Objects.equals(code, other.code)) {
            return false;
        }
        if (!Objects.equals(text, other.text)) {
            return false;
        }
        if (errorLevel != other.errorLevel) {
            return false;
        }
        if (!Objects.equals(invalidProperties, other.invalidProperties)) {
            return false;
        }
        if (!Objects.equals(markers, other.markers)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    /**
     * Creates a {@link Builder} for a {@link Message} with the required parameters for a
     * {@link Message}.
     *
     * @param text the text of the {@link Message}
     * @param errorLevel the {@link Message messages'} {@link ErrorLevel}
     *
     * @return a new {@link Builder}
     *
     * @throws NullPointerException if {@code text} or {@code errorLevel} is {@code null}
     *
     * @see Builder
     */
    public static Builder builder(String text, ErrorLevel errorLevel) {
        Objects.requireNonNull(text, "text must not be null");
        Objects.requireNonNull(errorLevel, "errorLevel must not be null");

        return new Builder(text, errorLevel);
    }

    /**
     * A builder for the {@link Message} class. This builder has been designed due to heavy
     * constructor overloading with many parameters. It helps instantiating global variables of
     * {@link Message}.
     * <p>
     * To use the builder simply create an instance by calling the
     * {@link Message#builder(String, ErrorLevel)}. Afterwards add needed information to the builder
     * for example call {@link #invalidObjectWithProperties(Object object, String... properties)} to
     * provide some invalid object properties. When the builder has every information that is needed
     * to create a proper message call {@link #create()}.
     */
    @SuppressWarnings("hiding")
    public static class Builder {

        private final String text;

        private final ErrorLevel errorLevel;

        private final List<ObjectProperty> invalidObjectProperties;

        private final Set<ValidationMarker> markers;

        @Nullable
        private String code;


        /**
         * Creates a new builder that is able to create a proper {@link Message} with all needed
         * information.
         * 
         * @param text The human readable text of this message
         * @param errorLevel the message's {@link ErrorLevel}
         */
        private Builder(String text, ErrorLevel errorLevel) {
            this.text = text;
            this.errorLevel = errorLevel;

            invalidObjectProperties = new ArrayList<>();
            markers = new HashSet<>();
        }

        /**
         * Set the message's code that identifies the kind of the message.
         * 
         * @param code message code that identifies the kind of the message
         *
         * @return this builder instance to directly add further properties
         */
        public Builder code(String code) {
            this.code = code;
            return this;
        }

        /**
         * Set an object property the message refers to.
         *
         * @param invalidObjectProperty An object property the message refers to
         *
         * @return this builder instance to directly add further properties
         *
         */
        public Builder invalidObject(ObjectProperty invalidObjectProperty) {
            return invalidObjects(Objects.requireNonNull(invalidObjectProperty,
                                                         "invalidObjectProperty must not be null"));
        }

        /**
         * Add a list of object properties the message refers to.
         * 
         * @param invalidObjectProperties A list of object properties the message refers to
         *
         * @return this builder instance to directly add further properties
         */
        public Builder invalidObjects(List<@NonNull ObjectProperty> invalidObjectProperties) {
            this.invalidObjectProperties.addAll(invalidObjectProperties);
            return this;
        }

        /**
         * Set object properties the message refers to.
         * 
         * @param invalidObjectProperties Object properties the message refers to
         *
         * @return this builder instance to directly add further properties
         */
        public Builder invalidObjects(@NonNull ObjectProperty... invalidObjectProperties) {
            return invalidObjects(Arrays.asList(invalidObjectProperties));
        }

        /**
         * Add some object properties the message refers to by creating instances of
         * {@link ObjectProperty} for every given property and the given object.
         * 
         * @param object The object the message refers to
         * @param properties Some properties the message refers to
         *
         * @return this builder instance to directly add further properties
         */
        public Builder invalidObjectWithProperties(Object object, @NonNull String... properties) {
            if (properties.length == 0) {
                return invalidObject(new ObjectProperty(object));
            }

            for (String property : properties) {
                invalidObjectProperties.add(new ObjectProperty(object, property));
            }

            return this;
        }

        /**
         * Set a collection of markers that should be provided to the new message.
         * 
         * @param markers a set of markers
         *
         * @return this builder instance to directly add further properties
         */
        public Builder markers(Collection<? extends ValidationMarker> markers) {
            this.markers.addAll(markers);
            return this;
        }

        /**
         * Set some markers that should be provided to the new message.
         * 
         * @param markers {@link ValidationMarker markers} for the message
         *
         * @return this builder instance to directly add further properties
         */
        public Builder markers(@NonNull ValidationMarker... markers) {
            return markers(Arrays.asList(markers));
        }

        /**
         * Creates a new {@link Message} with all previously given properties.
         * 
         * @return a new message that has the parameters of this builder.
         */
        public Message create() {
            return new Message(code, text, errorLevel, invalidObjectProperties, markers);
        }

    }

}
