package org.linkki.core.ui.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

class ComponentTreeRepresentationTest {

    @Test
    void testToStringOf_Component() {
        var componentTreeRepresentation = new ComponentTreeRepresentation();
        var component = new VerticalLayout(new Div("text"));

        var representation = componentTreeRepresentation.toStringOf(component);

        assertThat(representation).contains("VerticalLayout", "Div", "text");
    }

    @Test
    void testToStringOf_NotAComponent() {
        var componentTreeRepresentation = new ComponentTreeRepresentation();
        var testObjectWithToString = new TestObjectWithToString();

        var representation = componentTreeRepresentation.toStringOf(testObjectWithToString);

        assertThat(representation).contains("custom string");
    }

    @Test
    void testInAssert() {
        try {
            var layout = new VerticalLayout(new Div("text"));

            assertThat(layout)
                    .withRepresentation(new ComponentTreeRepresentation())
                    .isNull();
        } catch (AssertionError e) {
            assertThat(e.getMessage()).contains("VerticalLayout", "Div", "text");
        }
    }

    private static final class TestObjectWithToString {
        @Override
        public String toString() {
            return "custom string";
        }
    }
}
