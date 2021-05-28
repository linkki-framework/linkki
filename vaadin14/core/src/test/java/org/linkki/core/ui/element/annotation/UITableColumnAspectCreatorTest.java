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

package org.linkki.core.ui.element.annotation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

import edu.umd.cs.findbugs.annotations.NonNull;

public class UITableColumnAspectCreatorTest {

    @Test
    public void testWidthAndExpandRatioAreExclusive_OK() throws NoSuchMethodException, SecurityException {
        assertNotNull(create("methodWithWidthAnnotation"));
        assertNotNull(create("methodWithExpandRatioAnnotation"));
        assertNotNull(create("methodWithEmptyAnnotation"));
    }

    @Test
    public void testWidthAndExpandRatioAreExclusive_Fail() throws SecurityException {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            create("methodWithWidthAndExpandRatioAnnotation");
        });
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

        @UITableColumn(expandRatio = 1234)
        public void methodWithExpandRatioAnnotation() {
            // Noting to do
        }

        @UITableColumn(width = 100, expandRatio = 1234)
        public void methodWithWidthAndExpandRatioAnnotation() {
            // Noting to do
        }

        @UITableColumn()
        public void methodWithEmptyAnnotation() {
            // Noting to do
        }

    }

}
