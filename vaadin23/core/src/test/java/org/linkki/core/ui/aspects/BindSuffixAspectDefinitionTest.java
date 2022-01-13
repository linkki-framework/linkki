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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.ui.aspects.types.SuffixType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.textfield.TextArea;

class BindSuffixAspectDefinitionTest {

    private final PropertyDispatcher dispatcher = new PropertyDispatcherFactory()
            .createDispatcherChain(new TestObject(), BoundProperty.empty(),
                                   PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

    @Test
    public void testSuffixType_Static() {
        TextArea area = new TextArea();

        callSuffixAspectDefinition(area, "€", SuffixType.STATIC);
        String suffixResult = area.getSuffixComponent().getElement().getText();

        assertThat(suffixResult, is("€"));
    }

    @Test
    public void testSuffixType_Dynamic() {
        TextArea area = new TextArea();

        callSuffixAspectDefinition(area, "€", SuffixType.DYNAMIC);
        String suffixResult = area.getSuffixComponent().getElement().getText();

        assertThat(suffixResult, is("%"));
    }

    @Test
    public void testSuffixType_AutoEmptySuffix() {
        TextArea area = new TextArea();

        callSuffixAspectDefinition(area, "", SuffixType.AUTO);
        String suffixResult = area.getSuffixComponent().getElement().getText();

        assertThat(suffixResult, is("%"));
    }

    @Test
    public void testSuffixType_AutoFilledSuffix() {
        TextArea area = new TextArea();

        callSuffixAspectDefinition(area, "€", SuffixType.AUTO);
        String suffixResult = area.getSuffixComponent().getElement().getText();

        assertThat(suffixResult, is("€"));
    }

    public void callSuffixAspectDefinition(TextArea area, String value, SuffixType suffixType) {
        SuffixAspectDefinition aspectDefinition = new SuffixAspectDefinition(value, suffixType);
        createUiUpdaterAndApplyIt(aspectDefinition, area);
    }

    public void createUiUpdaterAndApplyIt(SuffixAspectDefinition aspectDefinition, TextArea area) {
        aspectDefinition.createUiUpdater(dispatcher, new NoLabelComponentWrapper(area)).apply();
    }

    private static class TestObject {

        @SuppressWarnings("unused")
        public String getSuffix() {
            return "%";
        }

    }
}
