/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.response;

import com.joachimvandersmissen.feather.shared.Headers;
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
     * @param statusCode The status code.
     * @param statusMessage The status message.
     * @param headers The headers.
     */
    public HttpResponse(int statusCode, String statusMessage, Headers headers) {
        this("HTTP/1.1", statusCode, statusMessage, headers);
    }

    /**
     * Constructs a new HTTP/1.1 response.
     *
     * @param status The status.
     * @param headers The headers.
     */
    public HttpResponse(Status status, Headers headers) {
        this(status.getCode(), status.getMessage(), headers);
    }
}
