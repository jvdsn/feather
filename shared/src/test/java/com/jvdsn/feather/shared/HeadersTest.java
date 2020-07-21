/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared;

import com.jvdsn.feather.shared.header.Header;
import com.jvdsn.feather.shared.header.UnknownHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author Joachim Vandersmissen
 */
public class HeadersTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new Headers(null));
        NavigableMap<String, Header> headers = new TreeMap<>();
        Assertions.assertEquals(headers, new Headers(headers).getHeaders());
        Assertions.assertEquals(headers, new Headers().getHeaders());
    }

    @Test
    public void testWith() {
        Headers headers = new Headers();
        Header header = new UnknownHeader("test");
        Headers with = headers.with("unknown", header);
        Assertions.assertTrue(with.contains("unknown"));
        Assertions.assertTrue(with.get("unknown", UnknownHeader.class).isPresent());
        Assertions.assertEquals(header, with.get("unknown", UnknownHeader.class).get());
    }
}
