/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
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

public class IntegrationTest {

    private final TestPmoWithAnnotations pmo = new TestPmoWithAnnotations();

    private final PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();

    private final UIAnnotationReader reader = new UIAnnotationReader(TestPmoWithAnnotations.class);

    @Test
    public void testGetStaticCaptionFromUIButton() {
        ElementDescriptor elementDescriptor = reader.findDescriptors("button1").getDescriptor(pmo);

        PropertyDispatcher dispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        assertThat(dispatcher.getCaption(), is(TestPmoWithAnnotations.STATIC_STRING));
    }

    @Test
    public void testGetDynamicCaptionFromUIButton() {
        ElementDescriptor elementDescriptor = reader.findDescriptors("button2").getDescriptor(pmo);

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
