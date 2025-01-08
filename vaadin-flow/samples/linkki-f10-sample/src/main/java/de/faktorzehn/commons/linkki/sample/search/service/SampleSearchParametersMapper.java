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

package de.faktorzehn.commons.linkki.sample.search.service;

import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_CITY;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_CLASSIFICATION;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_DATE_OF_BIRTH;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_EMAIL;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_GIVEN_NAME;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_IBAN;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_NAME;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_PARTNER_NUMBER;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_PHONE;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_POSTAL_CODE;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_SECTOR;
import static de.faktorzehn.commons.linkki.sample.search.service.SampleSearchParameters.PROPERTY_STREE_AND_NUMBER;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.faktorzehn.commons.linkki.search.model.SearchParameterMapper;
import de.faktorzehn.commons.linkki.search.util.ParamsUtil;

// tag::SampleSearchParametersMapper[]
public class SampleSearchParametersMapper implements SearchParameterMapper<SampleSearchParameters> {
    // end::SampleSearchParametersMapper[]

    @Override
    // tag::toSearchParameters[]
    public SampleSearchParameters toSearchParameters(Map<String, List<String>> queryParams) {
        var searchParams = new SampleSearchParameters();
        var parameters = ParamsUtil.flatten(queryParams);

        searchParams.setPartnerNumber(parameters.get(PROPERTY_PARTNER_NUMBER));
        // end::toSearchParameters[]

        searchParams.setName(parameters.get(PROPERTY_NAME));
        searchParams.setGivenName(parameters.get(PROPERTY_GIVEN_NAME));
        // tag::parseIsoDate[]
        ParamsUtil.parseIsoDate(parameters.get(PROPERTY_DATE_OF_BIRTH))
                .ifPresent(searchParams::setDateOfBirth);
        // end::parseIsoDate[]
        searchParams.setSector(parameters.get(PROPERTY_SECTOR));
        searchParams.setClassification(parameters.get(PROPERTY_CLASSIFICATION));
        searchParams.setPostalCode(parameters.get(PROPERTY_POSTAL_CODE));
        searchParams.setCity(parameters.get(PROPERTY_CITY));
        searchParams.setStreetAndNumber(parameters.get(PROPERTY_STREE_AND_NUMBER));
        searchParams.setEmail(parameters.get(PROPERTY_EMAIL));
        searchParams.setPhone(parameters.get(PROPERTY_PHONE));
        searchParams.setIban(parameters.get(PROPERTY_IBAN));

        return searchParams;
    }

    @Override
    // tag::toQueryParameters[]
    public Map<String, List<String>> toQueryParameters(SampleSearchParameters searchParams) {
        var queryParams = new LinkedHashMap<String, String>();

        queryParams.put(PROPERTY_PARTNER_NUMBER, searchParams.getPartnerNumber());
        // end::toQueryParameters[]

        queryParams.put(PROPERTY_NAME, searchParams.getName());
        queryParams.put(PROPERTY_GIVEN_NAME, searchParams.getGivenName());
        // tag::formatIsoDate[]
        queryParams.put(PROPERTY_DATE_OF_BIRTH, ParamsUtil.formatIsoDate(searchParams.getDateOfBirth()));
        // end::formatIsoDate[]
        queryParams.put(PROPERTY_SECTOR, searchParams.getSector());
        queryParams.put(PROPERTY_CLASSIFICATION, searchParams.getClassification());
        queryParams.put(PROPERTY_POSTAL_CODE, searchParams.getPostalCode());
        queryParams.put(PROPERTY_CITY, searchParams.getCity());
        queryParams.put(PROPERTY_STREE_AND_NUMBER, searchParams.getStreetAndNumber());
        queryParams.put(PROPERTY_EMAIL, searchParams.getEmail());
        queryParams.put(PROPERTY_PHONE, searchParams.getPhone());
        queryParams.put(PROPERTY_IBAN, searchParams.getIban());

        return ParamsUtil.expand(queryParams);
    }

}
