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

package org.linkki.framework.ui.pmo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.framework.ui.application.ApplicationInfo;

class ApplicationInfoPmoTest {

    @Test
    void testGetTitle() {
        ApplicationInfoPmo pmo = new ApplicationInfoPmo(new TestApplicationInfo());

        assertThat(pmo.getTitle(), containsString(TestApplicationInfo.NAME));
    }

    @Test
    void testGetVersion() {
        ApplicationInfoPmo pmo = new ApplicationInfoPmo(new TestApplicationInfo());

        assertThat(pmo.getVersion(), containsString(TestApplicationInfo.VERSION));
    }

    @Test
    void testGetDescription() {
        ApplicationInfoPmo pmo = new ApplicationInfoPmo(new TestApplicationInfo());

        assertThat(pmo.getDescription(), containsString(TestApplicationInfo.DESCRIPTION));
    }

    @Test
    void testGetCopyright() {
        ApplicationInfoPmo pmo = new ApplicationInfoPmo(new TestApplicationInfo());

        assertThat(pmo.getCopyright(), containsString(TestApplicationInfo.COPYRIGHT));
    }

    @Test
    void testGetDialogCaption() {
        ApplicationInfoPmo pmo = new ApplicationInfoPmo(new TestApplicationInfo());

        assertThat(pmo.getDialogCaption(), containsString(TestApplicationInfo.NAME));
    }

    @Test
    void testGetDialogWidth() {
        ApplicationInfoPmo pmo = new ApplicationInfoPmo(new TestApplicationInfo());

        assertThat(pmo.getDialogWidth(), is("600px"));
    }

    private static class TestApplicationInfo implements ApplicationInfo {

        private static final String NAME = "name";
        private static final String VERSION = "version";
        private static final String DESCRIPTION = "description";
        private static final String COPYRIGHT = "copyright";

        @Override
        public String getApplicationName() {
            return NAME;
        }

        @Override
        public String getApplicationVersion() {
            return VERSION;
        }

        @Override
        public String getApplicationDescription() {
            return DESCRIPTION;
        }

        @Override
        public String getCopyright() {
            return COPYRIGHT;
        }

    }
}
