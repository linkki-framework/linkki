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

package org.linkki.core.vaadin.component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Key;

public class KeyCodeTest {

    @Test
    public void testEnter() {
        assertThat(KeyCode.ENTER, is(getString(Key.ENTER)));
    }

    private String getString(Key key) {
        List<String> keys = key.getKeys();

        if (keys.size() == 1) {
            return keys.get(0);
        } else {
            throw new IllegalArgumentException("Cannot extract string from key");
        }
    }

}
