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

package org.linkki.testbench.conditions;

import java.util.Objects;
import java.util.function.Predicate;

import org.linkki.testbench.pageobjects.OkCancelDialogElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.HasStringValueProperty;
import com.vaadin.testbench.TestBenchElement;

/**
 * {@link ExpectedCondition ExpectedConditions} related to Vaadin Testbench elements.
 */
public class VaadinElementConditions {

    private VaadinElementConditions() {
        // no instances
    }

    /**
     * An expectation for checking that a query matches at least one
     * {@link TestBenchElement#isDisplayed() displayed} element. The first matching element is
     * returned.
     *
     * @param query the query used to find the element
     */
    public static <T extends TestBenchElement> ExpectedCondition<T> elementDisplayed(ElementQuery<T> query) {
        return elementDisplayed(query, e -> true);
    }

    /**
     * An expectation for checking that a query matches at least one
     * {@link TestBenchElement#isDisplayed() displayed} element. The first matching element is
     * returned.
     *
     * @param query the query used to find the element
     */
    public static <T extends TestBenchElement> ExpectedCondition<T> elementDisplayed(ElementQuery<T> query,
            Predicate<T> filter) {
        return new ExpectedCondition<>() {
            @Override
            public T apply(WebDriver driver) {
                return findFirst(query, filter.and(TestBenchElement::isDisplayed));
            }

            @Override
            public String toString() {
                return "presence of displayed element located by ElementQuery";
            }
        };
    }

    public static ExpectedCondition<NotificationElement> notificationDisplayed(String title) {
        return new ExpectedCondition<>() {
            @Override
            public NotificationElement apply(WebDriver driver) {
                ElementQuery<NotificationElement> query = new ElementQuery<>(NotificationElement.class).context(driver);
                return findFirst(query, this::matches);
            }

            private boolean matches(NotificationElement notification) {
                return notification.getText().startsWith(title);
            }
        };
    }

    public static ExpectedCondition<Boolean> noNotificationsDisplayed() {
        return driver -> {
            ElementQuery<NotificationElement> query = new ElementQuery<>(NotificationElement.class).context(driver);
            return !query.exists();
        };
    }

    public static ExpectedCondition<OkCancelDialogElement> dialogDisplayed(String title) {
        return new ExpectedCondition<>() {
            @Override
            public OkCancelDialogElement apply(WebDriver driver) {
                var query = new ElementQuery<>(OkCancelDialogElement.class).context(driver);
                return findFirst(query, this::matches);
            }

            private boolean matches(OkCancelDialogElement dialog) {
                return dialog.isOpen() && dialog.getCaption().contentEquals(title);
            }
        };
    }

    public static ExpectedCondition<Boolean> dialogClosed(String title) {
        return driver -> new ElementQuery<>(OkCancelDialogElement.class).context(driver)
                .all()
                .stream()
                .noneMatch(dialog -> dialog.getCaption().contentEquals(title));
    }

    public static ExpectedCondition<Boolean> allDialogsClosed() {
        return driver -> !new ElementQuery<>(DialogElement.class).context(driver).exists();
    }

    private static <T extends TestBenchElement> T findFirst(ElementQuery<T> query, Predicate<T> filter) {
        for (T element : query.all()) {
            if (filter.test(element)) {
                return element;
            }
        }
        return null;
    }

    /**
     * An expectation for checking that the value of an element equals the given value.
     *
     * @param element the element to read the value from
     * @param value the value that is expected
     */
    public static ExpectedCondition<Boolean> elementHasValue(HasStringValueProperty element, String value) {
        return driver -> {
            String elementValue = element.getValue();
            return Objects.equals(elementValue, value);
        };
    }

    public static ExpectedCondition<Boolean> isClosed(NotificationElement notification) {
        return driver -> !notification.isOpen();
    }

    public static ExpectedCondition<Boolean> isClosed(DialogElement dialog) {
        return driver -> !dialog.isOpen();
    }
}
