package org.linkki.core.ui.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.ComponentConditions.anyChildSatisfying;
import static org.linkki.core.ui.test.ComponentConditions.childOf;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

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
    void testAnyChildSatisfying() {
        var grandChild = new VerticalLayout();
        var child = new Div(grandChild);
        var layout = new Div(child);

        assertThat(layout)
                .has(anyChildSatisfying(Div.class::isInstance, "is an instance of Div"));
        assertThat(layout)
                .doesNotHave(anyChildSatisfying(Span.class::isInstance, "is an instance of Span"));
        assertThat(layout)
                .has(anyChildSatisfying(VerticalLayout.class::isInstance, "is an instance of VerticalLayout"));

    }
}
