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

package org.linkki.framework.ui.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H2;

class HeadlineTest {

    @Test
    void testHeadline_HeadlineTitle() {
        var basicHeadlinePmo = new BasicHeadlinePmo();

        var headline = new Headline();
        var binder = new Binder(headline, basicHeadlinePmo);
        binder.setupBindings(new BindingContext());

        var title = headline.getContent().getChildren().toList().getFirst();
        assertThat(title).isInstanceOf(CompositeH2.class);
        assertThat(((H2)title).getText()).isEqualTo("Headline Title");
    }

    @Test
    void testHeadline_H2_TitleComponent() {
        // given
        var basicHeadlinePmo = new BasicHeadlinePmo();
        var headline = new Headline(new H2());

        // when
        headline.addToTitle(new Text("Subtitle"));

        var binder = new Binder(headline, basicHeadlinePmo);
        binder.setupBindings(new BindingContext());

        // then
        Component title = headline.getContent().getChildren().toList().getFirst();
        assertThat(title).isInstanceOf(H2.class);
        assertThat(((H2)title).getText()).isEqualTo("Headline Title");
        assertThat(title.getChildren()).as("H2#setText replaces all children").hasSize(1);
    }

    @Test
    void testHeadline_CompositeH2_TitleComponent() {
        // given
        var basicHeadlinePmo = new BasicHeadlinePmo();
        var headline = new Headline();

        // when
        var subTitle = new Text("Subtitle");
        headline.addToTitle(subTitle);

        var binder = new Binder(headline, basicHeadlinePmo);
        binder.setupBindings(new BindingContext());

        // then
        Component title = headline.getContent().getChildren().toList().getFirst();
        assertThat(title).isInstanceOf(CompositeH2.class);
        assertThat(((H2)title).getText()).isEqualTo("Headline Title");
        assertThat(title.getChildren()).hasSize(2);
        assertThat(title.getChildren().toList().getLast()).isEqualTo(subTitle);
    }

    @Test
    void testHeadline_MissingHeaderTitleMethod() {
        var binder = new Binder(new Headline(), new NoHeadlineTitleHeadlinePmo());
        var bindingContext = new BindingContext();

        assertThrows(LinkkiBindingException.class, () -> binder.setupBindings(bindingContext));
    }

    public static class BasicHeadlinePmo {

        public String getHeaderTitle() {
            return "Headline Title";
        }
    }

    public static class NoHeadlineTitleHeadlinePmo {
        // no title, no controls
    }
}
