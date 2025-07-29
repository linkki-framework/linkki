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
package org.linkki.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

import com.tngtech.archunit.lang.ArchRule;

/**
 * Holds arch rules that can be enforced in different parts of this project.
 */
public class LinkkiArchRules {

    public static final ArchRule KARIBU_UI_EXTENSION_SHOULD_NOT_BE_STATIC = fields()
            .that().haveRawType("org.linkki.core.ui.test.KaribuUIExtension")
            .should().notBeStatic();

    private LinkkiArchRules() {
        // utility class
    }
}