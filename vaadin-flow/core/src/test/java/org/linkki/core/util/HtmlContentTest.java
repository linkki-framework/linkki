package org.linkki.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.icon.VaadinIcon;

class HtmlContentTest {

    @Test
    void testStyledTag() {
        assertThat(HtmlContent.styledTag("div", "color: red;", "hello").toString())
                .isEqualToIgnoringWhitespace("<div style=\"color: red;\">hello</div>");
    }

    @Test
    void testSanitizedText() {
        assertThat(HtmlContent.sanitizeText("<h1>Hasso</h1>").toString())
                .isEqualToIgnoringWhitespace("Hasso");
    }

    @Test
    void testSanitizedText_br() {
        assertThat(HtmlContent.sanitizeText("<br>").toString())
                .isEqualToIgnoringWhitespace("<br>");
    }

    @Test
    void testIcon() {
        var icon = VaadinIcon.TAG.create();
        icon.setColor("red");
        assertThat(HtmlContent.icon(icon).toString())
                .isEqualToIgnoringWhitespace("""
                        <vaadin-icon icon="vaadin:tag" style="fill:red"></vaadin-icon>""");
    }

    @Test
    void testText() {
        assertThat(HtmlContent.text("hello").toString())
                .isEqualToIgnoringWhitespace("hello");
    }

    @Test
    void testText_escapeInput() {
        assertThat(HtmlContent.text("<b>Hasso</b>").toString())
                .isEqualToIgnoringWhitespace("&lt;b&gt;Hasso&lt;/b&gt;");
    }
}