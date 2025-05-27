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
package org.linkki.samples.appsample.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@SuppressWarnings("all")
public class InMemoryBusinessPartnerRepository implements BusinessPartnerRepository {

    private final SortedMap<UUID, BusinessPartner> map = new TreeMap<>();

    @Override
    public Set<BusinessPartner> findBusinessPartners(String searchterm) {
        Set<BusinessPartner> retPartner = new LinkedHashSet<BusinessPartner>();
        if (searchterm != null && !searchterm.isEmpty()) {
            String searchTerm = searchterm.toLowerCase();
            for (BusinessPartner b : map.values()) {
                if (b.getName() != null && b.getName().toLowerCase().contains(searchTerm)) {
                    retPartner.add(b);
                }
                if (b.getDateOfBirth() != null && b.getDateOfBirth().toString().contains(searchTerm)) {
                    retPartner.add(b);
                }
                if (b.getUuid() != null && b.getUuid().toString().contains(searchTerm)) {
                    retPartner.add(b);
                }
                for (Address a : b.getAddresses()) {
                    if (a.getCity() != null && a.getCity().toLowerCase().contains(searchTerm)) {
                        retPartner.add(b);
                    }
                    if (a.getCountry() != null && a.getCountry().toLowerCase().contains(searchTerm)) {
                        retPartner.add(b);
                    }
                    if (a.getPostalCode() != null && a.getPostalCode().contains(searchTerm)) {
                        retPartner.add(b);
                    }
                    if (a.getStreet() != null && a.getStreet().toLowerCase().contains(searchTerm)) {
                        retPartner.add(b);
                    }
                    if (a.getStreetNumber() != null && a.getStreetNumber().contains(searchTerm)) {
                        retPartner.add(b);
                    }

                }
            }

        }
        return retPartner;
    }

    @Override
    public BusinessPartner getBusinessPartner(UUID id) {
        return map.get(id);
    }

    @Override
    public void saveBusinessPartner(BusinessPartner p) {
        map.put(p.getUuid(), p);
    }

    public static InMemoryBusinessPartnerRepository newSampleRepository() {
        InMemoryBusinessPartnerRepository repo = new InMemoryBusinessPartnerRepository();

        Address address1 = new Address("Im Zollhafen", "15/17", "Cologne", "Germany", "50678");
        Address address2 = new Address("Friedenheimer Bruecke", "21", "Munich", "Germany", "80639");
        Address address3 = new Address("Rosa-Luxemburg-Strasse", "14", "Berlin", "Germany", "10178");

        BusinessPartner p1 = new BusinessPartner("ac54f2f9-7b82-4bc8-ab69-554cac5926f1");
        p1.setName("John Doe");
        p1.addAddress(address1);
        p1.addAddress(address2);
        p1.setDateOfBirth(LocalDate.of(1978, Month.AUGUST, 27));

        repo.saveBusinessPartner(p1);

        BusinessPartner p2 = new BusinessPartner("ac54f2f9-7b82-4bc8-ab69-554cac5926f2");
        p2.setName("Jane Doe");
        repo.saveBusinessPartner(p2);

        BusinessPartner p3 = new BusinessPartner("ac54f2f9-7b82-4bc8-ab69-554cac5926f3");
        p3.setName("Average Doe");
        repo.saveBusinessPartner(p3);

        BusinessPartner p4 = new BusinessPartner("ac54f2f9-7b82-4bc8-ab69-554cac5926f4");
        p4.setName("OneAddress Doe");
        p4.addAddress(address3);
        repo.saveBusinessPartner(p4);

        BusinessPartner p5 = new BusinessPartner("ac54f2f9-7b82-4bc8-ab69-554cac5926f5");
        p5.addAddress(address1);
        repo.saveBusinessPartner(p5);

        return repo;

    }
}
