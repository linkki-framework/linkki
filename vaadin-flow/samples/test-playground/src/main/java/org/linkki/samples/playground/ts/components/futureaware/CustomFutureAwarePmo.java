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

package org.linkki.samples.playground.ts.components.futureaware;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

@UIVerticalLayout
public class CustomFutureAwarePmo {

    @UICustomFutureAwareComponent(position = 10)
    public CompletableFuture<String> getString() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            return "I am loaded asynchronously after 3 seconds!";
        });
    }

    @UICustomFutureAwareComponent(position = 20)
    public CompletableFuture<String> getStringWithError() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            throw new RuntimeException("Error loading the value!");
        });
    }

}
