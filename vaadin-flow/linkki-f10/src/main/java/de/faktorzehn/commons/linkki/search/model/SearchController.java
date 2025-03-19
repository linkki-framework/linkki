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

import java.util.Optional;
import java.util.function.Supplier;

import org.linkki.core.binding.validation.message.MessageList;

import de.faktorzehn.commons.linkki.search.SearchLayoutBuilder;

/**
 * A search controller is intended to be used in conjunction with {@link SearchLayoutBuilder}.
 * <p>
 * Its main functions are:
 * <ul>
 * <li>Caching the result and messages of a search operation
 * <li>Performing additional operations (optional, varies depending on the implementation) when
 * interacting with the search
 * </ul>
 * 
 * @param <PARAM> Type of search parameters
 * @param <RESULT> Type of result object of the search service, normally wraps the result and
 *            optional messages
 *
 * @deprecated moved to linkki-search-vaadin-flow. Use org.linkki.search.model.SearchController instead
 */
@Deprecated(since = "2.8.0")
public interface SearchController<PARAM, RESULT> extends Supplier<Optional<RESULT>> {

    /**
     * Triggers the search.
     * <p>
     * This function is called when the search button created by a {@link SearchLayoutBuilder} is
     * pressed.
     */
    public void search();

    /**
     * Resets the current result, messages and search parameters.
     * <p>
     * This function is called when the reset button created by a {@link SearchLayoutBuilder} is
     * pressed.
     */
    public void reset();

    /**
     * Returns the result object provided by the last {@link #search()} call.
     * <p>
     * The result is empty if there was no search triggered yet after creation or after calling
     * {@link #reset()}.
     */
    @Override
    public Optional<RESULT> get();

    /**
     * Returns the message list provided by the last {@link #search()} call.
     */
    public MessageList getMessages();

    /**
     * Returns the currently active parameters.
     * <p>
     * The parameters object may change during the life cycle of this object, so the returned object
     * should not be cached. Instead, this function should be called every time when accessing the
     * parameters (i.e. as a supplier).
     */
    public PARAM getParameters();

}
