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
package org.linkki.core.ui.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// https://github.com/mvysny/karibu-testing/issues/
// Test the implementation if multiple test classes are present
@ExtendWith(KaribuUIExtension.class)
class KaribuUIExtensionCustomTemplateParallelTest {

    @Test
    void testConstruction_TemplateResourceLoadedFromMetaInfFrontend() {
        var component = new MinimalLitTemplateComponent();

        assertThat(component.content).isNotNull();
    }
}
