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
package org.linkki.samples.appsample;

import java.io.Serial;

import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.linkki.samples.appsample.model.InMemoryBusinessPartnerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;

@StyleSheet(LinkkiTheme.STYLESHEET)
@SpringBootApplication
public class BusinessPartnerApplication implements AppShellConfigurator {

    @Serial
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        SpringApplication.run(BusinessPartnerApplication.class, args);
    }

    // tag::createRepository[]
    @Bean
    public BusinessPartnerRepository createBusinessPartnerRepository() {
        return InMemoryBusinessPartnerRepository.newSampleRepository();
    }
    // end::createRepository[]
}
