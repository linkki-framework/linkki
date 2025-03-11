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
package org.linkki.samples.playground.ts.navigation;

import java.util.HashMap;
import java.util.Objects;

/**
 * This might be a service or a UI state that handles the server state while the business object is
 * being edited.
 */
public class SampleConfirmService {

    private static final HashMap<String, SampleConfirmObject> SAMPLE_OBJECTS = new HashMap<>();

    public SampleConfirmObject loadSampleObject(String id) {
        return new SampleConfirmObject(id,
                SAMPLE_OBJECTS.computeIfAbsent(id, i -> new SampleConfirmObject(i, "")).getValue());
    }

    public void saveSampleObject(SampleConfirmObject newObject) {
        SAMPLE_OBJECTS.put(newObject.getId(), new SampleConfirmObject(newObject.getId(), newObject.getValue()));
    }

    public boolean isSaved(SampleConfirmObject object) {
        var sampleConfirmObject = SAMPLE_OBJECTS.get(object.getId());
        return sampleConfirmObject != null && Objects.equals(sampleConfirmObject.getValue(), object.getValue());
    }

}
