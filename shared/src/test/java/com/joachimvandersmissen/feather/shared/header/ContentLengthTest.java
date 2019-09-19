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

import java.math.BigInteger;

/**
 * @author Joachim Vandersmissen
 */
public class ContentLengthTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new ContentLength(null));
        ContentLength contentLength = new ContentLength(BigInteger.TEN);
        Assertions.assertEquals(BigInteger.TEN, contentLength.getLength());
    }

    @Test
    public void testDeserialize() {
        ContentLength contentLength = new ContentLength();
        Assertions.assertEquals(BigInteger.TEN, contentLength.deserialize("10").getLength());
    }

    @Test
    public void testSerialize() {
        Header header = new ContentLength(BigInteger.TEN);
        Assertions.assertEquals("10", header.serialize());
    }
}
