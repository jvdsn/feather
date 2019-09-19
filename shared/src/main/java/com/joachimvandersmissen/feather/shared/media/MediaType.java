/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.media;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A media type.
 *
 * @author Joachim Vandersmissen
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public abstract class MediaType {
    public static final char TYPE_SEPARATOR = '/';
    public static final char PARAMETER_SEPARATOR = ';';
    public static final char PARAMETER_VALUE_SEPARATOR = '=';
    public static final String TYPE_TEXT = "text";
    protected final @NonNull String type;
    protected final @NonNull String subtype;
    protected final @NonNull Map<String, String> parameters;

    /**
     * Parses a string to a media type.
     *
     * @param s The string to parse.
     * @return The parsed media type.
     */
    public static MediaType parse(String s) {
        String[] splitted = s.split(String.valueOf(PARAMETER_SEPARATOR));
        int i = splitted[0].indexOf(TYPE_SEPARATOR);
        String type = splitted[0].substring(0, i).trim().toLowerCase();
        String subtype = splitted[0].substring(i + 1).trim().toLowerCase();
        Map<String, String> parameters = new HashMap<>();
        for (int j = 1; j < splitted.length; j++) {
            int k = splitted[j].indexOf(PARAMETER_VALUE_SEPARATOR);
            parameters.put(splitted[j].substring(0, k).trim().toLowerCase(), splitted[j].substring(k + 1).trim());
        }

        switch (type) {
            case TYPE_TEXT:
                return new TextMediaType(subtype, parameters);
            default:
                return new UnknownMediaType(type, subtype, parameters);
        }
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    /**
     * Converts some binary data to string according to this media type.
     *
     * @param data The data.
     * @return The string.
     */
    public abstract String toString(byte... data);

    @Override
    public String toString() {
        return this.parameters.entrySet().stream().map(parameter -> PARAMETER_SEPARATOR + parameter.getKey() + PARAMETER_VALUE_SEPARATOR + parameter.getValue()).collect(Collectors.joining("", this.type + TYPE_SEPARATOR + this.subtype, ""));
    }
}
