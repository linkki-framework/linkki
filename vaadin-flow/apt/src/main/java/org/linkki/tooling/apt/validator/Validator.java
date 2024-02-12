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

package org.linkki.tooling.apt.validator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import javax.annotation.processing.Messager;

import org.linkki.tooling.apt.model.AptPmo;

/**
 * A single check or class of checks.
 */
public interface Validator {

    /**
     * Checks the given PMO on prints results to the {@link Messager}.
     */
    void validate(AptPmo pmo, Messager messager);

    static List<String> getMessageCodes(Class<? extends Validator> validator) {
        Optional<MessageCodes> codes = Optional.ofNullable(validator.getAnnotation(MessageCodes.class));
        return codes
                .map(it -> asList(it.value()))
                .orElse(emptyList());
    }
}
