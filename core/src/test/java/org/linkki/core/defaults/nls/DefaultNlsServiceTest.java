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
package org.linkki.core.defaults.nls;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.linkki.core.nls.NlsService;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class DefaultNlsServiceTest {
    private static final String KEY2 = "Key2";

    private static final String KEY1 = "Key1";

    private static final String BUNDLE_NAME = "messages";

    private static final String BUNDLE_NAME_NOT_EXISTS = BUNDLE_NAME + "1";

    private static final String defaultValue = "value";

    private static final Locale RUSSIAN_LOCALE = Locale.of("ru", "RU");

    private static final Locale GERMAN_LOCALE = Locale.GERMAN;

    private static final Locale ENGLISH_LOCALE = Locale.ENGLISH;

    private static final Locale CHINESE_LOCALE = Locale.CHINESE;

    private static final NlsService service = new DefaultNlsService();

    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    @Nested
    class BasicTests {

        public static Object[][] data() {
            return new Object[][] {
                // @formatter:off
                    /* [0] */ { KEY2, RUSSIAN_LOCALE, Optional.of("Ок") },
                    /* [1] */ { KEY1, RUSSIAN_LOCALE, Optional.of("Отмена") },
                    /* [2] */ { KEY2, GERMAN_LOCALE, Optional.of("Ok") },
                    /* [3] */ { KEY1, GERMAN_LOCALE, Optional.of("Abbrechen") },
                    /* [4] */ { KEY2, ENGLISH_LOCALE, Optional.of("Ok") },
                    /* [5] */ { KEY1, ENGLISH_LOCALE, Optional.of("Cancel") },
                    /* [6] */ { KEY2, CHINESE_LOCALE, Optional.of("Ok") }, // not existing locale
                    /* [7] */ { KEY1, CHINESE_LOCALE, Optional.of("Cancel") }, // not existing locale
                    /* [8] */ { KEY1 + KEY2, CHINESE_LOCALE, Optional.empty() }// not existing key
                // @formatter:on
            };
        }

        @ParameterizedTest
        @MethodSource("data")
        public void testBundleExists(String key, Locale locale, Optional<String> result) {
            assertEquals(result, service.getString(BUNDLE_NAME, key, locale));
        }

        @ParameterizedTest
        @MethodSource("data")
        public void testBundleNotExists(String key,
                Locale locale,
                @SuppressWarnings("unused") Optional<String> result) {
            assertEquals(Optional.empty(), service.getString(BUNDLE_NAME_NOT_EXISTS, key, locale));
        }
    }

    @SuppressFBWarnings(value = "NP_NONNULL_PARAM_VIOLATION", justification = "because we test this here")
    @Nested
    class NullTests {
        @Test
        public void nullBundleTest() {
            Assertions.assertThrows(NullPointerException.class, () -> {
                service.getString(null, KEY1, RUSSIAN_LOCALE);
            });
        }

        @Test
        public void nullKeyTest() {
            Assertions.assertThrows(NullPointerException.class, () -> {
                service.getString(BUNDLE_NAME, null, RUSSIAN_LOCALE);
            });
        }

        @Test
        public void nullLocaleTest() {
            Locale l = null;
            Assertions.assertThrows(NullPointerException.class, () -> {
                service.getString(BUNDLE_NAME, KEY1, l);
            });

        }

        @Test
        public void nullDefaultTest() {
            Assertions.assertThrows(NullPointerException.class, () -> {
                service.getString(BUNDLE_NAME, "foo", null, RUSSIAN_LOCALE);
            });
        }

        @Test
        public void nullDefaultTest2() {
            String s = null;
            Assertions.assertThrows(NullPointerException.class, () -> {
                service.getString(BUNDLE_NAME, "foo", s);
            });

        }
    }

    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    @Nested
    class DefaultValuesTests {

        public static Object[][] data() {
            return new Object[][] {
                // @formatter:off
                    /* [0] */ { KEY2, RUSSIAN_LOCALE, "Ок" },
                    /* [1] */ { KEY1, RUSSIAN_LOCALE, "Отмена" },
                    /* [2] */ { KEY2, GERMAN_LOCALE, "Ok" },
                    /* [3] */ { KEY1, GERMAN_LOCALE, "Abbrechen" },
                    /* [4] */ { KEY2, ENGLISH_LOCALE, "Ok" },
                    /* [5] */ { KEY1, ENGLISH_LOCALE, "Cancel" },
                    /* [6] */ { KEY2, CHINESE_LOCALE, "Ok" }, // not existing locale
                    /* [7] */ { KEY1, CHINESE_LOCALE, "Cancel" }, // not existing locale
                    /* [8] */ { KEY1 + KEY2, CHINESE_LOCALE, defaultValue }// not existing key
                // @formatter:on
            };
        }

        @ParameterizedTest
        @MethodSource("data")
        public void testBundleExists(String key, Locale locale, String result) {
            assertEquals(result, service.getString(BUNDLE_NAME, key, defaultValue, locale));
        }

        @ParameterizedTest
        @MethodSource("data")
        public void testBundleNotExists(String key, Locale locale, @SuppressWarnings("unused") String result) {
            assertEquals(defaultValue, service.getString(BUNDLE_NAME_NOT_EXISTS, key, defaultValue, locale));
        }
    }

}