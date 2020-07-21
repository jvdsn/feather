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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * A simple data class combining a host and a port.
 *
 * @author Joachim Vandersmissen
 */
@Value
@RequiredArgsConstructor
public class HostAndPort {
    public static final char SEPARATOR = ':';
    @NonNull String host;
    int port;

    /**
     * Parses a string to a host and port.
     *
     * @param s the string to parse
     * @return the host and port
     */
    public static HostAndPort parse(String s) {
        s = s.trim();
        int i = s.lastIndexOf(SEPARATOR);
        return new HostAndPort(s.substring(0, i).trim(), Integer.parseInt(s.substring(i + 1).trim()));
    }

    @Override
    public String toString() {
        return this.host + SEPARATOR + this.port;
    }
}
