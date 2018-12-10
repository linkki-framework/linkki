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
package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

/**
 * An observer that can be attached to a {@link BindingManager} and is notified if an update UI
 * event is triggered by a managed {@link BindingContext}. Updates its own UI upon notification.
 * 
 * @see BindingManager#addUiUpdateObserver(UiUpdateObserver)
 */
@FunctionalInterface
public interface UiUpdateObserver {

    /**
     * Called by {@link BindingManager} when UI updates are triggered (e.g. by managed
     * {@link BindingContext}s).
     */
    void uiUpdated();

    /**
     * Returns a composed {@code UiUpdateObserver} that performs, in sequence, this observer's
     * {@link #uiUpdated()} method followed by the {@code after} observer's {@link #uiUpdated()} method.
     * If performing either update throws an exception, it is relayed to the caller of the composed
     * observer. If performing this update throws an exception, the {@code after} observer will not be
     * notified.
     *
     * @param after the observer to notify after this observer
     * @return a composed {@code UiUpdateObserver} that notifies in sequence this observer followed by
     *         the {@code after} observer
     * @throws NullPointerException if {@code after} is null
     */
    default UiUpdateObserver andThen(UiUpdateObserver after) {
        requireNonNull(after, "after must not be null");
        return () -> {
            uiUpdated();
            after.uiUpdated();
        };
    }
}
