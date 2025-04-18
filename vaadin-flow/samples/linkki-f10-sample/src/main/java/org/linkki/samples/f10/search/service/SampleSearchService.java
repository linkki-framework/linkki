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

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;

import org.linkki.samples.f10.search.SampleSearchParametersPmo;

/**
 * Dummy search service
 */
public class SampleSearchService {

    public SampleSearchResult search(SampleSearchParameters parameters) {
        if (StringUtils.isEmpty(parameters.getPartnerNumber())) {
            return new SampleSearchResult(Collections.emptyList(),
                    new MessageList(new Message("", "Bitte passende Suchparameter eingeben.", Severity.ERROR)));
        } else {
            var result = IntStream.range(0, getResultCount(parameters))//
                    .mapToObj(i -> createPerson(i, parameters))//
                    .toList();
            if (StringUtils.contains(parameters.getName(), "error")) {
                return new SampleSearchResult(result, new MessageList(
                        Message.newInfo("123",
                                        "This is a long message describing a potential problem with the input.")));
            } else {
                return new SampleSearchResult(result, new MessageList());
            }
        }

    }

    private int getResultCount(SampleSearchParameters parameters) {
        var sector = parameters.getSector();
        if (SampleSearchParametersPmo.SECTOR_FINANCE.equals(sector)) {
            return 500;
        } else if (SampleSearchParametersPmo.SECTOR_TELECOM.equals(sector)) {
            return 1;
        } else {
            return 20;
        }
    }

    private SampleModelObject createPerson(int i, SampleSearchParameters parameters) {
        var partnerNummer = StringUtils.isBlank(parameters.getPartnerNumber())
                ? ("P-" + i)
                : (parameters.getPartnerNumber() + "-" + i);
        return new SampleModelObject(partnerNummer,
                StringUtils.defaultIfBlank(StringUtils.join(parameters.getGivenName(), parameters.getName(), " "),
                                           "Partner " + i),
                parameters.getDateOfBirth() == null ? LocalDate.now().minusYears(40) : parameters.getDateOfBirth(),
                i % 3 == 0 ? "divers" : (i % 3 == 1 ? "männlich" : "weiblich"),
                StringUtils.join(parameters.getStreetAndNumber(), parameters.getPostalCode(), parameters.getCity()),
                "https://doc.faktorzehn.de/produkt-ui-guide/0.2/html/search/search.html?q=" + partnerNummer);
    }
}
