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

package org.linkki.core.ui.bind.annotation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.bind.annotation.Bind.BindAnnotationBoundPropertyCreator;

public class BindAnnotationBoundPropertyCreatorTest {

    private BindAnnotationBoundPropertyCreator factory = new BindAnnotationBoundPropertyCreator();

    
    @Test
    public void testCreateBoundProperty() throws Exception {
        Method method = TestClass.class.getMethod("aMethod");
        Bind bindAnnotation = method.getAnnotation(Bind.class);

        BoundProperty boundProperty = factory.createBoundProperty(bindAnnotation, method);

        assertThat(boundProperty, is(notNullValue()));
        assertThat(boundProperty.getPmoProperty(), is("prop"));
        assertThat(boundProperty.getModelObject(), is("model"));
        assertThat(boundProperty.getModelAttribute(), is("attr"));
    }

    
    @Test
    public void testCreateBoundProperty_DefaultValues() throws Exception {
        Method method = TestClass.class.getMethod("anotherMethod");
        Bind bindAnnotation = method.getAnnotation(Bind.class);

        BoundProperty boundProperty = factory.createBoundProperty(bindAnnotation, method);

        assertThat(boundProperty, is(notNullValue()));
        assertThat(boundProperty.getPmoProperty(), is("anotherMethod"));
        assertThat(boundProperty.getModelObject(), is(ModelObject.DEFAULT_NAME));
        // empty model attribute defaults to pmo property
        assertThat(boundProperty.getModelAttribute(), is("anotherMethod"));
    }

    public static class TestClass {

        @Bind(pmoProperty = "prop", modelObject = "model", modelAttribute = "attr")
        public void aMethod() {
            // that does nothing
        }

        @Bind
        public void anotherMethod() {
            // that does nothing
        }

    }

}
