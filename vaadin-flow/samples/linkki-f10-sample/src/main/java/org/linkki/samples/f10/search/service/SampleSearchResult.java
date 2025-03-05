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
package org.linkki.samples.f10.search.service;

import java.util.List;

import org.linkki.core.binding.validation.message.MessageList;

public class SampleSearchResult {

    public static final int DEFAULT_MAX_RESULT_SIZE = 100;

    private List<SampleModelObject> result;
    private MessageList messages;

    SampleSearchResult(List<SampleModelObject> result, MessageList messages) {
        this.result = result;
        this.messages = messages;
    }

    public List<SampleModelObject> getResult() {
        return result;
    }

    public MessageList getMessages() {
        return messages;
    }

    public int getMaxResult() {
        return DEFAULT_MAX_RESULT_SIZE;
    }

}
