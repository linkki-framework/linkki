/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.tooling.apt.validator;

import java.util.HashMap;
import java.util.Map;

import javax.tools.Diagnostic.Kind;

import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;


/**
 * Helps with the conversion of options given as String to {@link Kind} instances.
 */
public final class Severity {


    private static final Map<String, Kind> MODES_BY_NAME = new HashMap<>();
    static {
        MODES_BY_NAME.put("IGNORE", Kind.OTHER);
        MODES_BY_NAME.put(Kind.ERROR.name(), Kind.ERROR);
        MODES_BY_NAME.put(Kind.WARNING.name(), Kind.WARNING);
        MODES_BY_NAME.put(Kind.NOTE.name(), Kind.NOTE);
    }

    private Severity() {
        // util
    }

    /**
     * Returns the severity of messages returned for the given message code as a {@link Kind}.
     * 
     * @see LinkkiAnnotationProcessor#toLinkkiAptOption(String)
     */
    public static Kind of(Map<String, String> options, String messageCode, Kind defaultValue) {
        return MODES_BY_NAME.getOrDefault(options.get(LinkkiAnnotationProcessor.toLinkkiAptOption(messageCode)),
                                          defaultValue);
    }

}
