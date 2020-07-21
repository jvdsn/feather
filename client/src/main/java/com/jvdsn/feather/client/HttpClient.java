/*
 * Copyright 2020 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.jvdsn.feather.client;

import com.jvdsn.feather.shared.Headers;
import com.jvdsn.feather.shared.HostAndPort;
import com.jvdsn.feather.shared.HttpAgent;
import com.jvdsn.feather.shared.header.ContentLength;
import com.jvdsn.feather.shared.header.Header;
import com.jvdsn.feather.shared.header.Host;
import com.jvdsn.feather.shared.request.HttpRequest;
import com.jvdsn.feather.shared.request.RequestMethod;
import com.jvdsn.feather.shared.response.HttpResponse;
import com.jvdsn.feather.shared.response.HttpResponseHandler;
import lombok.extern.java.Log;

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
@Log
public class HttpClient {
    public static final byte[] EMPTY_BYTES = new byte[0];
    protected final Map<HostAndPort, HttpAgent> servers = new HashMap<>();

    /**
     * Connects to a server if needed.
     *
     * @param hostAndPort the host and port of the server
     * @return the remote server
     */
    public HttpAgent connect(HostAndPort hostAndPort) throws IOException {
        if (!this.servers.containsKey(hostAndPort)) {
            log.info(String.format("Connecting to %s...", hostAndPort));
            Socket socket = new Socket(hostAndPort.getHost(), hostAndPort.getPort());
            HttpAgent server = new HttpAgent(socket);
            this.servers.put(hostAndPort, server);
            log.info(String.format("Connected to %s!", hostAndPort));
            return server;
        }

        return this.servers.get(hostAndPort);
    }

    /**
     * Closes a connection to a server.
     *
     * @param hostAndPort the host and port of the server
     */
    public void closeConnection(HostAndPort hostAndPort) throws IOException {
        if (this.servers.containsKey(hostAndPort)) {
            log.info(String.format("Disconnecting %s", hostAndPort));
            this.servers.get(hostAndPort).disconnect();
            log.info(String.format("Connected from %s!", hostAndPort));
        }

        this.servers.remove(hostAndPort);
    }

    /**
     * Sends a request.
     *
     * @param hostAndPort      the host and port
     * @param requestMethod    the request method
     * @param resource         the resource to request, should start with /
     * @param body             the request body
     * @param responseHandlers the HTTP response handlers to use
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
