/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/
package org.linkki.samples.playground.uitest.conditions;

import java.util.Objects;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.html.testbench.H3Element;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.HasStringValueProperty;
import com.vaadin.testbench.TestBenchElement;

/**
 * {@link ExpectedCondition ExpectedConditions} related to Vaadin Testbench elements.
 */
public class VaadinElementConditions {

    /**
     * An expectation for checking that a query matches at least one
     * {@link TestBenchElement#isDisplayed() displayed} element. The first matching element is returned.
     *
     * @param query the query used to find the element
     */
    public static <T extends TestBenchElement> ExpectedCondition<T> elementDisplayed(ElementQuery<T> query) {
        return new ExpectedCondition<T>() {
            @Override
            public T apply(WebDriver driver) {
                return findFirst(query, TestBenchElement::isDisplayed);
            }

            @Override
            public String toString() {
                return "presence of displayed element located by ElementQuery";
            }
        };
    }

    public static ExpectedCondition<NotificationElement> notificationDisplayed(String title) {
        return new ExpectedCondition<NotificationElement>() {
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
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                ElementQuery<NotificationElement> query = new ElementQuery<>(NotificationElement.class).context(driver);
                return !query.exists();
            }
        };
    }

    public static ExpectedCondition<DialogElement> dialogDisplayed(String title) {
        return new ExpectedCondition<DialogElement>() {
            @Override
            public DialogElement apply(WebDriver driver) {
                ElementQuery<DialogElement> query = new ElementQuery<>(DialogElement.class).context(driver);
                return findFirst(query, this::matches);
            }

            private boolean matches(DialogElement notification) {
                return notification.$(H3Element.class).first().getText().equals(title);
            }
        };
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
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String elementValue = element.getValue();
                return Objects.equals(elementValue, value);
            }
        };
    }

    public static ExpectedCondition<Boolean> isClosed(NotificationElement notification) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !notification.isOpen();
            }
        };
    }

    public static ExpectedCondition<Boolean> isClosed(DialogElement dialog) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !dialog.isOpen();
            }
        };
    }
}
