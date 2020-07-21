/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.header;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Joachim Vandersmissen
 */
public class ConnectionTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new Connection(null));
        Connection connection = new Connection("test");
        Assertions.assertEquals("test", connection.getTokens());
    }

    @Test
    public void testDeserialize() {
        Connection connection = new Connection();
        Assertions.assertEquals("test", connection.deserialize("test").getTokens());
    }

    @Test
    public void testSerialize() {
        Header header = new Connection("test");
        Assertions.assertEquals("test", header.serialize());
    }
}
