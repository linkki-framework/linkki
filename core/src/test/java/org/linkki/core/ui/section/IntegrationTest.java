/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IntegrationTest {
    private static final String ANY_VALUE = "ukztu7kxju76r";
    @Nonnull
    private final TestPmoWithAnnotations pmo = new TestPmoWithAnnotations();
    @Nonnull
    private final PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();
    @Nonnull
    private final UIAnnotationReader reader = new UIAnnotationReader(TestPmoWithAnnotations.class);

    @Test
    public void testGetStaticToolTipFromUIComboBox() {
        ElementDescriptor elementDescriptor = reader.findDescriptor("xyz");

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getToolTip(), is(TestPmoWithAnnotations.STATIC_STRING));
    }

    @Test
    public void testGetStaticToolTipFromUIButton() {
        ElementDescriptor elementDescriptor = reader.findDescriptor("button1");

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getToolTip(), is(TestPmoWithAnnotations.STATIC_STRING));
    }

    @Test
    public void testGetDynamicToolTipFromUIButton() {
        ElementDescriptor elementDescriptor = reader.findDescriptor("button2");

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getToolTip(), is(TestPmoWithAnnotations.STATIC_STRING + "0"));
        pmo.button2();
        assertThat(dispatcher.getToolTip(), is(TestPmoWithAnnotations.STATIC_STRING + "1"));
    }

    @Test
    public void testGetDynamicToolTipFromUITextField() {
        ElementDescriptor elementDescriptor = reader.findDescriptor("abc");

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getToolTip(), is(pmo.getAbc()));
        pmo.setAbc(ANY_VALUE);
        assertThat(dispatcher.getToolTip(), is(ANY_VALUE));
    }

    @Test
    public void testGetStaticCaptionFromUIButton() {
        ElementDescriptor elementDescriptor = reader.findDescriptor("button1");

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getCaption(), is(TestPmoWithAnnotations.STATIC_STRING));
    }

    @Test
    public void testGetDynamicCaptionFromUIButton() {
        ElementDescriptor elementDescriptor = reader.findDescriptor("button2");

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getCaption(), is("0"));
        pmo.button2();
        assertThat(dispatcher.getCaption(), is("1"));
    }

    @UISection
    public static class TestPmoWithAnnotations {
        private String xyz = "xyz";
        private String abc = "abc";

        public static final String STATIC_STRING = "StaticString";

        public boolean buttonVisible = true;
        public boolean buttonEnabled = true;
        public int clickCount = 0;

        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        @UITextField(position = 1)
        public void abc() {
            // nothing to do
        }

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public boolean isAbcEnabled() {
            return true;
        }

        public String getAbcToolTip() {
            return abc;
        }

        @UIToolTip(toolTipType = ToolTipType.STATIC, text = STATIC_STRING)
        @UIComboBox(position = 2, content = AvailableValuesType.NO_VALUES)
        public void xyz() {
            // nothing to do
        }

        public String getXyz() {
            return xyz;
        }

        public void setXyz(String xyz) {
            this.xyz = xyz;
        }

        @UIToolTip(text = STATIC_STRING)
        @UIButton(position = 3, caption = STATIC_STRING)
        public void button1() {
            // nothing to do
        }

        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        @UIButton(position = 4, captionType = CaptionType.DYNAMIC)
        public void button2() {
            clickCount++;
        }

        public String getButton2Caption() {
            return String.valueOf(clickCount);
        }

        public String getButton2ToolTip() {
            return STATIC_STRING + clickCount;
        }

        @ModelObject
        public Object getModelObject() {
            return StringUtils.EMPTY;
        }

    }
}