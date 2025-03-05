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
import static org.linkki.core.ui.test.ComponentConditions.anyChildSatisfying;
import static org.linkki.core.ui.test.ComponentConditions.anyVisibleChildOfType;
import static org.linkki.core.ui.test.ComponentConditions.childOf;
import static org.linkki.core.ui.test.ComponentConditions.exactlyOneVisibleChildOfType;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

class ComponentConditionsTest {

    @Test
    void testChildOf() {
        var grandChild = new Div();
        var child = new Div(grandChild);
        var layout = new Div(child);

        assertThat(child).is(childOf(layout));
        assertThat(grandChild).is(childOf(layout));
        assertThat(layout).isNot(childOf(child));
    }

    @Test
    void testChildOf_FailureText() {
        var child = new VerticalLayout(new Span("child"));
        var parent = new Div(child);

        try {
            assertThat(parent).is(childOf(child));
        } catch (AssertionError e) {
            assertThat(e.getMessage())
                    .as("The component tree of the parent should be displayed as message")
                    .contains("VerticalLayout", "Span", "child");
        }
    }

    @Test
    void testAnyChildSatisfying() {
        var grandChild = new VerticalLayout();
        var child = new Div(grandChild);
        var layout = new Div(child);

        assertThat(layout)
                .has(anyChildSatisfying(Div.class::isInstance, "is an instance of Div"))
                .has(anyChildSatisfying(VerticalLayout.class::isInstance, "is an instance of VerticalLayout"))
                .doesNotHave(anyChildSatisfying(Span.class::isInstance, "is an instance of Span"));
    }

    @Test
    void testAnyChildSatisfying_FailureText() {
        var layout = new Div();

        try {
            assertThat(layout)
                    .has(anyChildSatisfying(Span.class::isInstance, "is an instance of Span"));
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("is an instance of Span");
        }
    }

    @Test
    void testOneVisibleChildOfType() {
        var grandChild = new Span("visible grand child");
        var invisibleGrandChild = new Span("invisible grand child");
        invisibleGrandChild.setVisible(false);
        var grandChildWithSameType = new Div();
        grandChildWithSameType.setClassName("css");
        var child = new Div(grandChild, invisibleGrandChild, grandChildWithSameType);
        var uniqueChild = new H3();
        var layout = new Div(child, uniqueChild);

        assertThat(layout)
                .has(exactlyOneVisibleChildOfType(H3.class))
                .doesNotHave(exactlyOneVisibleChildOfType(TextField.class))
                .as("Having multiple children of the same type should fail")
                .doesNotHave(exactlyOneVisibleChildOfType(Div.class))
                .as("Search spec should be respected")
                .has(exactlyOneVisibleChildOfType(Div.class, s -> s.withClasses("css")))
                .as("Invisible children should not count")
                .has(exactlyOneVisibleChildOfType(Span.class));
    }

    @Test
    void testOneVisibleChildOfType_FailureMessage() {
        var layout = new Div();

        try {
            assertThat(layout)
                    .has(exactlyOneVisibleChildOfType(H3.class, s -> s.withClasses("css").withText("text")));
        } catch (AssertionError e) {
            assertThat(e.getMessage())
                    .contains("exactly one visible child", "H3", "text", "css");
        }
    }

    @Test
    void testAnyVisibleChildOfType() {
        var grandChild = new Span("visible grand child");
        var invisibleGrandChild = new H1("invisible grand child");
        invisibleGrandChild.setVisible(false);
        var grandChildWithSameType = new Div();
        grandChildWithSameType.setClassName("css");
        var child = new Div(grandChild, invisibleGrandChild, grandChildWithSameType);
        var uniqueChild = new H3();
        var layout = new Div(child, uniqueChild);

        assertThat(layout)
                .has(anyVisibleChildOfType(H3.class))
                .doesNotHave(anyVisibleChildOfType(TextField.class))
                .as("Having multiple children of the same type should not fail")
                .has(anyVisibleChildOfType(Div.class))
                .as("Search spec should be respected")
                .doesNotHave(anyVisibleChildOfType(Span.class, s -> s.withText("non existing")))
                .as("Invisible children should not count")
                .doesNotHave(anyVisibleChildOfType(H1.class));
    }

    @Test
    void testAnyVisibleChildOfType_FailureMessage() {
        var layout = new Div();

        try {
            assertThat(layout)
                    .has(anyVisibleChildOfType(H3.class, s -> s.withClasses("css").withText("text")));
        } catch (AssertionError e) {
            assertThat(e.getMessage())
                    .contains("any visible child", "H3", "text", "css");
        }
    }
}
