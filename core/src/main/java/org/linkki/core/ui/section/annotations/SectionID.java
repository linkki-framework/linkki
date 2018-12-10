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
package org.linkki.core.ui.section.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that returns a section's id. That ID is used as id in the resulting
 * <code>&lt;div&gt;</code> element in the HTML tree, for example to identify it for UI testing.
 * <p>
 * The annotated method is called exactly once when creating the section. A section's ID will remain
 * the same while it is displayed. It is never updated dynamically.
 * <p>
 * If the PMO class has no method that is annotated with SectionID, its class name will be used as
 * an id (fallback). Note that if the class name changes, that ID also changes. This may cause UI
 * tests to break. So when a section is involved in UI testing, an ID should be supplied via a
 * method annotated with {@link SectionID @SectionID} to ensure future correctness.
 * <p>
 * If more than one method is annotated with {@link SectionID @SectionID}, the first one will be
 * used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SectionID {
    // No properties yet.
}
