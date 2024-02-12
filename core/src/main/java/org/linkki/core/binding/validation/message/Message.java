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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.linkki.util.validation.ValidationMarker;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A human readable text message with a {@link Severity} and an optional code that identifies the
 * origin of the message.
 * <p>
 * In addition, a message provides access to the objects and their properties the message relates
 * to. For example, if a message says that "insured person's age must be at least 18" than the
 * invalid object that the message relates to is the insured person, and the invalid property is its
 * age. This information can be used to define which controls should display the message.
 * <p>
 * You can store additional information about the message by providing {@link ValidationMarker
 * validation markers} to the message. The exact use of the marker then depends on the
 * implementation of the {@link ValidationMarker}.
 * <p>
 * Message is an immutable value object. Two message objects are considered equal if they have the
 * same severity, code, text, invalid properties and {@link ValidationMarker markers}.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -6818159480375138792L;

    private final Severity severity;
    private final String text;
    @CheckForNull
    private final String code;

    /**
     * The objects and their properties that the message relates to.
     */
    private final List<ObjectProperty> invalidProperties;

    /**
     * A set of {@link ValidationMarker} containing additional information about the message.
     */
    private final Set<ValidationMarker> markers;

    /**
     * Creates a new message with an optional code, a text content and a {@link Severity}.
     * 
     * @param code a code that identifies the message
     * @param text the human readable text of this message
     * @param severity the message's {@link Severity}
     */
    public Message(@CheckForNull String code, String text, Severity severity) {
        this(code, text, severity, null, null);
    }

    Message(@CheckForNull String code, String text, Severity severity,
            @CheckForNull List<ObjectProperty> invalidObjectProperties, @CheckForNull Set<ValidationMarker> markers) {
        this.code = code;
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.severity = Objects.requireNonNull(severity, "severity must not be null");
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
     * Constructs a new {@link Message} with {@link Severity#INFO}.
     */
    public static Message newInfo(String code, String text) {
        return new Message(code, text, Severity.INFO);
    }

    /**
     * Constructs a new {@link Message} with {@link Severity#WARNING}.
     */
    public static Message newWarning(String code, String text) {
        return new Message(code, text, Severity.WARNING);
    }

    /**
     * Constructs a new {@link Message} with {@link Severity#ERROR}.
     */
    public static Message newError(String code, String text) {
        return new Message(code, text, Severity.ERROR);
    }

    public Severity getSeverity() {
        return severity;
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
    @CheckForNull
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
        return getMarkers()
                .stream()
                .anyMatch(ValidationMarker::isRequiredInformationMissing);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(severity.toString())
                .append(' ')
                .append(code)
                .append('[');

        int max = invalidProperties.size();
        for (int i = 0; i < max; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(invalidProperties.get(i).getObject().toString())
                    .append('.')
                    .append(invalidProperties.get(i).getProperty());
        }

        buffer.append(']')
                .append('\n')
                .append(text);

        return buffer.toString();
    }

    /**
     * Returns true if o is a Message and severity, code and text are equal.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message)) {
            return false;
        }
        Message other = (Message)o;
        if (!Objects.equals(code, other.code) || !Objects.equals(text, other.text) || (severity != other.severity)
                || !Objects.equals(invalidProperties, other.invalidProperties)) {
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
     * @param severity the {@link Message messages'} {@link Severity}
     *
     * @return a new {@link Builder}
     *
     * @throws NullPointerException if {@code text} or {@code severity} is {@code null}
     *
     * @see Builder
     */
    public static Builder builder(String text, Severity severity) {
        Objects.requireNonNull(text, "text must not be null");
        Objects.requireNonNull(severity, "severity must not be null");

        return new Builder(text, severity);
    }

    /**
     * A builder for the {@link Message} class. This builder has been designed due to heavy
     * constructor overloading with many parameters. It helps instantiating global variables of
     * {@link Message}.
     * <p>
     * To use the builder simply create an instance by calling the
     * {@link Message#builder(String, Severity)}. Afterwards add needed information to the builder
     * for example call {@link #invalidObjectWithProperties(Object object, String... properties)} to
     * provide some invalid object properties. When the builder has every information that is needed
     * to create a proper message call {@link #create()}.
     */
    @SuppressWarnings("hiding")
    public static class Builder {

        private final String text;

        private final Severity severity;

        private final List<ObjectProperty> invalidObjectProperties;

        private final Set<ValidationMarker> markers;

        @CheckForNull
        private String code;

        /**
         * Creates a new builder that is able to create a proper {@link Message} with all needed
         * information.
         * 
         * @param text The human readable text of this message
         * @param severity the message's {@link Severity}
         */
        private Builder(String text, Severity severity) {
            this.text = text;
            this.severity = severity;

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
        public Builder invalidObjects(List<ObjectProperty> invalidObjectProperties) {
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
        public Builder invalidObjectWithProperties(Object object, String... properties) {
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
            return new Message(code, text, severity, invalidObjectProperties, markers);
        }

    }

}
