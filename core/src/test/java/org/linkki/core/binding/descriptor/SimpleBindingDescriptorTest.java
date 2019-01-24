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

package org.linkki.core.binding.descriptor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.property.BoundProperty;
import org.linkki.core.ui.section.annotations.ModelObject;

public class SimpleBindingDescriptorTest {


    private List<LinkkiAspectDefinition> aspectDefs = Collections.emptyList();

    @Test
    public void testGetModelPropertyName() {
        assertThat(new SimpleBindingDescriptor("foo", aspectDefs).getModelPropertyName(), is("foo"));
        assertThat(new SimpleBindingDescriptor("foo", "bar", "baz", aspectDefs).getModelPropertyName(), is("bar"));
        assertThat(new SimpleBindingDescriptor(new BoundProperty("foo"), aspectDefs).getModelPropertyName(), is("foo"));
        assertThat(new SimpleBindingDescriptor(new BoundProperty("foo").withModelAttribute("bar"), aspectDefs)
                .getModelPropertyName(), is("bar"));
    }

    @Test
    public void testGetModelObjectName() {
        assertThat(new SimpleBindingDescriptor("foo", aspectDefs).getModelObjectName(), is(ModelObject.DEFAULT_NAME));
        assertThat(new SimpleBindingDescriptor("foo", "bar", "baz", aspectDefs).getModelObjectName(), is("baz"));
        assertThat(new SimpleBindingDescriptor(new BoundProperty("foo"), aspectDefs).getModelObjectName(),
                   is(ModelObject.DEFAULT_NAME));
        assertThat(new SimpleBindingDescriptor(new BoundProperty("foo").withModelObject("bar"), aspectDefs)
                .getModelObjectName(), is("bar"));
    }

    @Test
    public void testGetPmoPropertyName() {
        assertThat(new SimpleBindingDescriptor("foo", aspectDefs).getPmoPropertyName(), is("foo"));
        assertThat(new SimpleBindingDescriptor("foo", "bar", "baz", aspectDefs).getPmoPropertyName(), is("foo"));
        assertThat(new SimpleBindingDescriptor(new BoundProperty("foo"), aspectDefs).getPmoPropertyName(), is("foo"));
    }

}
