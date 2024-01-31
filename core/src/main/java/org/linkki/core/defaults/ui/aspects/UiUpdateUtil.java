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
package org.linkki.core.defaults.ui.aspects;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

/**
 * Provides utility for ui updates.
 */
public class UiUpdateUtil {

    private UiUpdateUtil() {
        // utility class
    }

    /**
     * Handles a caught {@link RuntimeException} during UI update. Giving the {@link PropertyDispatcher}
     * and the aspect this method creates a useful exception and throws a {@link LinkkiBindingException}
     * wrapping the exception.
     * <p>
     * Subclasses may change the behavior, throw more useful exceptions or silently ignore the exception
     * (beware this might lead to very difficult search for failures).
     *
     * @param e the exception that was caught
     * @param propertyDispatcher the {@link PropertyDispatcher} that was used to update the UI
     * @param aspect the aspect that was used to update the UI
     */
    public static <V> void handleUiUpdateException(RuntimeException e,
                                         PropertyDispatcher propertyDispatcher,
                                         Aspect<V> aspect) {
        Object boundObject = propertyDispatcher.getBoundObject();
        throw new LinkkiBindingException(
                e.getMessage() +
                        " while applying " +
                        (aspect.getName().isEmpty() ? "value" : "aspect " + aspect.getName()) +
                        " of " +
                        (boundObject != null ? boundObject.getClass() : "<no object>") +
                        "#" + propertyDispatcher.getProperty(),
                e);
    }

}
