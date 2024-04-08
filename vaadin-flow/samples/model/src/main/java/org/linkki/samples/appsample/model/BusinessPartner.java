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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.faktorips.runtime.MessageList;

public class BusinessPartner {

    public static final String MSG_TEXT_NAME = "The field Name must not be empty.";
    public static final String MSG_CODE_NAME = "error.checkName";

    private static final String PROPERTY_NAME = "name";

    private final UUID uuid;
    private String name;
    private LocalDate dateOfBirth;
    private List<Address> addresses = new ArrayList<>();
    private String note;
    private Status status;

    public BusinessPartner() {
        this.uuid = UUID.randomUUID();
    }

    public BusinessPartner(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public BusinessPartner(String name, LocalDate date, Status status) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.dateOfBirth = date;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getFirstAddress() {
        if (addresses.size() != 0) {
            Address address = addresses.get(0);
            return address.getStreet() + " " + address.getStreetNumber() + "\n" + address.getPostalCode()
                    + " " + address.getCity() + "\n" + address.getCountry();
        } else {
            return " ";
        }
    }

    /**
     * Adds the given {@link Address} to the addresses.
     * 
     * @param address Address to add
     */
    public void addAddress(Address address) {
        addresses.add(address);
    }

    /**
     * Remove given {@link Address} from the addresses.
     * 
     * @param address Address to remove
     */
    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public static enum Status {
        NEW_BUSINESS_PARTNER {
            @Override
            public String getCaption() {
                return "New Business Partner";
            }
        },
        NORMAL_BUSINESS_PARTNER {
            @Override
            public String getCaption() {
                return "Business Partner";
            }
        },
        VIP_BUSINESS_PARTNER {
            @Override
            public String getCaption() {
                return "VIP Business Partner";
            }
        };

        public abstract String getCaption();

        @Override
        public String toString() {
            return getCaption();
        }
    }

    public void copyPropertiesTo(BusinessPartner p) {
        p.name = name;
        p.dateOfBirth = dateOfBirth;
        p.status = status;
        p.note = note;
    }

    public MessageList validateName() {
        return Validation.validateRequiredProperty(getName(), MSG_CODE_NAME, MSG_TEXT_NAME, this, PROPERTY_NAME);
    }

    public MessageList validate() {
        MessageList msgList = new MessageList();
        msgList.add(validateName());
        return msgList;
    }
}
