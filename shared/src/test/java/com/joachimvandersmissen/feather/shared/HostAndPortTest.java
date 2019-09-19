/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Joachim Vandersmissen
 */
public class HostAndPortTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new HostAndPort(null, 0));
        HostAndPort hostAndPort = new HostAndPort("test", 10);
        Assertions.assertEquals("test", hostAndPort.getHost());
        Assertions.assertEquals(10, hostAndPort.getPort());
    }

    @Test
    public void testParse() {
        HostAndPort hostAndPort = HostAndPort.parse(" test : 10");
        Assertions.assertEquals("test", hostAndPort.getHost());
        Assertions.assertEquals(10, hostAndPort.getPort());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("test:10", new HostAndPort("test", 10).toString());
    }
}
