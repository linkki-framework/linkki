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
package org.linkki.samples.appsample.ui;

import java.time.Year;

import org.linkki.framework.ui.application.ApplicationInfo;

public class BusinessPartnerInfo implements ApplicationInfo {

    public static final String APPLICATION_NAME = "BusinessPartner";

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public String getApplicationVersion() {
        return "1.0";
    }

    @Override
    public String getApplicationDescription() {
        return "BusinessPartner";
    }

    @Override
    public String getCopyright() {
        return "© Faktor Zehn " + Year.now();
    }
}