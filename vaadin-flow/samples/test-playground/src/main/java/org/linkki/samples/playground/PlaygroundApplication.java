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

package org.linkki.samples.playground;

import java.io.Serial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;

@Theme(value = "linkki")
@SpringBootApplication
public class PlaygroundApplication implements AppShellConfigurator {

    @Serial
    private static final long serialVersionUID = 5214399675194827218L;

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(PlaygroundApplication.class);
    }

    // tag::favicon[]
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "favicon-192.png", "192x192");
    }
    // end::favicon[]
}
