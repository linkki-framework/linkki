/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.SystemUtils;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.server.ErrorMessage.ErrorLevel;

/**
 * A human readable text message with an optional code that identifies the type of the message and a
 * errorLevel that indicates if this is an error, warning or information.
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
     * The object and their properties that are addressed in the message as having an error or that
     * a warning or information relates to.
     */
    private final List<ObjectProperty> invalidOp;

    private final List<MsgReplacementParameter> replacementParameters;

    /**
     * A set of {@link ValidationMarker} containing additional information.
     */
    private final Set<ValidationMarker> markers;


    /**
     * Creates a new message by defining the following parameters.
     * 
     * @param code A message code that identifies the kind of the message
     * @param text The human readable text of this message
     * @param errorLevel the message's {@link ErrorLevel}
     */
    public Message(String code, String text, ErrorLevel errorLevel) {
        this(code, text, errorLevel, null, null, null);
    }

    /**
     * Creates a new message by defining the following parameters.
     * 
     * @param code A message code that identifies the kind of the message
     * @param text The human readable text of this message
     * @param errorLevel the message's {@link ErrorLevel}
     * @param invalidObjectProperties A list of object properties the message refers to
     * @param parameters a list of replacement parameters
     * @param markers a list of markers. If this parameter is null an empty list is set as markers.
     *            The List of markers is
     */
    private Message(@Nullable String code, String text, ErrorLevel errorLevel,
            @Nullable List<ObjectProperty> invalidObjectProperties,
            @Nullable List<MsgReplacementParameter> parameters, @Nullable Set<ValidationMarker> markers) {
        this.code = code;
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.errorLevel = Objects.requireNonNull(errorLevel, "errorLevel must not be null");
        if (markers != null) {
            this.markers = Collections.unmodifiableSet(new HashSet<>(markers));
        } else {
            this.markers = Collections.emptySet();
        }
        if (invalidObjectProperties != null) {
            invalidOp = Collections.unmodifiableList(new ArrayList<>(invalidObjectProperties));
        } else {
            invalidOp = Collections.emptyList();
        }
        if (parameters != null) {
            replacementParameters = Collections.unmodifiableList(new ArrayList<>(parameters));
        } else {
            replacementParameters = Collections.emptyList();
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

    /**
     * Returns the message's errorLevel as one of the constants ERROR, WARNING, INFO or NONE.
     */
    public ErrorLevel getErrorLevel() {
        return errorLevel;
    }

    /**
     * Returns the humand readable message text.
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
     * Returns the list of object properties the message refers to. E.g. if a message reads "The
     * driver's age must be greater than 18.", this method would probably return the driver object
     * and the property name age. Returns an empty array if this message does not refer to any
     * objects / properties.
     */
    public List<ObjectProperty> getInvalidObjectProperties() {
        if (invalidOp.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(invalidOp);
    }

    /**
     * Returns a set of {@link ValidationMarker}s associated with this class. Returns an empty set
     * if no markers are set.
     */
    public Set<ValidationMarker> getMarkers() {
        return markers;
    }

    /**
     * Returns <code>true</code> if the message contains the specified {@link ValidationMarker}
     * marker otherwise <code>false</code>.
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
        int max = invalidOp.size();
        for (int i = 0; i < max; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(invalidOp.get(i).getObject().toString());
            buffer.append('.');
            buffer.append(invalidOp.get(i).getProperty());
        }
        buffer.append(']');
        buffer.append(SystemUtils.LINE_SEPARATOR);
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
        if (!Objects.equals(invalidOp, other.invalidOp)) {
            return false;
        }
        if (!Objects.equals(replacementParameters, other.replacementParameters)) {
            return false;
        }
        if (!Objects.equals(markers, other.markers)) {
            return false;
        }
        return true;
    }

    /**
     * Returns {@code true} if the given message has at least one marker of type
     * {@code ValidationMarker} marking it as a mandatory field validation message, i.e.
     * {@link ValidationMarker#isRequiredInformationMissing()} is {@code true}. Returns
     * {@code false} if the message has no {@code ValidationMarker} or is not marked as a mandatory
     * field validation message.
     */
    public boolean isMandatoryFieldMessage() {
        return markers.stream()
                .anyMatch(ValidationMarker::isRequiredInformationMissing);
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

        private final List<MsgReplacementParameter> replacementParams;

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

            replacementParams = new ArrayList<>();
            invalidObjectProperties = new ArrayList<>();
            markers = new HashSet<>();
        }

        /**
         * Set the message's code that identifies the kind of the message.
         * 
         * @param code A message code that identifies the kind of the message
         * @return this builder instance to directly add further properties
         */
        public Builder code(String code) {
            this.code = code;
            return this;
        }

        /**
         * Set an object property that message refers to.
         *
         * @param invalidObjectProperty An object property that message refers to
         * @return this builder instance to directly add further properties
         *
         */
        public Builder invalidObject(ObjectProperty invalidObjectProperty) {
            return invalidObjects(Objects.requireNonNull(invalidObjectProperty,
                                                         "invalidObjectProperty must not be null"));
        }

        /**
         * Add a list of object properties that message refers to.
         * 
         * @param invalidObjectProperties A list of object properties that message refers to
         * @return this builder instance to directly add further properties
         */
        public Builder invalidObjects(List<ObjectProperty> invalidObjectProperties) {
            this.invalidObjectProperties.addAll(invalidObjectProperties);
            return this;
        }

        /**
         * Set object properties that message refers to.
         * 
         * @param invalidObjectProperties Object properties that message refers to
         * @return this builder instance to directly add further properties
         */
        public Builder invalidObjects(ObjectProperty... invalidObjectProperties) {
            return invalidObjects(Arrays.asList(invalidObjectProperties));
        }

        /**
         * Add some object properties the message refers to by creating instances of
         * {@link ObjectProperty} for every given property and the given object.
         * 
         * @param object The object the message refers to
         * @param properties Some properties the message refers to
         * @return this builder instance to directly add further properties
         * 
         */
        @SuppressWarnings("null")
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
         * A list of replacement parameters the message should reference.
         * 
         * @param replacementParams a list of replacement parameters
         * @return this builder instance to directly add further properties
         */
        public Builder replacements(List<MsgReplacementParameter> replacementParams) {
            this.replacementParams.addAll(replacementParams);
            return this;
        }

        /**
         * Some replacement parameters the message should reference.
         * 
         * @param replacementParams Some replacement parameters
         * @return this builder instance to directly add further properties
         */
        public Builder replacements(MsgReplacementParameter... replacementParams) {
            return replacements(Arrays.asList(replacementParams));
        }

        /**
         * Creates a new {@link MsgReplacementParameter} the message should reference
         * 
         * @param name The name of the {@link MsgReplacementParameter}
         * @param value The value of the {@link MsgReplacementParameter}
         * @return this builder instance to directly add further properties
         */
        public Builder replacement(String name, Object value) {
            replacementParams.add(new MsgReplacementParameter(name, value));
            return this;
        }

        /**
         * Set a collection of markers that should be provided to the new message.
         * 
         * @param markers a set of markers
         * @return this builder instance to directly add further properties
         */
        public Builder markers(Collection<? extends ValidationMarker> markers) {
            this.markers.addAll(markers);
            return this;
        }

        /**
         * Set some markers that should be provided to the new message.
         * 
         * @param markers Some markers
         * @return this builder instance to directly add further properties
         */
        public Builder markers(ValidationMarker... markers) {
            return markers(Arrays.asList(markers));
        }

        /**
         * Creates a new {@link Message} with all previously given properties.
         * 
         * @return a new message that has the parameters of this builder.
         */
        public Message create() {
            return new Message(code, text, errorLevel, invalidObjectProperties, replacementParams, markers);
        }

    }

}
