/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.aspects;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.linkki.core.defaults.ui.element.ItemCaptionProvider;

/**
 * A cache of items and their captions. Used to detect changes in items or their captions in
 * {@link AvailableValuesAspectDefinition}.
 */
public class ItemCache {

    private final ItemCaptionProvider<?> captionProvider;
    private final List<Object> items = new ArrayList<>();
    private final List<String> captions = new ArrayList<>();

    public ItemCache(ItemCaptionProvider<?> captionProvider) {
        this.captionProvider = requireNonNull(captionProvider, "captionProvider must not be null");
    }

    /**
     * Replaces the cache with the given content. The return value indicates whether the items or their
     * captions changed.
     */
    public boolean replaceContent(List<Object> newItems) {
        List<String> newCaptions = new ArrayList<>(newItems.size());
        newItems.forEach(i -> newCaptions.add(captionProvider.getUnsafeCaption(i)));

        if (!newItems.equals(items) || !newCaptions.equals(captions)) {
            items.clear();
            items.addAll(newItems);
            captions.clear();
            captions.addAll(newCaptions);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a read-only view of the items in the cache. Changes via {@link #replaceContent(List)} are
     * reflected in the returned list.
     */
    public List<Object> getItems() {
        return Collections.unmodifiableList(items);
    }


}
