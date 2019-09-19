/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.shared.request;

import com.joachimvandersmissen.feather.shared.Headers;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * A generic HTTP request.
 *
 * @author Joachim Vandersmissen
 */
@Value
@AllArgsConstructor
public class HttpRequest {
    @NonNull RequestMethod requestMethod;
    @NonNull String resource;
    @NonNull String httpVersion;
    @NonNull Headers headers;

    /**
     * Constructs a new HTTP/1.1 request.
     *
     * @param requestMethod The request method.
     * @param resource The requested resource.
     * @param headers The headers.
     */
    public HttpRequest(RequestMethod requestMethod, String resource, Headers headers) {
        this(requestMethod, resource, "HTTP/1.1", headers);
    }
}
