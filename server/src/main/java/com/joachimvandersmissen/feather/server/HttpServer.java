/*
 * Copyright 2019 Joachim Vandersmissen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.joachimvandersmissen.feather.server;

import com.joachimvandersmissen.feather.shared.Headers;
import com.joachimvandersmissen.feather.shared.header.ContentLength;
import com.joachimvandersmissen.feather.shared.header.Header;
import com.joachimvandersmissen.feather.shared.request.HttpRequest;
import com.joachimvandersmissen.feather.shared.request.HttpRequestHandler;
import com.joachimvandersmissen.feather.shared.request.RequestMethod;
import com.joachimvandersmissen.feather.shared.response.HttpResponse;
import com.joachimvandersmissen.feather.shared.response.Status;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A threaded HTTP server.
 * Forwards HTTP requests to request handlers.
 *
 * @author Joachim Vandersmissen
 */
public class HttpServer {
    protected final Map<RequestMethod, Set<HttpRequestHandler>> requestHandlers = Collections.synchronizedMap(new EnumMap<>(RequestMethod.class));
    protected final ExecutorService executorService = Executors.newCachedThreadPool();
    protected final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    protected ServerHandler serverHandler;

    /**
     * Registers an HTTP request handler for a request method.
     *
     * @param requestMethod  The request method to register for.
     * @param requestHandler The request handler to add.
     */
    public void registerRequestHandler(RequestMethod requestMethod, HttpRequestHandler requestHandler) {
        this.requestHandlers.computeIfAbsent(requestMethod, $ -> new HashSet<>()).add(requestHandler);
    }

    /**
     * Unregisters an HTTP request handler for a request method.
     *
     * @param requestMethod  The request method to unregister for.
     * @param requestHandler The request handler to remove.
     */
    public void unregisterRequestHandler(RequestMethod requestMethod, HttpRequestHandler requestHandler) {
        this.requestHandlers.getOrDefault(requestMethod, Collections.emptySet()).remove(requestHandler);
    }

    /**
     * Starts the HTTP server bound to a port.
     *
     * @param port The port to bind to.
     */
    public void start(int port) throws IOException {
        System.out.println("Starting server on " + port + "...");
        this.serverHandler = new ServerHandler(this, new ServerSocket(port));
        this.executorService.submit(this.serverHandler);
        System.out.println("Started server!");
    }

    /**
     * Stops the HTTP server.
     */
    public void stop() throws IOException {
        System.out.println("Stopping server...");
        // Stop server handler first so we don't get new connections.
        this.serverHandler.stop();
        // Stop the client handlers next so we don't get new requests.
        for (ClientHandler clientHandler : this.clients) {
            clientHandler.stop();
        }

        // Stop the thread pool last when we're somewhat sure all handlers are stopped.
        this.executorService.shutdown();
        System.out.println("Stopped server!");
    }

    /**
     * Accepts the connection of a new client.
     *
     * @param socket The socket.
     */
    protected void acceptConnection(Socket socket) throws IOException {
        System.out.println("Accepting connection from " + socket.getInetAddress() + "...");
        ClientHandler clientHandler = new ClientHandler(this, socket);
        this.clients.add(clientHandler);
        this.executorService.submit(clientHandler);
        System.out.println("Accepted connection! (" + this.clients.size() + " connections so far)");
    }

    /**
     * Handles a client request by forwarding to the appropriate request handlers.
     * Responds with BAD_REQUEST if the client did not specify a Host.
     * Responds with INTERNAL_SERVER_ERROR if an error occured.
     *
     * @param client  The client.
     * @param request The request.
     */
    protected void handleRequest(ClientHandler client, HttpRequest request) throws IOException {
        try {
            byte[] body = client.receiveMessageBody(request.getHeaders());
            if (!request.getHeaders().contains(Header.HOST)) {
                client.sendResponse(new HttpResponse(Status.BAD_REQUEST, new Headers().with(Header.CONTENT_LENGTH, new ContentLength(BigInteger.ZERO))));
                return;
            }

            for (HttpRequestHandler requestHandler : this.requestHandlers.getOrDefault(request.getRequestMethod(), Collections.emptySet())) {
                requestHandler.handleRequest(client, request, body);
            }
        } catch (Exception e) {
            client.sendResponse(new HttpResponse(Status.INTERNAL_SERVER_ERROR, new Headers().with(Header.CONTENT_LENGTH, new ContentLength(BigInteger.ZERO))));
            e.printStackTrace();
        }
    }
}
