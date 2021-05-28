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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UILink.LinkTarget;
import org.linkki.core.ui.element.annotation.UILinkIntegrationTest.LinkTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.html.Anchor;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class UILinkIntegrationTest extends ComponentAnnotationIntegrationTest<Anchor, LinkTestPmo> {

    public UILinkIntegrationTest() {
        super(LinkTestPmo::new);
    }

    @Test
    public void testValue_Dynamic() {
        Anchor link = getDynamicComponent();

        assertThat(getDefaultPmo().getValue(), is(nullValue()));
        // TODO LIN-2052
        // assertThat(link.getIcon(), is(nullValue()));

        getDefaultPmo().setValue("https://faktorzehn.org");
        modelChanged();

        assertThat(link.getHref(), is("https://faktorzehn.org"));

        getDefaultPmo().setValue("");
        modelChanged();

        assertThat(link.getHref(), is(""));
    }

    @Test
    public void testLinkCaption() {
        Anchor anchor = getDynamicComponent();

        assertThat(anchor.getText(), is(LinkTestPmo.INITIAL_CAPTION));

        getDefaultPmo().setValueCaption("caption");
        modelChanged();

        assertThat(anchor.getText(), is("caption"));
    }

    @Test
    public void testLinkCaption_Static() {
        Anchor anchor = getStaticComponent();

        assertThat(anchor.getText(), is(LinkTestPmo.STATIC_CAPTION));
    }

    @Test
    public void testLinkCaption_Default() {
        Anchor anchor = getComponentById("defaultsLink");

        assertThat(anchor.getText(), is(""));
    }

    @Test
    public void testTarget_Dynamic() {
        Anchor anchor = getDynamicComponent();

        assertThat(anchor.getTarget().get(), is("_top"));

        getDefaultPmo().setValueTarget("_parent");
        modelChanged();

        assertThat(anchor.getTarget().get(), is("_parent"));

        getDefaultPmo().setValueTarget("");
        modelChanged();

        assertThat(anchor.getTarget().isPresent(), is(false));
    }

    @Test
    public void testTarget_Static() {
        Anchor anchor = getStaticComponent();

        assertThat(anchor.getTarget().get(), is("_blank"));
    }

    @Test
    public void testTarget_Default() {
        Anchor anchor = getComponentById("defaultsLink");

        assertThat(anchor.getTarget().get(), is("_self"));
    }

    public void testEnabled() {
        assertThat(getStaticComponent().isEnabled(), is(true));
        assertThat(getDynamicComponent().isEnabled(), is(true));
    }

    @UISection
    protected static class LinkTestPmo extends AnnotationTestPmo {

        public static final String INITIAL_CAPTION = "https://linkki-framework.org";
        public static final String READONLY_LINK = "link";
        public static final String STATIC_CAPTION = "caption";

        @CheckForNull
        private String value;
        private String valueCaption;
        private String valueTarget;

        public LinkTestPmo(Object modelObject) {
            super(modelObject);
            this.valueCaption = INITIAL_CAPTION;
            this.valueTarget = "_top";
        }

        @CheckForNull
        @UILink(position = 1, visible = VisibleType.DYNAMIC, captionType = CaptionType.DYNAMIC, target = LinkTarget.DYNAMIC, label = "")
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValueCaption() {
            return valueCaption;
        }

        public void setValueCaption(String valueCaption) {
            this.valueCaption = valueCaption;
        }

        public String getValueTarget() {
            return valueTarget;
        }

        public void setValueTarget(String valueTarget) {
            this.valueTarget = valueTarget;
        }

        @UILink(position = 2, label = TEST_LABEL, visible = VisibleType.INVISIBLE, caption = STATIC_CAPTION, target = "_blank")
        public String getStaticValue() {
            return READONLY_LINK;
        }

        @UILink(position = 3)
        public String getDefaultsLink() {
            return READONLY_LINK;
        }

        public String getDefaultsLinkCaption() {
            return "";
        }

        @Override
        public void staticValue() {
            // not needed as UI Link does not support model binding
        }

        @Override
        public void value() {
            // not needed as UI Link does not support model binding
        }
    }
}
