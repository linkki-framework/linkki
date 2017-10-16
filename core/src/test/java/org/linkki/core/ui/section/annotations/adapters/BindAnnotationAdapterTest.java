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
package org.linkki.core.ui.section.annotations.adapters;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;

public class BindAnnotationAdapterTest {

    @Bind(pmoProperty = "")
    public Bind defaultAnnotation() {
        try {
            return getClass().getMethod("defaultAnnotation", new Class<?>[] {}).getAnnotation(Bind.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Bind(pmoProperty = "", availableValues = AvailableValuesType.DYNAMIC)
    public Bind customAnnotation() {
        try {
            return getClass().getMethod("customAnnotation", new Class<?>[] {}).getAnnotation(Bind.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAvailableValues_DefaultValueIsUsed() {
        BindAnnotationAdapter adapter = new BindAnnotationAdapter(defaultAnnotation());
        assertThat(adapter.availableValues(), is(AvailableValuesType.NO_VALUES));
    }

    @Test
    public void testAvailableValues_CustomValuesIsRead() {
        BindAnnotationAdapter adapter = new BindAnnotationAdapter(customAnnotation());
        assertThat(adapter.availableValues(), is(AvailableValuesType.DYNAMIC));
    }

}
