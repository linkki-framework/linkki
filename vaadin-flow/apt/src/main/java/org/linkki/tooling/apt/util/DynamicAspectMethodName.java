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

package org.linkki.tooling.apt.util;

import static java.util.Objects.requireNonNull;
import static org.linkki.util.BeanUtils.GET_PREFIX;
import static org.linkki.util.BeanUtils.IS_PREFIX;

import javax.lang.model.element.ExecutableElement;

import org.apache.commons.lang3.StringUtils;

/**
 * The name of the method that is called to determine the dynamic value of an aspect.
 */
public class DynamicAspectMethodName {

    private final String expectedMethodName;

    public DynamicAspectMethodName(ExecutableElement method, String suffix, boolean isBoolean) {
        requireNonNull(method, "method must not be null");
        requireNonNull(suffix, "suffix must not be null");
        String prefix = isBoolean ? IS_PREFIX : GET_PREFIX;
        this.expectedMethodName = prefix + StringUtils.capitalize(MethodNameUtils.getPropertyName(method))
                + StringUtils.capitalize(suffix);
    }

    public String getExpectedMethodName() {
        return expectedMethodName;
    }

    @Override
    public String toString() {
        return getExpectedMethodName();
    }

}
