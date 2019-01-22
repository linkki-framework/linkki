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
package org.linkki.core.nls.pmo;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.nls.pmo.sample.SamplePmo;
import org.linkki.core.ui.section.BaseSection;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.section.annotations.aspect.CaptionAspectDefinition;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class PmoNlsServiceSectionTest {
    @SuppressWarnings("null")
    Label sectionHeader;
    @SuppressWarnings("null")
    private Label textfieldLabelWithoutTranslation;
    @SuppressWarnings("null")
    private Label textfieldLabelWithTranslation;
    @SuppressWarnings("null")
    private Label buttonLabelWithTranslation;
    @SuppressWarnings("null")
    private Label buttonWithoutLabel;
    @SuppressWarnings("null")
    private Button buttonWithTranslatedCaption;

    @SuppressWarnings("null")
    private String textfieldLabelTranslation;
    @SuppressWarnings("null")
    private String buttonLabelTranslation;
    @SuppressWarnings("null")
    private String buttonCaptionTranslation;
    @SuppressWarnings("null")
    private Button buttonWithoutTranslatedCaption;

    @SuppressWarnings("null")
    @Before
    public void setUp() {
        BindingContext context = TestBindingContext.create();
        BaseSection section = new PmoBasedSectionFactory().createBaseSection(new SamplePmo(), context);
        HorizontalLayout header = (HorizontalLayout)section.getComponent(0);
        sectionHeader = (Label)header.getComponent(0);
        @NonNull
        GridLayout sectionContent = (GridLayout)((Panel)section.getComponent(1)).getContent();
        textfieldLabelWithTranslation = (Label)sectionContent.getComponent(0, 0);
        textfieldLabelWithoutTranslation = (Label)sectionContent.getComponent(0, 1);
        buttonLabelWithTranslation = (Label)sectionContent.getComponent(0, 2);
        buttonWithTranslatedCaption = (Button)sectionContent.getComponent(1, 2);
        buttonWithoutLabel = (Label)sectionContent.getComponent(0, 3);
        buttonWithoutTranslatedCaption = (Button)sectionContent.getComponent(1, 3);

        // test setup
        PmoNlsService.get();
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
        return PmoNlsService.get().getLabel(SamplePmo.class, property, "label", SamplePmo.PMO_LABEL);
    }

    private String getCaptionTranslation(String property) {
        return PmoNlsService.get().getLabel(SamplePmo.class, property, CaptionAspectDefinition.NAME,
                                            SamplePmo.PMO_CAPTION);
    }

    @Test
    public void testSectionCaption() {
        assertThat(sectionHeader.getValue(), is("Translated section caption"));
    }

    @Test
    public void testLabels() {
        assertThat(textfieldLabelWithTranslation.getValue(), is(textfieldLabelTranslation));
        assertThat(textfieldLabelWithoutTranslation.getValue(), is(SamplePmo.PMO_LABEL));
        assertThat(buttonLabelWithTranslation.getValue(), is(buttonLabelTranslation));
        assertThat(buttonWithoutLabel.getValue(), is(""));
        assertThat(buttonWithTranslatedCaption.getCaption(), is(buttonCaptionTranslation));
        assertThat(buttonWithoutTranslatedCaption.getCaption(), is(SamplePmo.PMO_CAPTION));
    }
}
