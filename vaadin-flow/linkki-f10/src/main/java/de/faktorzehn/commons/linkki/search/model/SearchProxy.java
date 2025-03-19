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

package de.faktorzehn.commons.linkki.search.model;

import java.util.function.Function;
import java.util.function.Supplier;

import org.linkki.core.binding.validation.message.MessageList;

/**
 * @deprecated Use {@link SimpleSearchController} instead.
 */
@Deprecated(since = "22.3.0")
public class SearchProxy<PARAM, RESULT> extends SimpleSearchController<PARAM, RESULT> {

    public SearchProxy(Supplier<PARAM> parameterCreator, Function<PARAM, RESULT> searchFunction,
            Function<RESULT, MessageList> messages) {
        super(parameterCreator, searchFunction, messages);
    }

}
