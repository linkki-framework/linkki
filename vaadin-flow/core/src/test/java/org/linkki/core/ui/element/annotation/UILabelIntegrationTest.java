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
package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.aspects.FutureAwareAspectDefinition;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.ui.element.annotation.UILabelIntegrationTest.LabelTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nls.NlsText;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.ui.test.TestLogAppender;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.shared.communication.PushMode;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import edu.umd.cs.findbugs.annotations.CheckForNull;

class UILabelIntegrationTest extends ComponentAnnotationIntegrationTest<LinkkiText, LabelTestPmo> {

    private static final String STYLES = "blabla";

    private final BindingContext bindingContext = new BindingContext();
    private final TestLogAppender testLogAppender = new TestLogAppender();

    UILabelIntegrationTest() {
        super(TestModelObjectWithString::new, LabelTestPmo::new);
    }

    @BeforeEach
    void setupLogger() {
        testLogAppender.setContext((LoggerContext)LoggerFactory.getILoggerFactory());
        var gridItemsAspectDefinitionLogger = (Logger)LoggerFactory
                .getLogger(FutureAwareAspectDefinition.class);
        gridItemsAspectDefinitionLogger.setLevel(Level.DEBUG);
        gridItemsAspectDefinitionLogger.addAppender(testLogAppender);

        // Adds the logs from the error handler as exceptions during UI#access are logged by it.
        var errorHandlerLogger = (Logger)LoggerFactory
                .getLogger(UI.getCurrent().getSession().getErrorHandler().getClass());
        errorHandlerLogger.setLevel(Level.DEBUG);
        errorHandlerLogger.addAppender(testLogAppender);

        testLogAppender.start();
    }

    @Test
    void testLabelFieldValue() {
        LinkkiText label = getDynamicComponent();

        assertThat(label.getClassName()).contains(STYLES);
        assertThat(label.getText()).isBlank();

        ((TestModelObjectWithString)getDefaultModelObject()).setValue("fdsa");
        modelChanged();
        assertThat(label.getText()).isEqualTo("fdsa");
    }

    @Test
    void testLabelFieldValue_Integer_UsesConverter() {
        setModelObjectSupplier(TestModelObjectWithInteger::new);
        LinkkiText label = getDynamicComponent();

        assertThat(label.getClassName()).contains(STYLES);
        assertThat(label.getText()).isBlank();

        ((TestModelObjectWithInteger)getDefaultModelObject()).setValue(123456);
        modelChanged();
        assertThat(label.getText()).isEqualTo("123.456");
    }

    @Test
    void testEnabled() {
        assertThat(getStaticComponent().getElement().isEnabled()).isTrue();
        assertThat(getDynamicComponent().getElement().isEnabled()).isTrue();
    }

    @Test
    void testLocalDateUsesConverter() {
        assertThat(getComponentById("localDate").getText()).isEqualTo("06.05.1234");
    }

    @Test
    void testLocalDateTimeUsesConverter() {
        assertThat(getComponentById("localDateTime").getText()).isEqualTo("06.05.1234 07:08");
    }

    @Test
    void testBooleanUsesConverter() {
        assertThat(getComponentById("yesBoolean").getText()).isEqualTo("Ja");
        assertThat(getComponentById("noBoolean").getText()).isEqualTo("Nein");
        assertThat(getComponentById("nullBoolean").getText()).isEqualTo("");
    }

    @Test
    void testUnnamedEnumUsesToString() {
        assertThat(getComponentById("enum").getText()).isEqualTo("FLOOR");
    }

    @Test
    void testNamedEnumUsesGetName() {
        assertThat(getComponentById("namedEnum").getText()).isEqualTo("name");
    }

    @Test
    void testNamedObjectUsesGetName() {
        assertThat(getComponentById("namedObject").getText()).isEqualTo("name");
    }

    @Test
    void testNamedEnumWithToStringCaptionProviderUsesToString() {
        assertThat(getComponentById("namedEnumWithToStringCaptionProvider").getText()).isEqualTo("VALUE");
    }

    @Test
    void testIconPosition() {
        assertThat(getDynamicComponent().getIconPosition()).isEqualTo(IconPosition.LEFT);
        assertThat(getStaticComponent().getIconPosition()).isEqualTo(IconPosition.RIGHT);
    }

    @EnumSource(PushMode.class)
    @ParameterizedTest
    void testLabel_WithCompletableFuture(PushMode pushMode) throws NoSuchMethodException {
        var pmo = getDefaultPmo();
        var method = pmo.getClass().getMethod("getValueFromCompletableFuture");
        var ui = com.vaadin.flow.component.UI.getCurrent();
        ui.getPushConfiguration().setPushMode(pushMode);

        var wrapper = UiCreator
                .<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                      NoLabelComponentWrapper::new);
        var label = (LinkkiText)wrapper.getComponent();

        var warningLogs = testLogAppender.getLoggedEvents(Level.WARN);
        if (!pushMode.isEnabled()) {
            assertThat(warningLogs).hasSize(1);
            assertThat(warningLogs).first().asString()
                    .contains(pmo.getClass().getName())
                    .contains("valueFromCompletableFuture");
        } else {
            assertThat(warningLogs).isEmpty();
        }
        assertThat(label.getText())
                .as("Initially on creation: Text should be updated asynchronously disregarding the push mode")
                .isBlank();
        assertThat(label.getElement().hasAttribute("content-loading")).isTrue();
        assertThat(label.getElement().hasAttribute("has-loading-error")).isFalse();

