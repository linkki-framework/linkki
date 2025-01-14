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

package org.linkki.samples.f10.search.service;

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

        searchParams.setPartnerNumber(parameters.get(SampleSearchParameters.PROPERTY_PARTNER_NUMBER));
        // end::toSearchParameters[]

        searchParams.setName(parameters.get(SampleSearchParameters.PROPERTY_NAME));
        searchParams.setGivenName(parameters.get(SampleSearchParameters.PROPERTY_GIVEN_NAME));
        // tag::parseIsoDate[]
        ParamsUtil.parseIsoDate(parameters.get(SampleSearchParameters.PROPERTY_DATE_OF_BIRTH))
                .ifPresent(searchParams::setDateOfBirth);
        // end::parseIsoDate[]
        searchParams.setSector(parameters.get(SampleSearchParameters.PROPERTY_SECTOR));
        searchParams.setClassification(parameters.get(SampleSearchParameters.PROPERTY_CLASSIFICATION));
        searchParams.setPostalCode(parameters.get(SampleSearchParameters.PROPERTY_POSTAL_CODE));
        searchParams.setCity(parameters.get(SampleSearchParameters.PROPERTY_CITY));
        searchParams.setStreetAndNumber(parameters.get(SampleSearchParameters.PROPERTY_STREE_AND_NUMBER));
        searchParams.setEmail(parameters.get(SampleSearchParameters.PROPERTY_EMAIL));
        searchParams.setPhone(parameters.get(SampleSearchParameters.PROPERTY_PHONE));
        searchParams.setIban(parameters.get(SampleSearchParameters.PROPERTY_IBAN));

        return searchParams;
    }

    @Override
    // tag::toQueryParameters[]
    public Map<String, List<String>> toQueryParameters(SampleSearchParameters searchParams) {
        var queryParams = new LinkedHashMap<String, String>();

        queryParams.put(SampleSearchParameters.PROPERTY_PARTNER_NUMBER, searchParams.getPartnerNumber());
        // end::toQueryParameters[]

        queryParams.put(SampleSearchParameters.PROPERTY_NAME, searchParams.getName());
        queryParams.put(SampleSearchParameters.PROPERTY_GIVEN_NAME, searchParams.getGivenName());
        // tag::formatIsoDate[]
        queryParams.put(SampleSearchParameters.PROPERTY_DATE_OF_BIRTH,
                        ParamsUtil.formatIsoDate(searchParams.getDateOfBirth()));
        // end::formatIsoDate[]
        queryParams.put(SampleSearchParameters.PROPERTY_SECTOR, searchParams.getSector());
        queryParams.put(SampleSearchParameters.PROPERTY_CLASSIFICATION, searchParams.getClassification());
        queryParams.put(SampleSearchParameters.PROPERTY_POSTAL_CODE, searchParams.getPostalCode());
        queryParams.put(SampleSearchParameters.PROPERTY_CITY, searchParams.getCity());
        queryParams.put(SampleSearchParameters.PROPERTY_STREE_AND_NUMBER, searchParams.getStreetAndNumber());
        queryParams.put(SampleSearchParameters.PROPERTY_EMAIL, searchParams.getEmail());
        queryParams.put(SampleSearchParameters.PROPERTY_PHONE, searchParams.getPhone());
        queryParams.put(SampleSearchParameters.PROPERTY_IBAN, searchParams.getIban());

        return ParamsUtil.expand(queryParams);
    }

}
