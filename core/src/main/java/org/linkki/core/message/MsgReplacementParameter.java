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
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * When creating a message the text might be created by replacing parameters (or placeholders) with
 * concrete values, e.g. "The sum insured must be at least {minSumInsured}." where {minSumInsured}
 * is replaced with the current minimum e.g. 200 Euro. If you need to represent the user a different
 * text, you need the actual value for the parameter. To achieve this the message holds the
 * parameters along with their actual value.
 * <p>
 * The following are scenarios where you might need to present a different text for a message:
 * <ul>
 * <li>You have limited space available for the text, for example if your display is a
 * terminal.</li>
 * <li>You present the text to a different user group, e.g. internet users instead of your
 * backoffice employees.</li>
 * </ul>
 * 
 * @author Jan Ortmann
 */
public class MsgReplacementParameter implements Serializable {

    private static final long serialVersionUID = -4588558762246019241L;

    private String name;
    @Nullable
    private Object value;

    /**
     * Creates a new parameter value with name and value.
     * 
     * @throws NullPointerException if paramName is null.
     */
    public MsgReplacementParameter(String paramName, Object paramValue) {
        name = Objects.requireNonNull(paramName, "paramName must not be null");
        value = paramValue;
    }

    /**
     * Returns the parameter's name. This method never returns <code>null</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the parameter's value.
     */
    @Nullable
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(@SuppressWarnings("null") Object o) {
        if (!(o instanceof MsgReplacementParameter)) {
            return false;
        }
        MsgReplacementParameter other = (MsgReplacementParameter)o;
        return name.equals(other.name)
                && ((value == null && other.value == null) || (value != null && value.equals(other.value)));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

}
