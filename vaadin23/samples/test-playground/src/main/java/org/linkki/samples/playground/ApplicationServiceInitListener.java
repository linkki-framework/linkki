package org.linkki.samples.playground;

import org.springframework.stereotype.Component;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

// tag::vaadin-service-init-listener[]
@Component
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addIndexHtmlRequestListener(new SecurityHeaderSetter());
    }
}
// end::vaadin-service-init-listener[]