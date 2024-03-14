/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.samples.appsample.model;

import java.util.Set;
import java.util.UUID;

public interface BusinessPartnerRepository {

    /**
     * Returns all {@link BusinessPartner} whose data (e.g. name, address etc.) matches the given search
     * term.
     * 
     * @param searchTerm the term to search for
     * @return the list of matching {@link BusinessPartner}
     */
    Set<BusinessPartner> findBusinessPartners(String searchTerm);

    BusinessPartner getBusinessPartner(UUID id);

    /**
     * Saves the given {@link BusinessPartner}, i.e. adds it to the repository if its a new partner or
     * updates an existing partner.
     * 
     * @param p the {@link BusinessPartner} to save
     */
    void saveBusinessPartner(BusinessPartner p);

}
