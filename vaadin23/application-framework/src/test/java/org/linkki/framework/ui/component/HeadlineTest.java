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

package org.linkki.framework.ui.component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;

public class HeadlineTest {

    @Test
    public void testHeadline_headlineTitle() {
        BasicHeadlinePmo basicHeadlinePmo = new BasicHeadlinePmo();
        BindingContext bindingContext = new BindingContext();

        Headline headline = new Headline();
        Binder binder = new Binder(headline, basicHeadlinePmo);
        binder.setupBindings(bindingContext);

        Component title = headline.getContent().getChildren().collect(Collectors.toList()).get(0);
        assertThat(title, is(instanceOf(H2.class)));
        assertThat(((H2)title).getText(), is("Headline Title"));
    }

    @Test
    public void testHeadline_missingHeaderTitleMethod() {
        Binder binder = new Binder(new Headline(), new NoHeadlineTitleHeadlinePmo());
        BindingContext bindingContext = new BindingContext();
        assertThrows(LinkkiBindingException.class, () -> binder.setupBindings(bindingContext));
    }

    static class BasicHeadlinePmo {

        public String getHeaderTitle() {
            return "Headline Title";
        }
    }

    static class NoHeadlineTitleHeadlinePmo {
        // no title, no controls
    }
}
