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

package org.linkki.samples.playground.ts.layouts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.faktorips.values.Decimal;

public class BasicElementsLayoutBehaviorModelObject {

    public static final String PROPERTY_TEXT = "text";
    public static final String PROPERTY_LONGTEXT = "longText";
    public static final String PROPERTY_INTVALUE = "intValue";
    public static final String PROPERTY_DOUBLEVALUE = "doubleValue";
    public static final String PROPERTY_DATE = "date";
    public static final String PROPERTY_DATE_TIME = "dateTime";
    public static final String PROPERTY_ENUMVALUE = "enumValue";
    public static final String PROPERTY_ENUMVALUES = "enumValues";
    public static final String PROPERTY_BOOLEANVALUE = "booleanValue";
    public static final String PROPERTY_SECRET = "secret";
    public static final String PROPERTY_BIG_DECIMAL = "bigDecimal";
    public static final String PROPERTY_DECIMALVALUE = "decimalValue";

    private String text = "I am a text";
    private String longText = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed "
            + "diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
            + "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. "
            + "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit "
            + "amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy "
            + "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam "
            + "voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet "
            + "clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    private int intValue = 42;
    private double doubleValue = 47.11;
    private LocalDate date = LocalDate.now();
    private LocalDateTime dateTime = LocalDateTime.now();
    private boolean booleanValue = true;
    private String secret = "secret";
    private BigDecimal bigDecimal = BigDecimal.valueOf(1234567890L, 5);
    private Decimal decimalValue = Decimal.valueOf(1234567890L, 5);
    private SampleEnum enumValue;
    private Set<SampleEnum> enumValues = Collections.emptySet();

    public enum SampleEnum {

        VALUE1("Value 1"),
        VALUE2("Value 2"),
        VALUE3("Value Three"),
        VALUE4("Value Four");

        private final String name;

        SampleEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Decimal getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(Decimal decimalValue) {
        this.decimalValue = decimalValue;
    }

    public SampleEnum getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(SampleEnum enumValue) {
        this.enumValue = enumValue;
    }

    public Set<SampleEnum> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(Set<SampleEnum> enumValues) {
        this.enumValues = enumValues;
    }

}
