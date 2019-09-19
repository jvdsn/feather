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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An HTTP header field.
 *
 * @author Joachim Vandersmissen
 */
public interface Header {
    String CONNECTION = "Connection";
    String CONTENT_LENGTH = "Content-Length";
    String CONTENT_TYPE = "Content-Type";
    String HOST = "Host";
    String IF_MODIFIED_SINCE = "If-Modified-Since";
    String TRANSFER_ENCODING = "Transfer-Encoding";
    char SEPARATOR = ':';

    /**
     * Returns a header by the field name, or an unknown header if the field name is unknown.
     *
     * @param fieldName The field name.
     * @return The header.
     */
    static Header getByFieldName(String fieldName) {
        switch (fieldName) {
            case CONNECTION:
                return new Connection();
            case CONTENT_LENGTH:
                return new ContentLength();
            case CONTENT_TYPE:
                return new ContentType();
            case HOST:
                return new Host();
            case IF_MODIFIED_SINCE:
                return new IfModifiedSince();
            case TRANSFER_ENCODING:
                return new TransferEncoding();
            default:
                return new UnknownHeader();
        }
    }

    /**
     * Converts a string to a # list.
     *
     * @param s        The string.
     * @param function The conversion function.
     * @return The list.
     */
    static <V> List<V> stringToList(String s, Function<String, V> function) {
        String[] splitted = s.split(",");
        return Arrays.stream(splitted).map(String::trim).filter(t -> !t.isEmpty()).map(function).collect(Collectors.toCollection(() -> new ArrayList<>(splitted.length)));
    }

    /**
     * Converts a # list to a string.
     *
     * @param list     The list.
     * @param function The conversion function.
     * @return The string.
     */
    static <V> String listToString(List<V> list, Function<V, String> function) {
        return list.stream().map(function).collect(Collectors.joining(","));
    }

    /**
     * Deserializes a string to this header field.
     *
     * @param s The string to deserialize.
     * @return This header field.
     */
    Header deserialize(String s);

    /**
     * Serializes this header field to a string.
     *
     * @return The string.
     */
    String serialize();
}
