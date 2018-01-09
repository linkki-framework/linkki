/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.samples.appsample.config;

import javax.enterprise.context.ApplicationScoped;

import org.linkki.framework.state.ApplicationConfig;

@ApplicationScoped
public class ApplicationConfigSample implements ApplicationConfig {

    @Override
    public String getApplicationName() {
        return "linkki Application Sample";
    }

    @Override
    public String getApplicationVersion() {
        return "1.0";
    }

    @Override
    public String getCopyright() {
        return "© Faktor Zehn AG";
    }

}
