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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

import com.vaadin.flow.component.UI;

/**
 * Use with {@link KaribuUIExtension} to set the {@link UI}'s {@link Locale} for a test. Default is
 * {@link Locale#GERMAN}, {@link Locale#ROOT} if left out.
 * <p>
 * When using {@link KaribuUIExtension} with {@link org.junit.jupiter.api.extension.ExtendWith}.
 * When using {@link org.junit.jupiter.api.extension.RegisterExtension}, use
 * {@link org.linkki.core.ui.test.KaribuUIExtension.KaribuConfiguration#setLocale(Locale)} instead.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithLocale {
    String value() default "de";
}
