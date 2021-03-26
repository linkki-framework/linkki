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

package org.linkki.core.ui.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.wrapper.WrapperType;

import com.vaadin.flow.component.textfield.TextField;

public class LabelComponentWrapperTest {

    @Test
    public void testSetLabel() {
        TextField component = new TextField();
        LabelComponentWrapper wrapper = new LabelComponentWrapper(component, WrapperType.FIELD);

        wrapper.setLabel("Label Text");

        assertThat(component.getElement().getProperty("label"), is("Label Text"));
    }

    @Test
    public void testSetLabel_Null() {
        TextField component = new TextField();
        LabelComponentWrapper wrapper = new LabelComponentWrapper(component, WrapperType.FIELD);

        wrapper.setLabel(null);

        assertThat(component.getElement().hasProperty("label"), is(false));
    }

    @Test
    public void testSetLabel_Empty() {
        TextField component = new TextField();
        LabelComponentWrapper wrapper = new LabelComponentWrapper(component, WrapperType.FIELD);

        wrapper.setLabel("");

        assertThat(component.getElement().hasProperty("label"), is(false));
    }

}
