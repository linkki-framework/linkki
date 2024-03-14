package org.linkki.samples.appsample;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.linkki.samples.appsample.model.BusinessPartnerRepository;
import org.linkki.samples.appsample.model.InMemoryBusinessPartnerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.Serial;

@Theme("linkki")
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
