/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.shared.request;

import com.jvdsn.feather.shared.HttpAgent;

import java.io.IOException;

/**
 * Handles an HTTP request.
 *
 * @author Joachim Vandersmissen
 */
public interface HttpRequestHandler {
    /**
     * Handles an HTTP request.
     *
     * @param client  the client representing the sender of the HTTP request
     * @param request the HTTP request
     * @param body    the message body of the HTTP request
     */
    void handleRequest(HttpAgent client, HttpRequest request, byte[] body) throws IOException;
}
