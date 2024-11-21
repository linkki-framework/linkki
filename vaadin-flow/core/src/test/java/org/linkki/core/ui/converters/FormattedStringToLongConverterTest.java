package org.linkki.core.ui.converters;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValueContext;

public class FormattedStringToLongConverterTest {

    private FormattedStringToLongConverter converter;
    private ValueContext context;

    @BeforeEach
    void setup() {
        converter = new FormattedStringToLongConverter("#,##0");
        context = new ValueContext(new Binder<>(), Locale.GERMAN);
    }

    @Test
    void testConvertToPresentation() {
        assertThat(converter.convertToPresentation(12345L, context)).isEqualTo("12.345");
        assertThat(converter.convertToPresentation(12345678910L, context)).isEqualTo("12.345.678.910");
    }

    @Test
    void testConvertToPresentation_Null() {
        assertThat(converter.convertToPresentation(null, context)).isEmpty();
    }

    @Test
    void testConvertToModel() {
        assertThat(converter.convertToModel("1.234", context).getOrThrow(AssertionError::new))
                .isEqualTo(1234L);
        assertThat(converter.convertToModel("12.345.678.910", context).getOrThrow(AssertionError::new))
                .isEqualTo(12345678910L);
    }

    @Test
    void testConvertToModel_Null() {
        assertThat(converter.convertToModel("", context).getOrThrow(AssertionError::new)).isNull();
    }

    @Test
    void testConvertToModel_Overflow() {
        assertThat(converter.convertToModel(String.valueOf((double) Long.MAX_VALUE + 1), context).isError()).isTrue();
    }

    @Test
    void testConvertToModel_NumberFormattedExceptionWithVeryLargeNumber() {
        assertThat(converter.convertToModel(
            "1234567890028366545423133234235234612561513456" +
                  "1234567890028366545423133234235234612561513456" +
                  "1234567890028366545423133234235234612561513456" +
                  "1234567890028366545423133234235234612561513456" +
                  "1234567890028366545423133234235234612561513456" +
                  "1234567890028366545423133234235234612561513456" +
                  "1234567890028366545423133234235234612561513456",
                  context)
            .isError())
        .isTrue();
    }

    @Test
    void testConvertToModel_ParseExceptionWithVeryLargeNumber() {
        assertThat(converter.convertToModel("no-number-input", context).isError()).isTrue();
    }

}
