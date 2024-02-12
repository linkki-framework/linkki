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

package org.linkki.core.ui.aspects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A cache of items. Used to detect changes in items or their captions in
 * {@link AvailableValuesAspectDefinition}.
 */
public class ItemCache {

    private final List<Object> items = new ArrayList<>();

    /**
     * Replaces the cache with the given content. The return value indicates whether the items
     * changed.
     */
    public boolean replaceContent(List<Object> newItems) {
        if (!newItems.equals(items)) {
            items.clear();
            items.addAll(newItems);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a read-only view of the items in the cache. Changes via {@link #replaceContent(List)}
     * are reflected in the returned list.
     */
    public List<Object> getItems() {
        return Collections.unmodifiableList(items);
    }

}
