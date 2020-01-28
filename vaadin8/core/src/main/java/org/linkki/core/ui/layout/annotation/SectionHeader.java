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
package org.linkki.core.ui.layout.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.linkki.core.vaadin.component.section.AbstractSection;

import com.vaadin.ui.Component;

/**
 * Places the component into the section header. This annotation can only be used inside a
 * {@link UISection}.
 * <p>
 * It is recommended to use negative numbers for the position of elements marked with this annotation to
 * avoid conflicts with elements placed in the section's content area.
 * 
 * @implNote Components will be added using {@link AbstractSection#addHeaderComponent(Component)}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SectionHeader {
    // empty
}
