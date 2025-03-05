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

package org.linkki.search.model;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.linkki.core.binding.validation.message.MessageList;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A {@link SearchController} that only caches the search result and performs no additional
 * operations.
 *
 * @since 2.8.0
 */
public class SimpleSearchController<PARAM, RESULT> implements SearchController<PARAM, RESULT> {

    private final Supplier<PARAM> parameterCreator;
    private final Function<PARAM, RESULT> searchFunction;
    private final Function<RESULT, MessageList> messages;
    private PARAM parameters;
    @CheckForNull
    private RESULT result;

    /**
     * Creates the {@link SimpleSearchController} with the necessary
     * 
     * @param parameterCreator Is triggered to get a new parameter object, either for a new search
     *            or when {@link #reset()} is called
     * @param searchFunction A function that calls the search service. It is called whenever the a
     *            search is triggered
     * @param messages Optional messages that are returned by the search service. If there is no
     *            search result for the given parameters, there must be at least on message to
     *            explain that fact.
     */
    public SimpleSearchController(Supplier<PARAM> parameterCreator,
            Function<PARAM, RESULT> searchFunction,
            Function<RESULT, MessageList> messages) {
        this.parameterCreator = parameterCreator;
        this.searchFunction = searchFunction;
        this.messages = messages;
        this.parameters = parameterCreator.get();
    }

    @Override
    public Optional<RESULT> get() {
        return Optional.ofNullable(result);
    }

    @Override
    public void search() {
        result = searchFunction.apply(parameters);
    }

    @Override
    public MessageList getMessages() {
        return get().map(messages).orElse(new MessageList());
    }

    @Override
    public void reset() {
        result = null;
        parameters = parameterCreator.get();
    }

    @Override
    public PARAM getParameters() {
        return parameters;
    }

}
