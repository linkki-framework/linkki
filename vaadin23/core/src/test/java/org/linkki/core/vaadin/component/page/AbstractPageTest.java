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

package org.linkki.core.vaadin.component.page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

class AbstractPageTest {

    @Test
    void testInit() {
        TestPage testPage = new TestPage();
        assertThat(testPage.getComponentCount(), is(0));

        testPage.init();

        assertThat(testPage.getComponentCount(), is(1));
    }

    @Test
    void testAddSection() {
        TestPage testPage = new TestPage();
        assertThat(testPage.getComponentCount(), is(0));

        testPage.addSection(new TestPmo());

        assertThat(testPage.getComponentCount(), is(1));
        assertThat(testPage.getComponentAt(0), is(instanceOf(LinkkiSection.class)));
    }

    @Test
    void testAddSections() {
        TestPage testPage = new TestPage();
        assertThat(testPage.getComponentCount(), is(0));

        testPage.addSections(new TestPmo(), new TestPmo());

        assertThat(testPage.getComponentCount(), is(1));
        assertThat(testPage.getComponentAt(0), is(instanceOf(HorizontalLayout.class)));
        assertThat(((HorizontalLayout)testPage.getComponentAt(0)).getComponentCount(), is(2));
    }

    static class TestPage extends AbstractPage {

        private static final long serialVersionUID = 1L;

        private final BindingManager bindingManager = new DefaultBindingManager();

        public TestPage() {
            super();
        }

        public TestPage(PmoBasedSectionFactory factory) {
            super(factory);
        }

        @Override
        public void createContent() {
            addSection(new TestPmo());
        }

        @Override
        protected BindingManager getBindingManager() {
            return bindingManager;
        }

    }

    @UISection
    static class TestPmo {
        // no content
    }
}
