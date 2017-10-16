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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

public class TableColumnDescriptorTest {

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

    private TableColumnDescriptor newDescriptor(String methodName) throws NoSuchMethodException, SecurityException {
        Method method = TestClassWithAnnotations.class.getDeclaredMethod(methodName, new Class<?>[] {});
        UITableColumn annotation = method.getAnnotation(UITableColumn.class);
        return new TableColumnDescriptor(TestClassWithAnnotations.class, method, annotation);
    }

    @Test
    public void testIsCustomWidthDefined() throws NoSuchMethodException, SecurityException {
        TableColumnDescriptor widthDefined = newDescriptor("methodWithWidthAnnotation");
        TableColumnDescriptor expandRatioDefined = newDescriptor("methodWithExpandRatioAnnotation");
        TableColumnDescriptor bothDefined = newDescriptor("methodWithWidthAndExpandRatioAnnotation");
        TableColumnDescriptor nothingDefined = newDescriptor("methodWithEmptyAnnotation");

        assertThat(widthDefined.isCustomWidthDefined(), is(true));
        assertThat(expandRatioDefined.isCustomWidthDefined(), is(false));
        assertThat(bothDefined.isCustomWidthDefined(), is(true));
        assertThat(nothingDefined.isCustomWidthDefined(), is(false));
    }

    @Test
    public void testGetWidth() throws NoSuchMethodException, SecurityException {
        TableColumnDescriptor widthDefined = newDescriptor("methodWithWidthAnnotation");
        TableColumnDescriptor expandRatioDefined = newDescriptor("methodWithExpandRatioAnnotation");
        TableColumnDescriptor bothDefined = newDescriptor("methodWithWidthAndExpandRatioAnnotation");
        TableColumnDescriptor nothingDefined = newDescriptor("methodWithEmptyAnnotation");

        assertThat(widthDefined.getWidth(), is(100));
        assertThat(expandRatioDefined.getWidth(), is(UITableColumn.UNDEFINED_WIDTH));
        assertThat(bothDefined.getWidth(), is(100));
        assertThat(nothingDefined.getWidth(), is(UITableColumn.UNDEFINED_WIDTH));
    }

    @Test
    public void testIsCustomExpandRatioDefined() throws NoSuchMethodException, SecurityException {
        TableColumnDescriptor widthDefined = newDescriptor("methodWithWidthAnnotation");
        TableColumnDescriptor expandRatioDefined = newDescriptor("methodWithExpandRatioAnnotation");
        TableColumnDescriptor bothDefined = newDescriptor("methodWithWidthAndExpandRatioAnnotation");
        TableColumnDescriptor nothingDefined = newDescriptor("methodWithEmptyAnnotation");

        assertThat(widthDefined.isCustomExpandRatioDefined(), is(false));
        assertThat(expandRatioDefined.isCustomExpandRatioDefined(), is(true));
        assertThat(bothDefined.isCustomExpandRatioDefined(), is(true));
        assertThat(nothingDefined.isCustomExpandRatioDefined(), is(false));
    }

    @Test
    public void testGetExpandRatio() throws NoSuchMethodException, SecurityException {
        TableColumnDescriptor widthDefined = newDescriptor("methodWithWidthAnnotation");
        TableColumnDescriptor expandRatioDefined = newDescriptor("methodWithExpandRatioAnnotation");
        TableColumnDescriptor bothDefined = newDescriptor("methodWithWidthAndExpandRatioAnnotation");
        TableColumnDescriptor nothingDefined = newDescriptor("methodWithEmptyAnnotation");

        assertThat(widthDefined.getExpandRatio(), is(UITableColumn.UNDEFINED_EXPAND_RATIO));
        assertThat(expandRatioDefined.getExpandRatio(), is(12.34f));
        assertThat(bothDefined.getExpandRatio(), is(12.34f));
        assertThat(nothingDefined.getExpandRatio(), is(UITableColumn.UNDEFINED_EXPAND_RATIO));
    }

    @Test
    public void testCheckValidConfiguration() throws NoSuchMethodException, SecurityException {
        TableColumnDescriptor widthDefined = newDescriptor("methodWithWidthAnnotation");
        TableColumnDescriptor expandRatioDefined = newDescriptor("methodWithExpandRatioAnnotation");
        TableColumnDescriptor nothingDefined = newDescriptor("methodWithEmptyAnnotation");

        // No exceptions should be thrown
        widthDefined.checkValidConfiguration();
        expandRatioDefined.checkValidConfiguration();
        nothingDefined.checkValidConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckValidConfiguration_ThrowsExceptionForInvalidConfiguration()
            throws NoSuchMethodException, SecurityException {
        TableColumnDescriptor bothDefined = newDescriptor("methodWithWidthAndExpandRatioAnnotation");

        bothDefined.checkValidConfiguration();
    }

}
