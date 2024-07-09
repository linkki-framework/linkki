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

package org.linkki.samples.playground.bugs.lin3884;

import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.util.HtmlContent;

/**
 * <ul>
 * <li>If the label of a required form layout item is empty/ blank, the required indicator must be
 * visible anyway.</li>
 * <li>If the label of a required form layout item is very long/ multiline, the required indicator
 * should be displayed on the top right of the label.</li>
 * <li>No scrollbar must be visible in any case.</li>
 * </ul>
 */
@UISection(caption = EmptyLabelComponentsPmo.CAPTION)
public class EmptyLabelComponentsPmo {

    public static final String CAPTION = "LIN-3884";

    @ModelObject
    private final TestPmo testPmo = new TestPmo();

    @UILabel(position = 10, label = "Bug description")
    public HtmlContent getDescription() {
        return HtmlContent
                .text("""
                        If the label of a required form layout item is empty/ blank, the required indicator must be
                        visible anyway. If the label of a required form layout item is very long/ multiline,
                        the required indicator should be displayed on the top right of the label.
                        No scrollbar must be visible in any case.

                        Check and uncheck the required checkbox and check if the required indicator is correct.
                        """);
    }

    @UICheckBox(position = 20, modelAttribute = "required")
    public void required() {
        // model binding
    }

    @UICheckBox(position = 30, label = "", required = RequiredType.DYNAMIC)
    public boolean getCheckboxWithEmptyLabel() {
        return testPmo.isFlag();
    }

    public void setCheckboxWithEmptyLabel(boolean flag) {
        testPmo.setFlag(flag);
    }

    public boolean isCheckboxWithEmptyLabelRequired() {
        return testPmo.isRequired();
    }

    @UICheckBox(position = 40, label = "This is a very long label that must be printed on at least two lines",
            required = RequiredType.DYNAMIC)
    public boolean getCheckboxWithLongLabel() {
        return testPmo.isFlag();
    }

    public void setCheckboxWithLongLabel(boolean flag) {
        testPmo.setFlag(flag);
    }

    public boolean isCheckboxWithLongLabelRequired() {
        return testPmo.isRequired();
    }

    private static class TestPmo {

        private boolean required = false;
        private boolean flag = false;

        public void setRequired(boolean required) {
            this.required = required;
        }

        public boolean isRequired() {
            return required;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public boolean isFlag() {
            return flag;
        }
    }

}
