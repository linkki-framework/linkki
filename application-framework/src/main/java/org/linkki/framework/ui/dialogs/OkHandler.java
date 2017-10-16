/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.framework.ui.dialogs;

import javax.annotation.Nonnull;

import org.linkki.util.handler.Handler;

@FunctionalInterface
public interface OkHandler extends Handler {

    /** A handler that does nothing. */
    OkHandler NOP_HANDLER = () -> {
        // nothing to do
    };

    @Override
    default void apply() {
        onOk();
    }

    /**
     * Called when the user clicks OK.
     */
    void onOk();

    /**
     * Returns a composed handler that first executes this handler {@code onOk()} method and then
     * the {@code onOk()} method of the given handler.
     */
    @Override
    default OkHandler andThen(@Nonnull Handler after) {
        return () -> {
            apply();
            after.apply();
        };
    }

}