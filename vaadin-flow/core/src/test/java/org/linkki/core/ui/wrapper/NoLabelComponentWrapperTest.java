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

package org.linkki.core.ui.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.wrapper.WrapperType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;

public class NoLabelComponentWrapperTest extends BaseComponentWrapperTest {

    @Test
    public void testSetId() {
        Span component = spy(new Span());
        NoLabelComponentWrapper wrapper = new NoLabelComponentWrapper(component,
                WrapperType.FIELD);

        wrapper.setId("anotherId");

        verify(component).setId("anotherId");
    }

    @Test
    public void testSetEnabled() {
        Button button = spy(new Button());
        NoLabelComponentWrapper wrapper = new NoLabelComponentWrapper(button, WrapperType.FIELD);

        wrapper.setEnabled(true);

        verify(button).setEnabled(true);

        wrapper.setEnabled(false);

        verify(button).setEnabled(false);
    }

    @Test
    public void testSetVisible() {
        Button button = spy(new Button());
        NoLabelComponentWrapper wrapper = new NoLabelComponentWrapper(button,
                WrapperType.FIELD);

        wrapper.setVisible(true);

        verify(button).setVisible(true);

        wrapper.setVisible(false);

        verify(button).setVisible(false);
    }

    @Test
    public void testGetComponent() {
        Component component = mock(Component.class);
        NoLabelComponentWrapper wrapper = new NoLabelComponentWrapper(component,
                WrapperType.FIELD);

        assertThat(wrapper.getComponent(), is(sameInstance(component)));
    }

    @Test
    public void testGetType() {
        Span component = new Span();

        assertThat(new NoLabelComponentWrapper(component, WrapperType.FIELD).getType(),
                   is(WrapperType.FIELD));
        assertThat(new NoLabelComponentWrapper(component, WrapperType.LAYOUT).getType(),
                   is(WrapperType.LAYOUT));
    }

    @Test
    public void testSetTooltip() {
        Span component = new Span();
        NoLabelComponentWrapper wrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        wrapper.setTooltip("testTip");
        assertThat(getTitleAttribute(wrapper), is("testTip"));
        wrapper.setTooltip("<script>");
        assertThat(getTitleAttribute(wrapper), is(""));
        wrapper.setTooltip("<div> some text </div>");
        assertThat(getTitleAttribute(wrapper), is(" some text "));
        wrapper.setTooltip("<div> some text <br> with page break</div> ");
        assertThat(getTitleAttribute(wrapper), is(" some text \n with page break "));
    }

}
