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

package org.linkki.samples.playground.uitest.vaadin14;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.samples.playground.allelements.AllUiElementsModelObject;
import org.linkki.samples.playground.uitest.AbstractUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class AllUiElementsTest extends AbstractUiTest {

    @Test
    public void testLabel_HtmlContent() {
        DivElement label = $(DivElement.class).id("htmlContentLabel");

        assertThat(label.getText(), is("HTML\nContent"));
        assertThat(label.findElements(By.tagName("i")).size(), is(1));
        assertThat(label.findElements(By.tagName("b")).size(), is(1));
        assertThat(label.getProperty("innerHTML"),
                   is("<i style=\"color: red;\">HTML</i> <b>Content</b>"));
    }

    @Test
    public void testLabel_NotHtmlContent() {
        DivElement label = $(DivElement.class).id("notHtmlContentLabel");

        assertThat(label.getText(), is("<b>NOT</b> HTML Content"));
        assertThat(label.findElements(By.tagName("b")).size(), is(0));
        assertThat(label.findElement(By.tagName("span")).getProperty("innerHTML"),
                   is("&lt;b&gt;NOT&lt;/b&gt; HTML Content"));
    }

    @Test
    public void testLabel_StyleNames() {
        DivElement label = $(DivElement.class).id("textLabel");

        assertThat(label.getClassNames(),
                   containsInAnyOrder(LinkkiText.CLASS_NAME, "firstStyleName", "anotherStyleName"));
    }

    @Test
    public void testTextArea_Height() {
        TextAreaElement textArea = $(TextAreaElement.class).id(AllUiElementsModelObject.PROPERTY_LONGTEXT);

        // custom height defined in annotation
        assertThat(textArea.getPropertyString("style", "height"), is("5em"));
    }

    @Test
    public void testButton_ShortcutKey() {
        TextFieldElement textField = $(TextFieldElement.class).id(AllUiElementsModelObject.PROPERTY_TEXT);

        // the button shortcut should trigger even when another element is in focus
        textField.sendKeys(Keys.ENTER);

        NotificationElement notification = $(NotificationElement.class).first();
        assertThat(notification.getText(), is("Normal Button clicked"));
    }
}