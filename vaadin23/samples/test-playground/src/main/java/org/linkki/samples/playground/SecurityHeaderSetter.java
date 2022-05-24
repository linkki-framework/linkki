package org.linkki.samples.playground;

import com.vaadin.flow.server.communication.IndexHtmlRequestListener;
import com.vaadin.flow.server.communication.IndexHtmlResponse;

public class SecurityHeaderSetter implements IndexHtmlRequestListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void modifyIndexHtmlResponse(IndexHtmlResponse indexHtmlResponse) {
        indexHtmlResponse.getVaadinResponse().setHeader("Referrer-Policy", "no-referrer");
        indexHtmlResponse.getVaadinResponse().setHeader("X-Frame-Options", "deny");
        indexHtmlResponse.getVaadinResponse().setHeader("X-Content-Type-Options", "nosniff");
        indexHtmlResponse.getVaadinResponse().setHeader("Permissions-Policy", "microphone=(),camera=()");
    }
}