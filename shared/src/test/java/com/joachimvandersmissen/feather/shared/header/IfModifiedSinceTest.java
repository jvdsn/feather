/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.header;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Joachim Vandersmissen
 */
public class IfModifiedSinceTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new IfModifiedSince(null));
        ZonedDateTime date = DateTimeFormatter.RFC_1123_DATE_TIME.parse("Tue, 3 Jun 2008 11:05:30 GMT", ZonedDateTime::from);
        IfModifiedSince ifModifiedSince = new IfModifiedSince(date);
        Assertions.assertEquals(date, ifModifiedSince.getDate());
    }

    @Test
    public void testDeserialize() {
        IfModifiedSince ifModifiedSince = new IfModifiedSince();
        ZonedDateTime date = DateTimeFormatter.RFC_1123_DATE_TIME.parse("Tue, 3 Jun 2008 11:05:30 GMT", ZonedDateTime::from);
        Assertions.assertEquals(date, ifModifiedSince.deserialize("Tue, 3 Jun 2008 11:05:30 GMT").getDate());
    }

    @Test
    public void testSerialize() {
        Header header = new IfModifiedSince(DateTimeFormatter.RFC_1123_DATE_TIME.parse("Tue, 3 Jun 2008 11:05:30 GMT", ZonedDateTime::from));
        Assertions.assertEquals("Tue, 3 Jun 2008 11:05:30 GMT", header.serialize());
    }
}
