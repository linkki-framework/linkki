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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.html.NativeLabel;

public class CaptionAspectDefinitionTest {


    @Test
    public void testCreateAspect_Static() {
        CaptionAspectDefinition captionAspectDefinition = new CaptionAspectDefinition(CaptionType.STATIC, "foo");

        Aspect<String> aspect = captionAspectDefinition.createAspect();

        assertThat(aspect.getName(), is(CaptionAspectDefinition.NAME));
        assertThat(aspect.getValue(), is("foo"));
    }

    @Test
    public void testCreateAspect_Auto_WithValue() {
        CaptionAspectDefinition captionAspectDefinition = new CaptionAspectDefinition(CaptionType.AUTO, "foo");

        Aspect<String> aspect = captionAspectDefinition.createAspect();

        assertThat(aspect.getName(), is(CaptionAspectDefinition.NAME));
        assertThat(aspect.getValue(), is("foo"));
    }

    @Test
    public void testCreateAspect_Auto_WithoutValue() {
        CaptionAspectDefinition captionAspectDefinition = new CaptionAspectDefinition(CaptionType.AUTO, "");

        Aspect<String> aspect = captionAspectDefinition.createAspect();

        assertThat(aspect.getName(), is(CaptionAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateAspect_Dynamic() {
        CaptionAspectDefinition captionAspectDefinition = new CaptionAspectDefinition(CaptionType.DYNAMIC, "foo");

        Aspect<String> aspect = captionAspectDefinition.createAspect();

        assertThat(aspect.getName(), is(CaptionAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateAspect_None() {
        CaptionAspectDefinition captionAspectDefinition = new CaptionAspectDefinition(CaptionType.NONE, "foo");

        Aspect<String> aspect = captionAspectDefinition.createAspect();

        assertThat(aspect.getName(), is(CaptionAspectDefinition.NAME));
        assertThat(aspect.getValue(), is(nullValue()));
    }

    @Test
    public void testCreateComponentValueSetter() {
        CaptionAspectDefinition captionAspectDefinition = new CaptionAspectDefinition(CaptionType.DYNAMIC, "foo");
        NativeLabel component = new NativeLabel();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component);

        Consumer<String> componentValueSetter = captionAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept("bar");
        assertThat(component.getText(), is("bar"));
    }

}
