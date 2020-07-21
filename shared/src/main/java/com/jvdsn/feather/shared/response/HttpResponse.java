/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.response;

import com.jvdsn.feather.shared.Headers;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * A generic HTTP response.
 *
 * @author Joachim Vandersmissen
 */
@Value
@AllArgsConstructor
public class HttpResponse {
    @NonNull String httpVersion;
    int statusCode;
    @NonNull String statusMessage;
    @NonNull Headers headers;

    /**
     * Constructs a new HTTP/1.1 response.
     *
     * @param statusCode    the status code
     * @param statusMessage the status message
     * @param headers       the headers
     */
    public HttpResponse(int statusCode, String statusMessage, Headers headers) {
        this("HTTP/1.1", statusCode, statusMessage, headers);
    }

    /**
     * Constructs a new HTTP/1.1 response.
     *
     * @param status  the status
     * @param headers the headers
     */
    public HttpResponse(Status status, Headers headers) {
        this(status.getCode(), status.getMessage(), headers);
    }
}
