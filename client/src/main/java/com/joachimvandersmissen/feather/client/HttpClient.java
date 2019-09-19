/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.client;

import com.joachimvandersmissen.feather.shared.Headers;
import com.joachimvandersmissen.feather.shared.HostAndPort;
import com.joachimvandersmissen.feather.shared.HttpAgent;
import com.joachimvandersmissen.feather.shared.header.ContentLength;
import com.joachimvandersmissen.feather.shared.header.Header;
import com.joachimvandersmissen.feather.shared.header.Host;
import com.joachimvandersmissen.feather.shared.request.HttpRequest;
import com.joachimvandersmissen.feather.shared.request.RequestMethod;
import com.joachimvandersmissen.feather.shared.response.HttpResponse;
import com.joachimvandersmissen.feather.shared.response.HttpResponseHandler;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * An HTTP client.
 * Provides methods to send arbitrary HTTP requests or just simple get/head/post/put methods.
 *
 * @author Joachim Vandersmissen
 */
public class HttpClient {
    public static final byte[] EMPTY_BYTES = new byte[0];
    protected final Map<HostAndPort, HttpAgent> servers = new HashMap<>();

    /**
     * Connects to a server if needed.
     *
     * @param hostAndPort The host and port of the server.
     * @return The remote server.
     */
    public HttpAgent connect(HostAndPort hostAndPort) throws IOException {
        if (!this.servers.containsKey(hostAndPort)) {
            Socket socket = new Socket(hostAndPort.getHost(), hostAndPort.getPort());
            HttpAgent server = new HttpAgent(socket);
            this.servers.put(hostAndPort, server);
            return server;
        }

        return this.servers.get(hostAndPort);
    }

    /**
     * Closes a connection to a server.
     *
     * @param hostAndPort The host and port of the server.
     */
    public void closeConnection(HostAndPort hostAndPort) throws IOException {
        if (this.servers.containsKey(hostAndPort)) {
            this.servers.get(hostAndPort).disconnect();
        }

        this.servers.remove(hostAndPort);
    }

    /**
     * Sends a request.
     *
     * @param hostAndPort      The host and port.
     * @param requestMethod    The request method.
     * @param resource         The resource to request. The resource should start with /
     * @param body             The request body.
     * @param responseHandlers The HTTP response handlers to use.
     */
    public void sendRequest(HostAndPort hostAndPort, RequestMethod requestMethod, String resource, byte[] body, HttpResponseHandler... responseHandlers) throws IOException {
        HttpAgent server = this.connect(hostAndPort);
        Header host = new Host(hostAndPort);
        Header contentLength = new ContentLength(BigInteger.valueOf(body.length));
        Headers headers = new Headers().with(Header.HOST, host).with(Header.CONTENT_LENGTH, contentLength);
        HttpRequest request = new HttpRequest(requestMethod, resource, headers);
        server.sendRequest(request);
        server.sendMessageBody(headers, body);
        HttpResponse response = server.receiveResponse();
        byte[] responseBody = EMPTY_BYTES;
        if (requestMethod != RequestMethod.HEAD) {
            responseBody = server.receiveMessageBody(response.getHeaders());
        }

        for (HttpResponseHandler responseHandler : responseHandlers) {
            responseHandler.handleResponse(server, request, body, response, responseBody);
        }
    }
}
