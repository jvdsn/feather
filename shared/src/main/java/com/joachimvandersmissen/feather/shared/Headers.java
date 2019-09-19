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

import com.joachimvandersmissen.feather.shared.header.Header;
import lombok.NonNull;
import lombok.Value;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Contains headers.
 *
 * @author Joachim Vandersmissen
 */
@Value
public class Headers {
    NavigableMap<String, Header> headers;

    /**
     * Constructs a new headers.
     *
     * @param headers The headers.
     */
    public Headers(@NonNull NavigableMap<String, Header> headers) {
        this.headers = Collections.unmodifiableNavigableMap(headers);
    }

    /**
     * Constructs a new headers containing no headers.
     */
    public Headers() {
        this(Collections.emptyNavigableMap());
    }

    /**
     * Adds a header and returns a new instance.
     *
     * @param fieldName The field name.
     * @param header    The header.
     * @return The new instance containing the new header.
     */
    public Headers with(String fieldName, Header header) {
        TreeMap<String, Header> treeMap = new TreeMap<>(this.headers);
        treeMap.put(fieldName, header);
        return new Headers(treeMap);
    }

    /**
     * Returns true if this headers contains a field name.
     *
     * @param fieldName The field name.
     * @return True if this headers contains the field name, false otherwise.
     */
    public boolean contains(String fieldName) {
        return this.headers.containsKey(fieldName);
    }

    /**
     * Returns the header associated with a field name.
     *
     * @param fieldName   The field name.
     * @param headerClass The class to cast the header to.
     * @return The header
     */
    public <H extends Header> Optional<H> get(String fieldName, Class<H> headerClass) {
        return Optional.ofNullable(this.headers.get(fieldName)).filter(headerClass::isInstance).map(headerClass::cast);
    }
}
