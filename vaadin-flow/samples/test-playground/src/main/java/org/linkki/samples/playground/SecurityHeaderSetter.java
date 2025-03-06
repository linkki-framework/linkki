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

import com.vaadin.flow.server.communication.IndexHtmlRequestListener;
import com.vaadin.flow.server.communication.IndexHtmlResponse;

import java.io.Serial;

// tag::index-html-request-listener[]
public class SecurityHeaderSetter implements IndexHtmlRequestListener {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void modifyIndexHtmlResponse(IndexHtmlResponse indexHtmlResponse) {
        indexHtmlResponse.getVaadinResponse().setHeader("Referrer-Policy", "no-referrer");
        indexHtmlResponse.getVaadinResponse().setHeader("X-Frame-Options", "deny");
        indexHtmlResponse.getVaadinResponse().setHeader("X-Content-Type-Options", "nosniff");
        indexHtmlResponse.getVaadinResponse().setHeader("Permissions-Policy", "microphone=(),camera=()");
    }
}
// end::index-html-request-listener[]