        KaribuUtils.UI.push(ui);

        assertThat(label.getText()).isEqualTo("I am loaded asynchronously");
        assertThat(label.getElement().hasAttribute("content-loading")).isFalse();
    }

    @EnumSource(PushMode.class)
    @ParameterizedTest
    void testLabel_WithCompletableFuture_Exception(PushMode pushMode) throws NoSuchMethodException {
        var pmo = getDefaultPmo();
        var method = pmo.getClass().getMethod("getValueFromCompletableFuture");
        var ui = com.vaadin.flow.component.UI.getCurrent();
        ui.getPushConfiguration().setPushMode(pushMode);

        pmo.setException(true);

        var wrapper = UiCreator
                .<Component, NoLabelComponentWrapper> createUiElement(method, pmo, bindingContext,
                                                                      NoLabelComponentWrapper::new);
        var label = (LinkkiText)wrapper.getComponent();

        var warningLogs = testLogAppender.getLoggedEvents(Level.WARN);
        if (!pushMode.isEnabled()) {
            assertThat(warningLogs).hasSize(1);
            assertThat(warningLogs).first().asString()
                    .contains(pmo.getClass().getName())
                    .contains("valueFromCompletableFuture");
        } else {
            assertThat(warningLogs).isEmpty();
        }
        assertThat(label.getText())
                .as("Initially on creation: Text should be updated asynchronously disregarding the push mode")
                .isBlank();
        assertThat(label.getElement().hasAttribute("content-loading")).isTrue();
        assertThat(label.getElement().hasAttribute("has-loading-error")).isFalse();

        KaribuUtils.UI.push(ui);

        assertThat(label.getText()).isEmpty();
        assertThat(label.getElement().getStyle().get("--loading-error-message"))
                .contains(NlsText.getString("FutureAwareAspectDefinition.loadingError"));
        assertThat(label.getElement().hasAttribute("content-loading")).isFalse();
        assertThat(label.getElement().hasAttribute("has-loading-error")).isTrue();
    }

    @UISection
    protected static class LabelTestPmo extends AnnotationTestPmo {

        public static final String EXCEPTION_MESSAGE = "Exception message";

        private boolean exception = false;

        public LabelTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @UILabel(position = 1, visible = VisibleType.DYNAMIC, styleNames = STYLES)
        public void value() {
            // model binding
        }

        @Override
        @UILabel(position = 2, label = TEST_LABEL, visible = VisibleType.INVISIBLE, iconPosition = IconPosition.RIGHT)
        public void staticValue() {
            // model binding
        }

        // just have some further labels and check that the section could be created

        @UILabel(position = 3)
        public BigDecimal getOtherTyp() {
            return new BigDecimal("123");
        }

        @UILabel(position = 4)
        public int getInt() {
            return 1231234;
        }

        @UILabel(position = 5)
        public LocalDate getLocalDate() {
            return LocalDate.of(1234, 5, 6);
        }

        @UILabel(position = 6)
        public LocalDateTime getLocalDateTime() {
            return LocalDateTime.of(1234, 5, 6, 7, 8, 9);
        }

        @UILabel(position = 7)
        public RoundingMode getEnum() {
            return RoundingMode.FLOOR;
        }

        @UILabel(position = 8)
        public NamedEnum getNamedEnum() {
            return NamedEnum.VALUE;
        }

        @UILabel(position = 9)
        public NamedObject getNamedObject() {
            return new NamedObject();
        }

        @UILabel(position = 10, itemCaptionProvider = ToStringCaptionProvider.class)
        public NamedEnum getNamedEnumWithToStringCaptionProvider() {
            return NamedEnum.VALUE;
        }

        @UILabel(position = 11)
        public CompletableFuture<String> getValueFromCompletableFuture() {
            if (!exception) {
                return CompletableFuture.supplyAsync(() -> "I am loaded asynchronously");
            } else {
                return CompletableFuture.failedFuture(new RuntimeException(EXCEPTION_MESSAGE));
            }
        }

        @UILabel(position = 12)
        public Boolean getYesBoolean() {
            return true;
        }

        @UILabel(position = 13)
        public Boolean getNoBoolean() {
            return false;
        }

        @UILabel(position = 14)
        public Boolean getNullBoolean() {
            return null;
        }

        public void setException(boolean exception) {
            this.exception = exception;
        }

    }

    protected static class TestModelObjectWithString extends TestModelObject<String> {

        @CheckForNull
        private String value = null;

        @CheckForNull
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull String value) {
            this.value = value;
        }
    }

    protected static class TestModelObjectWithInteger extends TestModelObject<Integer> {

        @CheckForNull
        private Integer value = null;

        @CheckForNull
        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Integer value) {
            this.value = value;
        }
    }

    public static enum NamedEnum {
        VALUE;

        public String getName() {
            return "name";
        }
    }

    public static class NamedObject {

        public String getName() {
            return "name";
        }

        @Override
        public String toString() {
            return "toString";
        }
    }
}
