/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.nls;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueNlsService;
import org.linkki.core.nls.sample.SamplePmo;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.element.annotation.TestUiUtil;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;

public class StaticValueNlsServiceSectionTest {

    private String sectionCaption;

    private Button buttonWithTranslatedCaption;

    private String textfieldLabelWithoutTranslation;

    private String textfieldLabelWithTranslation;

    private String textfieldLabelTranslation;

    private String buttonLabelTranslation;

    private String buttonCaptionTranslation;

    private Button buttonWithoutTranslatedCaption;


    @BeforeEach
    public void setUp() {
        BindingContext context = new BindingContext();
        LinkkiSection section = new PmoBasedSectionFactory().createSection(new SamplePmo(), context);
        sectionCaption = section.getCaption();
        Div sectionContent = section.getContentWrapper();

        textfieldLabelWithTranslation = TestUiUtil.getLabelOfComponentAt(sectionContent, 0);
        textfieldLabelWithoutTranslation = TestUiUtil.getLabelOfComponentAt(sectionContent, 1);
        buttonWithTranslatedCaption = (Button)TestUiUtil.getComponentAtIndex(2, sectionContent);
        buttonWithoutTranslatedCaption = (Button)TestUiUtil.getComponentAtIndex(3, sectionContent);

        // test setup
        textfieldLabelTranslation = getLabelTranslation(SamplePmo.PROPERTY_TEXTFIELD);
        assertThat(textfieldLabelTranslation, is(not(SamplePmo.PMO_LABEL)));
        buttonLabelTranslation = getLabelTranslation(SamplePmo.PROPERTY_MYBUTTON);
        assertThat(buttonLabelTranslation, is(not(SamplePmo.PMO_LABEL)));
        buttonCaptionTranslation = getCaptionTranslation(SamplePmo.PROPERTY_MYBUTTON);
        assertThat(buttonCaptionTranslation, is(not(SamplePmo.PMO_CAPTION)));

        assertThat(getLabelTranslation(SamplePmo.PROPERTY_CBFIELD), is(SamplePmo.PMO_LABEL));
        assertThat(getCaptionTranslation(SamplePmo.PROPERTY_MYBUTTON2), is(SamplePmo.PMO_CAPTION));
    }

    private String getLabelTranslation(String property) {
        return StaticValueNlsService.getInstance().getString(SamplePmo.class, property, "label", SamplePmo.PMO_LABEL);
    }

    private String getCaptionTranslation(String property) {
        return StaticValueNlsService.getInstance().getString(SamplePmo.class, property, CaptionAspectDefinition.NAME,
                                            SamplePmo.PMO_CAPTION);
    }

    @Test
    public void testSectionCaption() {
        assertThat(sectionCaption, is("Translated section caption"));
    }

    @Test
    public void testCaptions() {
        assertThat(buttonWithTranslatedCaption.getText(), is(buttonCaptionTranslation));
        assertThat(buttonWithoutTranslatedCaption.getText(), is(SamplePmo.PMO_CAPTION));
    }

    @Test
    public void testLabels() {
        assertThat(textfieldLabelWithTranslation, is(textfieldLabelTranslation));
        assertThat(textfieldLabelWithoutTranslation, is(SamplePmo.PMO_LABEL));
    }
}
