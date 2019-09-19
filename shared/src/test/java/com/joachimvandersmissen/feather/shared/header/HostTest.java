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

import com.joachimvandersmissen.feather.shared.HostAndPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Joachim Vandersmissen
 */
public class HostTest {
    @Test
    public void testConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> new Host(null));
        HostAndPort hostAndPort = new HostAndPort("test", 10);
        Host host = new Host(hostAndPort);
        Assertions.assertSame(hostAndPort, host.getHostAndPort());
    }

    @Test
    public void testDeserialize() {
        Host host = new Host();
        Assertions.assertEquals(new HostAndPort("test", 10), host.deserialize(" test : 10 ").getHostAndPort());
    }

    @Test
    public void testSerialize() {
        Header header = new Host(new HostAndPort("test", 10));
        Assertions.assertEquals("test:10", header.serialize());
    }
}
