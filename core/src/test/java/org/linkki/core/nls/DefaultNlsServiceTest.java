/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.nls;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Enclosed.class)
public class DefaultNlsServiceTest {
    private static final String KEY2 = "Key2";

    private static final String KEY1 = "Key1";

    private static final String BUNDLE_NAME = "messages";

    private static final String BUNDLE_NAME_NOT_EXISTS = BUNDLE_NAME + "1";

    private static final String defaultValue = "value";

    private static final Locale RUSSIAN_LOCALE = new Locale("ru", "RU");

    private static final Locale GERMAN_LOCALE = Locale.GERMAN;

    private static final Locale ENGLISH_LOCALE = Locale.ENGLISH;

    private static final Locale CHINESE_LOCALE = Locale.CHINESE;

    private static final NlsService service = new DefaultNlsService();

    @SuppressWarnings("null")
    @RunWith(Parameterized.class)
    public static class BasicTests {


        @Parameterized.Parameter(value = 0)
        public String key;

        @Parameterized.Parameter(value = 1)
        public Locale locale;

        @Parameterized.Parameter(value = 2)
        public Optional<String> result;

        @Parameterized.Parameters
        public static Object[][] parameters() {

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

        @Test
        public void testBundleExists() {


            assertEquals(result, service.getString(BUNDLE_NAME, key, locale));

        }

        @Test
        public void testBundleNotExists() {


            assertEquals(Optional.empty(), service.getString(BUNDLE_NAME_NOT_EXISTS, key, locale));

        }


    }

    public static class NullTests {
        @SuppressWarnings("null")
        @Test(expected = NullPointerException.class)
        public void nullBundleTest() {
            service.getString(null, KEY1, RUSSIAN_LOCALE);
        }

        @SuppressWarnings("null")
        @Test(expected = NullPointerException.class)
        public void nullKeyTest() {
            service.getString(BUNDLE_NAME, null, RUSSIAN_LOCALE);
        }

        @SuppressWarnings("null")
        @Test(expected = NullPointerException.class)
        public void nullLocaleTest() {
            Locale l = null;
            service.getString(BUNDLE_NAME, KEY1, l);
        }

        @SuppressWarnings("null")
        @Test(expected = NullPointerException.class)
        public void nullDefaultTest() {
            service.getString(BUNDLE_NAME, "foo", null, RUSSIAN_LOCALE);
        }

        @SuppressWarnings("null")
        @Test(expected = NullPointerException.class)
        public void nullDefaultTest2() {
            String s = null;
            service.getString(BUNDLE_NAME, "foo", s);
        }
    }

    @SuppressWarnings("null")
    @RunWith(Parameterized.class)
    public static class DefaultValuesTests {


        @Parameterized.Parameter(value = 0)
        public String key;

        @Parameterized.Parameter(value = 1)
        public Locale locale;

        @Parameterized.Parameter(value = 2)
        public String result;

        @Parameterized.Parameters
        public static Object[][] parameters() {

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

        @Test
        public void testBundleExists() {


            assertEquals(result, service.getString(BUNDLE_NAME, key, defaultValue, locale));

        }

        @Test
        public void testBundleNotExists() {


            assertEquals(defaultValue, service.getString(BUNDLE_NAME_NOT_EXISTS, key, defaultValue, locale));

        }
    }


}