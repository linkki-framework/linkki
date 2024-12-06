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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindClosable;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

@UIVerticalLayout
public class ClosableSections {

    @UINestedComponent(position = 10)
    public AttributeClosableTruePmo getAttributeClosableTruePmo() {
        return new AttributeClosableTruePmo();
    }

    @UINestedComponent(position = 20)
    public BindClosableInitialFalsePmo getBindClosableSectionInitialFalse() {
        return new BindClosableInitialFalsePmo();
    }

    @UINestedComponent(position = 30)
    public BindClosableInitialTruePmo getBindClosableSectionInitialTrue() {
        return new BindClosableInitialTruePmo();
    }

    @BindCaption("Section with attribute closable = true")
    @UISection(closeable = true)
    public static class AttributeClosableTruePmo {

        @UILabel(position = 10)
        public String getLabel() {
            return "I am a label.";
        }
    }

    @BindCaption("Section with @BindClosable")
    @BindClosable(initial = false)
    @UISection
    public static class BindClosableInitialFalsePmo {
        @UILabel(position = 10)
        public String getLabel() {
            return "I am a label.";
        }
    }

    @BindCaption("Section with @BindClosable(initial = true)")
    @BindClosable(initial = true)
    @UISection
    public static class BindClosableInitialTruePmo {
        @UILabel(position = 10)
        public String getLabel() {
            return "I am a label.";
        }
    }

}
