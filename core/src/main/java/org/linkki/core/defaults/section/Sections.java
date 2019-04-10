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

package org.linkki.core.defaults.section;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.ui.element.UiElementCreator;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.pmo.SectionID;
import org.linkki.util.BeanUtils;

/**
 * Helper methods to create UI layout sections from presentation model objects.
 * 
 * @see UiElementCreator UiElementCreator to create the section's contents
 */
public class Sections {
    private Sections() {
        // prevent instantiation
    }

    /**
     * Returns the id to be used for the section created from the presentation model object.
     * 
     * @implNote uses a PMO method annotated with {@link SectionID @SectionID} if present and defaults
     *           to the {@link Class#getSimpleName() simple name} of the PMO class.
     * @param pmo a presentation model object
     * @return the section id
     */
    public static String getSectionId(Object pmo) {
        Optional<Method> idMethod = BeanUtils.getMethod(pmo.getClass(),
                                                        (m) -> m.isAnnotationPresent(SectionID.class));
        return idMethod.map(m -> getIdFromSectionIdMethod(pmo, m)).orElse(pmo.getClass().getSimpleName());
    }

    private static String getIdFromSectionIdMethod(Object pmo, Method m) {
        try {
            return requireNonNull((String)m.invoke(pmo),
                                  "The method annotated with @" + SectionID.class.getSimpleName()
                                          + " must not return null.");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new LinkkiBindingException(
                    "Cannot call method to get section ID from " + pmo.getClass().getName() + "#" + m.getName(), e);
        }
    }

    /**
     * Returns the {@link ButtonPmo} for the edit button of the section created from the presentation
     * model object if one is defined.
     * 
     * @implNote Uses {@link PresentationModelObject#getEditButtonPmo()} if the PMO implements
     *           {@link PresentationModelObject}.
     * @param pmo a presentation model object
     * @return the {@link ButtonPmo} for the edit button
     */
    public static Optional<ButtonPmo> getEditButtonPmo(Object pmo) {
        return (pmo instanceof PresentationModelObject) ? ((PresentationModelObject)pmo).getEditButtonPmo()
                : Optional.empty();
    }
}
