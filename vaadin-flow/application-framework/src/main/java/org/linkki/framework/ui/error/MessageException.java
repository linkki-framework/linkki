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

package org.linkki.framework.ui.error;

/**
 * This exception can be used to display error messages on the {@link LinkkiErrorPage}, that are
 * specifically tailored to the user of the application. The message is displayed as-is, any
 * localization must take place on the calling side.
 * <p>
 * Since the message is displayed regardless of the deployment environment of the application, it
 * <b>must not expose internal details</b> (e.g. technical or sensitive information) about the
 * system.
 */
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code MessageException} with the given message.
     * <p>
     * This constructor is <b>only intended for errors directly caused by the user</b>, which do not
     * have any impact on the application. Since it is purely informational, it is <b>not logged</b>
     * by the default implementation of {@link LinkkiErrorPage}. An example use case would be the
     * user specifying an invalid ID in the URL.
     * 
     * @param message the localized message displayed to the end user.
     */
    public MessageException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MessageException} with the given message and cause.
     * <p>
     * This constructor is <b>only intended for application errors</b>, for which a user-friendly
     * message should be shown. The cause is therefore <b>logged with the same severity as regular
     * unhandled exceptions</b> by the default implementation of {@link LinkkiErrorPage}.
     * 
     * @param message the localized message displayed to the end user.
     * @param cause the cause. It is not shown in a production deployment, so it may contain
     *            internal details about the system.
     */
    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
