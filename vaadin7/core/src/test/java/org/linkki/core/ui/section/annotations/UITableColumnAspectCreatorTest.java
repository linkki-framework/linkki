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

package org.linkki.core.ui.section.annotations;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Test;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;

import edu.umd.cs.findbugs.annotations.NonNull;

public class UITableColumnAspectCreatorTest {

    @Test
    public void testWidthAndExpandRatioAreExclusive_OK() throws NoSuchMethodException, SecurityException {
        assertNotNull(create("methodWithWidthAnnotation"));
        assertNotNull(create("methodWithExpandRatioAnnotation"));
        assertNotNull(create("methodWithEmptyAnnotation"));
    }

    @Test(expected = IllegalStateException.class)
    public void testWidthAndExpandRatioAreExclusive_Fail() throws NoSuchMethodException, SecurityException {
        create("methodWithWidthAndExpandRatioAnnotation");
    }

    private static LinkkiAspectDefinition create(String methodName) throws NoSuchMethodException, SecurityException {
        Method method = TestClassWithAnnotations.class.getDeclaredMethod(methodName, new Class<?>[] {});
        
        @NonNull
        UITableColumn annotation = method.getAnnotation(UITableColumn.class);
        return new UITableColumn.TableColumnAspectDefinitionCreator().create(annotation);
    }

    private static class TestClassWithAnnotations {

        @UITableColumn(width = 100)
        public void methodWithWidthAnnotation() {
            // Noting to do
        }

        @UITableColumn(expandRatio = 12.34f)
        public void methodWithExpandRatioAnnotation() {
            // Noting to do
        }

        @UITableColumn(width = 100, expandRatio = 12.34f)
        public void methodWithWidthAndExpandRatioAnnotation() {
            // Noting to do
        }

        @UITableColumn()
        public void methodWithEmptyAnnotation() {
            // Noting to do
        }

    }

}
