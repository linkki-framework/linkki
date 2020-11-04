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

package org.linkki.samples.playground.uitest.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;

public class WebElementMatchers {

    public static WebElementEnabledMatcher enabled() {
        return new WebElementEnabledMatcher(true);
    }

    public static WebElementEnabledMatcher disabled() {
        return new WebElementEnabledMatcher(false);
    }

    public static class WebElementEnabledMatcher extends TypeSafeMatcher<WebElement> {

        private boolean enabled;

        public WebElementEnabledMatcher(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(enabled ? "enabled" : "disabled");
        }

        @Override
        protected void describeMismatchSafely(WebElement item, Description mismatchDescription) {
            mismatchDescription.appendValue(item).appendText(" was ")
                    .appendText(item.isEnabled() ? "enabled" : "disabled");
        }

        @Override
        protected boolean matchesSafely(WebElement item) {
            return item.isEnabled() == enabled;
        }

    }
}
