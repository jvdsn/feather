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

import com.joachimvandersmissen.feather.shared.HttpAgent;
import com.joachimvandersmissen.feather.shared.request.HttpRequest;

import java.io.IOException;

/**
 * Handles an HTTP response.
 *
 * @author Joachim Vandersmissen
 */
public interface HttpResponseHandler {
    /**
     * Handles an HTTP response.
     *
     * @param server       The server representing the sender of the HTTP response.
     * @param request      The original HTTP request.
     * @param body         The message body of the original HTTP request.
     * @param response     The HTTP response.
     * @param responseBody The message body of the HTTP response.
     */
    void handleResponse(HttpAgent server, HttpRequest request, byte[] body, HttpResponse response, byte... responseBody) throws IOException;
}
