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
package org.linkki.testbench.util;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public final class Locator {

    private static String locatorXpathWithValue = "//*[contains(@value,'%s')]";
    private static String locatorXpathWithId = "//*[contains(@id,'%s')]";
    private static String locatorXpathWithText = "//*[contains(text(),'%s')]";

    private Locator() {
        // prevents instantiation
    }

    public static By byText(String text) {
        return by(locatorXpathWithText, text);
    }

    public static By byValue(String value) {
        return by(locatorXpathWithValue, value);
    }

    public static By byId(String id) {
        return by(locatorXpathWithId, id);
    }

    private static By by(String xPath, String valueToReplace) {
        return new By() {
            @Override
            public List<WebElement> findElements(SearchContext context) {
                List<WebElement> elements = context.findElements(By.xpath(String.format(xPath, valueToReplace)));
                return elements;
            }
        };
    }
}
